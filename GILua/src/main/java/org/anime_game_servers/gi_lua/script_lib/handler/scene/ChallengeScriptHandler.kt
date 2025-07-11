package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.ChallengeEventMarkType
import org.anime_game_servers.gi_lua.models.constants.FatherChallengeProperty
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

data class CreateFatherChallengeParameters(
    val success: Int,
    val fail: Int,
    @LuaNames("fail_on_wipe")
    val failOnWipe: Boolean
) {
    companion object {
        fun fromLuaTable(table: LuaTable): CreateFatherChallengeParameters? {
            val success = table.optInt("success", -1)
            val fail = table.optInt("fail", -1)
            val failOnWipe = table.optBoolean("fail_on_wipe", false)

            if (success == -1 || fail == -1) {
                return null // Invalid or missing data
            }

            return CreateFatherChallengeParameters(
                success = success,
                fail = fail,
                failOnWipe = failOnWipe
            )
        }
    }
}
data class AttachChildChallengePointConfig(
    val success: Int,
    val fail: Int,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): AttachChildChallengePointConfig? {
            val success = table.optInt("success", -1)
            val fail = table.optInt("fail", -1)

            if (success == -1 || fail == -1) {
                return null // Invalid or missing data
            }

            return AttachChildChallengePointConfig(
                success = success,
                fail = fail,
            )
        }
    }
}

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
        challengeParams: List<Int>
    ): Int

    fun stopChallenge(context: GroupEventContext, challengeIndex: Int, result: Int): Int
    fun pauseChallenge(context: GroupEventContext, challengeIndex: Int): Int

    fun createFatherChallenge(
        context: GroupEventContext,
        challengeIndex: Int,
        challengeId: Int,
        timeLimit: Int,
        parameters: CreateFatherChallengeParameters
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
        context: GroupEventContext, fatherChallengeIndex: Int, childChallengeIndex: Int, childChallengeId: Int,
        parameterList: List<Int>, uidList: List<Int>, pointConfig: AttachChildChallengePointConfig
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