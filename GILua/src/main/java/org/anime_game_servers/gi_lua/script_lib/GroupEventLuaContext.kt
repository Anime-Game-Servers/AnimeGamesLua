package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.models.SceneGroup
import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.lua.engine.LuaEngine

interface GroupEventLuaContext: LuaContext {
    fun getGroupInstance(): SceneGroup
    fun getArgs(): ScriptArgs
}