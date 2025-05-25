package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the UGC activity.
 * These are only callable from a group context.
 */
interface UgcDungeonScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun enterCustomDungeonOfficialEdit(context: GroupEventContext, roomId: Int): Int
    fun getCurrentCustomDungeonForbidSkill(context: GroupEventContext): Boolean
    fun getCurrentCustomDungeonParamVec(context: GroupEventContext): List<Int>
    fun getCustomDungeonCoinNum(context: GroupEventContext): Int
    fun getCustomDungeonOpenRoomVec(context: GroupEventContext): List<Int>
}