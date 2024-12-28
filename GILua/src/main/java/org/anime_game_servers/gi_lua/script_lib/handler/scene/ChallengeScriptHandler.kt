package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.gi_lua.models.constants.ChallengeEventMarkType
import org.anime_game_servers.gi_lua.models.constants.FatherChallengeProperty
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

/**
 * Handler for scriptlib functions used in Challenges
 * These are only callable from a lua group context.
 */
interface ChallengeScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun activeChallenge(
        context: GroupEventContext,
        challengeIndex: Int,
        challengeId: Int,
        timeLimitOrGroupId: Int,
        groupId: Int,
        objectiveKills: Int,
        param5: Int
    ): Int

    fun startChallenge(
        context: GroupEventContext,
        challengeIndex: Int,
        challengeId: Int,
        challengeParams: LuaTable?
    ): Int

    fun stopChallenge(context: GroupEventContext, challengeIndex: Int, result: Int): Int
    fun pauseChallenge(context: GroupEventContext, challengeIndex: Int): Int

    fun createFatherChallenge(
        context: GroupEventContext,
        challengeIndex: Int,
        challengeId: Int,
        timeLimit: Int,
        conditionTable: LuaTable?
    ): Int

    fun startFatherChallenge(context: GroupEventContext, challengeIndex: Int): Int
    fun endFatherChallenge(context: GroupEventContext, fatherChallengeIndex: Int): Int

    fun modifyFatherChallengeProperty(
        context: GroupEventContext,
        challengeId: Int,
        propertyTypeIndex: FatherChallengeProperty,
        value: Int
    ): Int

    fun attachChildChallenge(
        context: GroupEventContext, fatherChallengeIndex: Int, childChallengeIndex: Int,
        childChallengeId: Int, var4: LuaTable?, var5: LuaTable?, var6: LuaTable?
    ): Int


    fun getChallengeTransaction(context: GroupEventContext, challengeId: Int): Int

    fun isChallengeStartedByChallengeId(context: GroupEventContext, challengeId: Int): Boolean
    fun isChallengeStartedByChallengeIndex(context: GroupEventContext, groupId: Int, challengeIndex: Int): Boolean

    /**
     * Adds or removed time from the challenge
     * TODO verify and implement
     * @param context
     * @param challengeId The active target challenges id
     * @param duration The duration to add or remove
     * @return 0 if success, 1 if no challenge is active, 2 if the challenge id doesn't match the active challenge,
     * 3 if modifying the duration failed
     */
    fun addChallengeDuration(context: GroupEventContext, challengeId: Int, duration: Int): Int
    fun setChallengeDuration(context: GroupEventContext, challengeId: Int, time: Int): Int


    fun setChallengeEventMark(context: GroupEventContext, challengeId: Int, eventMarkType: ChallengeEventMarkType): Int

}