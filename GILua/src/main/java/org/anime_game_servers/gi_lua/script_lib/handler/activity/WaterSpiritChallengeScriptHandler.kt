package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the WaterSpiritChallenge/Wishful Drops activity.
 * These are only callable from a group context.
 */
interface WaterSpiritChallengeScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun addRegionRecycleProgress(context: GroupEventContext, regionId: Int, delta: Int): Int
    fun addRegionSearchProgress(context: GroupEventContext, regionId: Int, delta: Int): Int
}