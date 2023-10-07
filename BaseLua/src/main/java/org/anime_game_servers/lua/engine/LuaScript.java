package org.anime_game_servers.lua.engine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

public interface LuaScript {
    boolean hasMethod(@Nonnull String methodName);

    @Nullable
    LuaValue callMethod(@Nonnull String methodName, Object... args) throws ScriptException, NoSuchMethodException;

    void evaluate() throws ScriptException;

    <T> T getGlobalVariable(String name, Class<T> type);

    <T> List<T> getGlobalVariableList(String name, Class<T> type);

    <T> Map<String, T> getGlobalVariableMap(String name, Class<T> type);

    LuaEngine getEngine();
}
