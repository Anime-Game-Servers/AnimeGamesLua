package org.anime_game_servers.jnlua_engine;

import lombok.val;
import org.anime_game_servers.lua.engine.LuaEngine;
import org.anime_game_servers.lua.engine.LuaScript;
import org.anime_game_servers.lua.engine.LuaValue;
import org.anime_game_servers.lua.models.BooleanLuaValue;
import org.anime_game_servers.lua.models.IntLuaValue;
import org.anime_game_servers.lua.models.ScriptType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terasology.jnlua.script.CompiledLuaScript;
import org.terasology.jnlua.script.LuaBindings;
import org.terasology.jnlua.script.LuaScriptEngine;

import javax.script.*;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class JNLuaScript implements LuaScript {

    final Reader scriptReader;
    @Nullable
    private final CompiledLuaScript compiledScript;
    private final LuaBindings binding;
    private final JNLuaEngine engine;
    private final LuaScriptEngine scriptEngine;
    SimpleScriptContext context = new SimpleScriptContext();

    JNLuaScript(JNLuaEngine engine, Path scriptPath, ScriptType scriptType) throws ScriptException, IOException {
        this.engine = engine;
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
        scriptReader = Files.newBufferedReader(scriptPath);
        if(scriptType.getPrecompile()) {
            this.compiledScript = (CompiledLuaScript) ((Compilable) scriptEngine).compile(scriptReader);
        } else {
            this.compiledScript = null;
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
            scriptEngine.eval(scriptReader, context);
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
