package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.lua.engine.LuaEngine

interface LuaContext {
    val engine: LuaEngine

    // fields used by some scripts
    fun uid(): Int
    fun sourceEntityId(): Int
    fun targetEntityId(): Int
    fun ownerUid(): Int
}
