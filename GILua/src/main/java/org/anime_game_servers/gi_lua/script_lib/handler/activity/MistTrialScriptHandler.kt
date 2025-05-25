package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the MistTrial activity.
 * These are only callable from a group context.
 */
interface MistTrialScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun failMistTrialDungeonChallenge(context: GroupEventContext, challengeIndex: Int): Int
    fun setMistTrialServerGlobalValue(context: GroupEventContext, floorLevel: Int): Int
}