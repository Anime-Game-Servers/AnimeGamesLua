package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the TreasureMap activity.
 * These are only callable from a group context.
 */
interface TreasureMapScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun createTreasureMapSpotRewardGadget(context: GroupEventContext, gadgetCfgId: Int): Int
    fun getBonusTreasureMapSolution(context: GroupEventContext, groupId: Int): Int
}