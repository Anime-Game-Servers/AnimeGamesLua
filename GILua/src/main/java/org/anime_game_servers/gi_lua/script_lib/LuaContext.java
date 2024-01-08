package org.anime_game_servers.gi_lua.script_lib;

import org.anime_game_servers.lua.engine.LuaEngine;

public interface LuaContext {
    LuaEngine getEngine();
    ScriptLibHandler getScriptLibHandler();

    // fields used by some scripts
    /*int uid();
    int source_entity_id();
    int target_entity_id();*/
}
