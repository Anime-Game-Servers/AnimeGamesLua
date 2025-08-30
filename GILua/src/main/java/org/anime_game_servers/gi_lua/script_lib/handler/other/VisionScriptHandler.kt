package org.anime_game_servers.gi_lua.script_lib.handler.other

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions interacting with the vision/view system
 * These are only callable from a lua group context.
 */
interface VisionScriptHandler<GroupEventContext : GroupEventLuaContext> {
    // Vision/view stuff
    fun addPlayerGroupVisionType(context: GroupEventContext, uids: List<Int>, visionTypeList: List<Int>): Int
    fun delPlayerGroupVisionType(context: GroupEventContext, uids: List<Int>, visionTypeList: List<Int>): Int
    fun setPlayerGroupVisionType(context: GroupEventContext, uids: List<Int>, visionTypeList: List<Int>): Int

    fun revertPlayerRegionVision(context: GroupEventContext, uid: Int): Int
    fun forbidPlayerRegionVision(context: GroupEventContext, uid: Int): Int

    // updates the state and sends PlayerEyePointStateNotify
    fun setPlayerEyePointStream(
        context: GroupEventContext,
        targetRegionConfigId: Int,
        relatedRegionConfigId: Int,
        isStream: Boolean
    ): Int

    fun setPlayerEyePoint(context: GroupEventContext, targetRegionConfigId: Int, relatedRegionConfigId: Int): Int
    fun setPlayerEyePointLOD(
        context: GroupEventContext,
        targetRegionConfigId: Int,
        relatedRegionConfigId: Int,
        fixLodLevel: Int
    ): Int

    fun clearPlayerEyePoint(context: GroupEventContext, targetRegionConfigId: Int): Int


    // sends AvatarFollowRouteNotify to viewing players
    fun moveAvatarByPointArray(
        context: GroupEventContext,
        uid: Int,
        pointArrayId: Int,
        routeList: List<Int>,
        speed: Float,
        clientParams: String
    ): Int

    // sends AvatarFollowRouteNotify to viewing players
    fun moveAvatarByPointArrayWithTemplate(
        context: GroupEventContext,
        uid: Int,
        pointArrayId: Int,
        routeList: List<Int>,
        templateId: Int,
        speed: Float
    ): Int

    // sends MonsterForceAlertNotify to group viewing players
    fun setMonsterBattleByGroup(context: GroupEventContext, configId: Int, groupId: Int): Int
}
