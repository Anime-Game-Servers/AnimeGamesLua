package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the CharAmusement activity.
 * These are only callable from a group context.
 */
interface CharAmusementScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun charAmusementMultistagePlaySwitchTeam(context: GroupEventContext, groupId: Int, playIndex: Int, stageIndex: Int): Int
    fun charAmusementUpdateScore(context: GroupEventContext, groupId: Int, playIndex: Int, scoreChange: Int): Int
    fun getCharAmusementGalleryTarget(context: GroupEventContext, galleryId: Int, isMultiplayer: Boolean): Int
    fun getCharAmusementMultistagePlayGalleryIdVec(context: GroupEventContext, groupId: Int, playIndex: Int): List<Int>
}