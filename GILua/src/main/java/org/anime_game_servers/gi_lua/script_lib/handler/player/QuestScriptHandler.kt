package org.anime_game_servers.gi_lua.script_lib.handler.player

import org.anime_game_servers.core.gi.enums.QuestState
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext


/**
 * Handler for scriptlib functions directly interacting with the questing system
 * These are only callable from a lua group context.
 */
interface QuestScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun addQuestProgress(context: GroupEventContext, eventNotifyName: String): Int
    fun getHostQuestState(context: GroupEventContext, questId: Int): QuestState
    fun getQuestState(context: GroupEventContext, entityId: Int, questId: Int): QuestState
    fun getQuestStateByUid(context: GroupEventContext, uid: Int, questId: Int): QuestState
}