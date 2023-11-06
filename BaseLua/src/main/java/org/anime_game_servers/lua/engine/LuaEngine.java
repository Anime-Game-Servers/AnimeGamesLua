package org.anime_game_servers.lua.engine;

import org.anime_game_servers.lua.models.IntValueEnum;
import org.anime_game_servers.lua.models.ScriptType;
import org.anime_game_servers.lua.serialize.Serializer;

import java.nio.file.Path;

public interface LuaEngine {
    ScriptConfig getScriptConfig();

    <T extends Enum<T>> boolean addGlobalEnumByOrdinal(String name, T[] enumArray);

    <T extends Enum<T> & IntValueEnum> boolean addGlobalEnumByIntValue(String name, T[] enumArray);

    boolean addGlobalStaticClass(String name, Class<?> staticClass);

    boolean addObject(String name, Object object);

    Serializer getSerializer();

    default LuaScript getScript(String scriptName, ScriptType scriptType){
        final Path scriptPath = getScriptConfig().getScriptLoader().getScriptPath(scriptName);
        return getScript(scriptPath, scriptType);
    }
    LuaScript getScript(Path scriptPath, ScriptType scriptType);

    LuaTable getTable(Object table);

    LuaTable createTable();

}
