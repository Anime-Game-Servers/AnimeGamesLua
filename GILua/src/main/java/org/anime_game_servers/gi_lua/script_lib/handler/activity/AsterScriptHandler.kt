package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Aster/Unreconciled Stars activity.
 * These are only callable from a group context.
 */
interface AsterScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun createAsterMidGeneralRewardGadget(context: GroupEventContext, configId: Int, difficultyId: Int): Int
}