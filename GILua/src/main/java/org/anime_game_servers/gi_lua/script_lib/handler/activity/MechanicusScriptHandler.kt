package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.annotations.lua.LuaStatic
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

@LuaStatic
@LuaNames("MechanicusChallenge")
enum class MechanicusChallengeState{
    @LuaNames("None")
    NONE,
    @LuaNames("OnGoing")
    ON_GOING,
    @LuaNames("Fail")
    FAIL,
    @LuaNames("Success")
    SUCCESS
}

/**
 * This handles script lib functions that are specific to the Mechanicus activity.
 * These are only callable from a group context.
 */
interface MechanicusScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun addMechanicusBuildingPoints(context: GroupEventContext, groupId: Int, playIndex: Int, uid: Int, delta: Int): Int
    fun getMechanicusBuildingPoints(context: GroupEventContext, groupId: Int, playIndex: Int, uid: Int): Int

    fun setMechanicusChallengeState(
        context: GroupEventContext,
        groupId: Int,
        playIndex: Int,
        cardId: Int,
        effectId: Int,
        state: MechanicusChallengeState
    ): Int

    fun getMechanicusMonsterPoolVec(context: GroupEventContext, groupId: Int, playIndex: Int): List<Int>
    fun setMechanicusMonsterPoolVec(context: GroupEventContext, groupId: Int, playIndex: Int, monsterPool: List<Int>): Int
}