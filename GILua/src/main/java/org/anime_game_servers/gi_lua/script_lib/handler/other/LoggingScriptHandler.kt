package org.anime_game_servers.gi_lua.script_lib.handler.other

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

/**
 * Handler for scriptlib functions related to some type of logging
 * These are only callable from a lua group context.
 */
interface LoggingScriptHandler<GroupEventContext : GroupEventLuaContext> {

    fun printGroupWarning(context: GroupEventContext, msg: String)

    // seems to be logging/debug related
    // on its done on the scene owner
    fun markPlayerAction(context: GroupEventContext, var1: Int, var2: Int, var3: Int): Int
    fun markGroupLuaAction(context: GroupEventContext, action: String, transaction: String, log: LuaTable): Int
}
