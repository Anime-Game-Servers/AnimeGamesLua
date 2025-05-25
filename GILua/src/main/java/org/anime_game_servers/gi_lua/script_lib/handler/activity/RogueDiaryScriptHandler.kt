package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Rogue Diary activity.
 * The Rogue Diary activity also makes use of some [RoguelikeScriptHandler] functions
 * These are only callable from a group context.
 */
interface RogueDiaryScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getRogueDiaryDungeonStage(context: GroupEventContext): Int
    fun getRogueDiaryRoundAndRoom(context: GroupEventContext): List<Int>
    fun finishRogueDiaryDungeonSingleRoom(context: GroupEventContext, isFailed: Boolean): Int
}