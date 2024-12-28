package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the HideAndSeek activities.
 * These are only callable from a group context.
 */
interface HideAndSeekScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getHideAndSeekPlayIndex(context: GroupEventContext): Int // returns index
    fun getHideAndSeekHunter(context: GroupEventContext, index: Int): Int // returns uid
    fun getHideAndSeekPreyUidList(context: GroupEventContext, index: Int): List<Int> //returns uid list
    fun getHideAndSeekMap(context: GroupEventContext, index: Int): Int // returns map id
    fun getHideAndSeekPlayGalleryId(context: GroupEventContext, index: Int): Int // returns gallery id
    fun getHideAndSeekPlayerSkillList(context: GroupEventContext, index: Int, uid: Int): List<Int>
}