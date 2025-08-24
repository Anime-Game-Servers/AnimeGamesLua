package org.anime_game_servers.gi_lua.script_lib.handler

import org.anime_game_servers.gi_lua.script_lib.LuaContext

interface ScriptLibStaticHandler {
    fun printLog(msg: String?)
    fun getEntityType(entityId: Int): Int
    fun printContextLog(context: LuaContext, msg: String)
}
