package org.anime_game_servers.luaj_engine;

import lombok.val;
import org.anime_game_servers.lua.engine.LuaEngine;
import org.anime_game_servers.lua.engine.LuaScript;
import org.anime_game_servers.lua.engine.LuaValue;
import org.anime_game_servers.lua.models.BooleanLuaValue;
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

public class LuaJScript implements LuaScript {
    private final CompiledScript compiledScript;
    private final Bindings binding;
    private final LuaJEngine engine;

    public LuaJScript(LuaJEngine engine, Path scriptPath) throws IOException, ScriptException {
        this.engine = engine;
        this.compiledScript = ((Compilable) engine.getEngine()).compile(Files.newBufferedReader(scriptPath));
        this.binding = engine.getEngine().createBindings();
    }

    @Override
    public boolean hasMethod(@Nonnull String methodName) {
        return binding.containsKey(methodName) && ((org.luaj.vm2.LuaValue) binding.get(methodName)).isfunction();
    }

    @Override
    public LuaValue callMethod(@Nonnull String methodName, Object... args) {
        val function = (org.luaj.vm2.LuaValue) binding.get(methodName);
        if (function == null || !function.isfunction()) {
            //LuaEngine.logger.warn("Attempted to call method {} on script {} but it does not exist or is not a function", methodName, this);
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
