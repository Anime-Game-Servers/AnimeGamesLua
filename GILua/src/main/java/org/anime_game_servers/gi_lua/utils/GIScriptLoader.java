package org.anime_game_servers.gi_lua.utils;

import org.anime_game_servers.lua.engine.LuaScript;
import org.anime_game_servers.lua.models.ScriptType;

public interface GIScriptLoader {
    LuaScript getSceneScript(int sceneId, String scriptName, ScriptType scriptType);
}
