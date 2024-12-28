package org.anime_game_servers.gi_lua.script_lib;

import org.anime_game_servers.lua.engine.LuaEngine;

public interface LuaContext {
    LuaEngine getEngine();

    // fields used by some scripts
    int uid();
    int sourceEntityId();
    int targetEntityId();
    int ownerUid();
}
