package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the LunaRite activity.
 * These are only callable from a group context.
 */
interface LunaRiteScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getLunaRiteSacrificeNum(context: GroupEventContext, areaId: Int): Int
}