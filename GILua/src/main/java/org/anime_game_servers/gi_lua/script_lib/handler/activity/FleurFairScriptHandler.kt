package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the FleurFair/Windblume activity.
 * These are only callable from a group context.
 */
interface FleurFairScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun addFleurFairMultistagePlayBuffEnergy(
        context: GroupEventContext,
        groupId: Int,
        param2: Int,
        uid: Int,
        bonusId: Int
    ): Int

    fun finishFleurFairGalleryStageByUid(
        context: GroupEventContext,
        groupId: Int,
        var2: Int,
        uid: Int,
        var4: Boolean
    ): Int

    fun getFleurFairDungeonSectionId(context: GroupEventContext): Int
    fun getFleurFairMultistagePlayBuffEnergy(context: GroupEventContext, groupId: Int, var2: Int, uid: Int): Int
    fun getFleurFairMultistagePlayGalleryIdVec(context: GroupEventContext, groupId: Int, var2: Int): List<Int>
    fun getFleurFairMultistagePlayGalleryTempValue(
        context: GroupEventContext,
        groupId: Int,
        var2: Int,
        tmpValueKey: String
    ): Int
}