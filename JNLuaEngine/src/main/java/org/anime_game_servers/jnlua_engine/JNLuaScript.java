package org.anime_game_servers.jnlua_engine;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import kotlin.Pair;
import lombok.val;
import org.anime_game_servers.lua.engine.LuaEngine;
import org.anime_game_servers.lua.engine.LuaScript;
import org.anime_game_servers.lua.engine.LuaValue;
import org.anime_game_servers.lua.engine.RequireMode;
import org.anime_game_servers.lua.models.BooleanLuaValue;
import org.anime_game_servers.lua.models.IntLuaValue;
import org.anime_game_servers.lua.models.MutableBoolean;
import org.anime_game_servers.lua.models.ScriptType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terasology.jnlua.script.CompiledLuaScript;
import org.terasology.jnlua.script.LuaBindings;
import org.terasology.jnlua.script.LuaScriptEngine;

import javax.script.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class JNLuaScript implements LuaScript {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(JNLuaScript.class.getName());
    @Nullable
    private final CompiledLuaScript compiledScript;
    private final String modifiedScript;
    private final LuaBindings binding;
    private final JNLuaEngine engine;
    private final LuaScriptEngine scriptEngine;
    SimpleScriptContext context = new SimpleScriptContext();
    private Path scriptPath;

    JNLuaScript(JNLuaEngine engine, Path scriptPath, ScriptType scriptType) throws ScriptException, IOException {
        this.engine = engine;
        this.scriptPath = scriptPath;
        this.scriptEngine = (LuaScriptEngine) engine.getManager().getEngineByName("jnlua");
        if(scriptType.getAddDefaultGlobals()) {
            context.setBindings(engine.getBindings(), ScriptContext.GLOBAL_SCOPE);
        }
        this.binding = (LuaBindings) scriptEngine.createBindings();
        context.setBindings(binding, ScriptContext.ENGINE_SCOPE);
        val luaState = binding.getLuaState();
        luaState.setConverter(JNLuaConverter.getINSTANCE());

        val requireFunction = JNLuaRequireCommonFunction.getInstance(engine.getScriptConfig());
        binding.put(requireFunction.getName(), requireFunction);
        if (engine.getScriptConfig().getEnableIncludeWorkaround() == RequireMode.ENABLED_WITH_WORKAROUND &&
                (scriptType == ScriptType.EXECUTABLE || scriptType == ScriptType.STATIC_EXECUTABLE || scriptType == ScriptType.ONE_TIME_EXECUTABLE)) {
            this.modifiedScript = compileScriptWithWorkaround(scriptPath);
        } else {
            this.modifiedScript = null;
        }

        if(scriptType.getPrecompile()) {
             if(modifiedScript != null) {
                this.compiledScript = (CompiledLuaScript) ((Compilable) scriptEngine).compile(modifiedScript);
            } else {
                 try (val reader = getReader()) {
                     this.compiledScript = (CompiledLuaScript) ((Compilable) scriptEngine).compile(reader);
                 }
             }
        } else {
            this.compiledScript = null;
        }
    }

    private BufferedReader getReader() throws IOException {
        return Files.newBufferedReader(scriptPath);
    }

    // todo maybe caching?
    private String compileScriptWithWorkaround(Path path) throws IOException,ScriptException {
        val requireRegex = Pattern.compile("\\s*require\\s+\"(.*)\"");
        val changed = new MutableBoolean(false);
        try (val reader = getReader()){
            val script = reader.lines().map(line -> {
                val result = requireRegex.matcher(line);
                if (result.matches()) {
                    val requireBasePath = engine.getScriptConfig().getScriptLoader().getRequireScriptParams(result.group(1)).getBasePath();
                    val requirePath = engine.getScriptConfig().getScriptLoader().getScriptPath(requireBasePath);
                    if(requirePath == null){
                        logger.warn(()->"Could not find require script "+result.group(1)+" for script "+ path);
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
            return changed.getValue() ? script : null;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean hasMethod(@NotNull String methodName) {
        return binding.containsKey(methodName);
    }

    @Nullable
    @Override
    public LuaValue callMethod(@NotNull String methodName, Object... args) throws ScriptException, NoSuchMethodException {
        val result = ((Invocable) scriptEngine).invokeFunction(methodName, args);
        if (result instanceof Boolean) {
            return ((Boolean) result) ? BooleanLuaValue.TRUE : BooleanLuaValue.FALSE;
        } else if (result instanceof Integer) {
            return new IntLuaValue((Integer) result);
        }
        //TODO
        return null;
    }

    @Override
    public void evaluate() throws ScriptException {
        if(compiledScript != null) {
            compiledScript.eval(context);
        } else {
            if(modifiedScript != null) {
                scriptEngine.eval(modifiedScript, context);
            } else {
                try (val reader = getReader()) {
                    scriptEngine.eval(reader, context);
                } catch (IOException e) {
                    throw new ScriptException(e);
                }
            }
        }
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
