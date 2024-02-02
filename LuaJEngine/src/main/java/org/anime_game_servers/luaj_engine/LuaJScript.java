package org.anime_game_servers.luaj_engine;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import kotlin.Pair;
import kotlin.text.Regex;
import lombok.val;
import org.anime_game_servers.lua.engine.LuaEngine;
import org.anime_game_servers.lua.engine.LuaScript;
import org.anime_game_servers.lua.engine.LuaValue;
import org.anime_game_servers.lua.engine.RequireMode;
import org.anime_game_servers.lua.models.BooleanLuaValue;
import org.anime_game_servers.lua.models.MutableBoolean;
import org.anime_game_servers.lua.models.ScriptType;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import javax.annotation.Nonnull;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class LuaJScript implements LuaScript {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(LuaJScript.class.getName());
    private final CompiledScript compiledScript;
    private final String modifiedScript;
    private final Bindings binding;
    private final LuaJEngine engine;

    public LuaJScript(LuaJEngine engine, Path scriptPath, ScriptType scriptType) throws IOException, ScriptException {
        this.engine = engine;
        if (engine.getScriptConfig().getEnableIncludeWorkaround() == RequireMode.ENABLED_WITH_WORKAROUND && (scriptType == ScriptType.EXECUTABLE || scriptType == ScriptType.STATIC_EXECUTABLE || scriptType == ScriptType.ONE_TIME_EXECUTABLE)) {
            val result = compileScriptWithWorkaround(scriptPath);
            this.compiledScript = result.getFirst();
            this.modifiedScript = result.getSecond();
        } else {
            this.compiledScript = ((Compilable) engine.getEngine()).compile(Files.newBufferedReader(scriptPath));
            this.modifiedScript = null;
        }
        this.binding = engine.getEngine().createBindings();
    }

    // todo maybe caching?
    private Pair<CompiledScript, String> compileScriptWithWorkaround(Path path) throws IOException,ScriptException {
        val requireRegex = Pattern.compile("\\s*require\\s+\"(.*)\"");
        val changed = new MutableBoolean(false);
        try (val reader = Files.newBufferedReader(path)){
            val script = reader.lines().map(line -> {
                val result = requireRegex.matcher(line);
                if (result.matches()) {
                    val requireBasePath = engine.getScriptConfig().getScriptLoader().getRequireScriptParams(result.group(1)).getBasePath();
                    val requirePath = engine.getScriptConfig().getScriptLoader().getScriptPath(requireBasePath);
                    if(requirePath == null){
                        logger.warn(()->"Could not find require script "+result.group(1)+" for script "+path);
                        return line;
                    }
                    try {
                        try (val requireReader = Files.newBufferedReader(requirePath)){
                            val requireScript = requireReader.lines().reduce((a, b) -> a + "\n" + b).orElse(line);
                            changed.setValue(true);
                            return requireScript;
                        }
                    } catch (IOException e) {
                        return line;
                    }
                } else {
                    return line;
                }
            }).reduce((a, b) -> a + "\n" + b).orElse("");
            String modifiedScript = changed.getValue() ? script : null;
            return new Pair<>(((Compilable) engine.getEngine()).compile(script), modifiedScript);
        }
    }

    @Override
    public boolean hasMethod(@Nonnull String methodName) {
        return binding.containsKey(methodName) && ((org.luaj.vm2.LuaValue) binding.get(methodName)).isfunction();
    }

    @Override
    public LuaValue callMethod(@Nonnull String methodName, Object... args) {
        val function = (org.luaj.vm2.LuaValue) binding.get(methodName);
        if (function == null || !function.isfunction()) {
            logger.warn(() -> "Attempted to call method "+methodName+" on script "+this+" but it does not exist or is not a function");
            return BooleanLuaValue.FALSE;
        }

        org.luaj.vm2.LuaValue[] luaArgs;

        luaArgs = new org.luaj.vm2.LuaValue[args.length];
        for (int i = 0; i < args.length; i++)
            luaArgs[i] = CoerceJavaToLua.coerce(args[i]);

        return new LuaJValue(engine, function.invoke(luaArgs).arg1());
    }

    @Override
    public void evaluate() throws ScriptException {
        this.compiledScript.eval(this.binding);
    }

    @Override
    public <T> T getGlobalVariable(String name, Class<T> type) {
        return engine.getSerializer().toObject(type, binding.get(name));
    }

    @Override
    public <T> List<T> getGlobalVariableList(String name, Class<T> type) {
        return engine.getSerializer().toList(type, binding.get(name));
    }

    @Override
    public <T> Map<String, T> getGlobalVariableMap(String name, Class<T> type) {
        return engine.getSerializer().toMap(type, binding.get(name));
    }

    @Override
    public LuaEngine getEngine() {
        return engine;
    }
}
