package org.anime_game_servers.gi_lua.script_lib

interface ControllerLuaContext<GadgetEntity> : LuaContext {
    val gadget:GadgetEntity
}
