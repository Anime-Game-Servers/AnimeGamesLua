package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

/**
 * This handles script lib functions that are specific to the EffigyChallenge activity.
 * These are only callable from a group context.
 */
interface EffigyScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun createEffigyChallengeMonster(context: GroupEventContext, var1: Int, var2Table: LuaTable?): Int
    fun getEffigyChallengeMonsterLevel(context: GroupEventContext): Int
    fun getEffigyChallengeLimitTime(context: GroupEventContext): Int
    fun getEffigyChallengeV2DungeonDifficulty(context: GroupEventContext): Int
    fun isEffigyChallengeConditionSelected(context: GroupEventContext, conditionId: Int): Boolean
}