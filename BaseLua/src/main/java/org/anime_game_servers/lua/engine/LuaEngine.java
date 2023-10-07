package org.anime_game_servers.lua.engine;

import org.anime_game_servers.lua.models.IntValueEnum;
import org.anime_game_servers.lua.models.ScriptType;
import org.anime_game_servers.lua.serialize.Serializer;

public interface LuaEngine {
    //TODO move to a better place
    static ScriptFinder scriptFinder = null;

    <T extends Enum<T>> boolean addGlobalEnumByOrdinal(String name, T[] enumArray);

    <T extends Enum<T> & IntValueEnum> boolean addGlobalEnumByIntValue(String name, T[] enumArray);

    boolean addGlobalStaticClass(String name, Class<?> staticClass);

    boolean addObject(String name, Object object);

    Serializer getSerializer();

    LuaScript getScript(String scriptName, ScriptType scriptType);

    LuaTable getTable(Object table);

    LuaTable createTable();

}
