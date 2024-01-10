package org.anime_game_servers.gi_lua.script_lib.handler

import org.anime_game_servers.gi_lua.script_lib.LuaContext

interface ScriptLibStaticHandler {
    fun PrintLog(msg: String?)
    fun PrintContextLog(context: LuaContext, msg: String)
    fun GetEntityType(entityId: Int): Int

}
