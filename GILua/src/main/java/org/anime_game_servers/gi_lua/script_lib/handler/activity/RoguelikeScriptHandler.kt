package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Roguelike activity, but some are also used in the Rogue Diary event.
 * These are only callable from a group context.
 */
interface RoguelikeScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun triggerRoguelikeCurseByLua(context: GroupEventContext, uid: Int): Int
    fun doRoguelikeCardGachaByLua(context: GroupEventContext, uid: Int): Int
    fun disableRoguelikeTrapBySgv(context: GroupEventContext, sgvName: String, uid: Int): Int
    fun setRogueCellState(context: GroupEventContext, groupId: Int, state: Int): Int
    fun isRogueBossCellPrevCellFinish(context: GroupEventContext): Boolean
    fun getRogueCellState(context: GroupEventContext, groupId: Int): Int
    fun enterRogueCell(context: GroupEventContext, groupId: Int): Int
    fun enterRogueDungeonNextLevel(context: GroupEventContext): Int
}