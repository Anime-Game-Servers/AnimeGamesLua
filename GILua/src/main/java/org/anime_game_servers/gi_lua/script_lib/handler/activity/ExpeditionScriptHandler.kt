package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Expedition activity that first happened in 1.3.
 * These are only callable from a group context.
 */
interface ExpeditionScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun finishExpeditionChallenge(context: GroupEventContext): Int
    fun expeditionChallengeEnterRegion(context: GroupEventContext, isFinished: Boolean): Int
}