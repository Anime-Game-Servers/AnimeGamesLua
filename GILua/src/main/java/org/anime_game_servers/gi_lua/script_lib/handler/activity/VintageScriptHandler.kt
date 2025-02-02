package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Vintage activity.
 * These are only callable from a group context.
 */
interface VintageScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun vintageFinishGroupByPresentId(context: GroupEventContext, presentId: Int): Int
}