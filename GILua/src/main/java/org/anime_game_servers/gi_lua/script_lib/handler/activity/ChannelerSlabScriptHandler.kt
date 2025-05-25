package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the ChannelerSlab activity.
 * These are only callable from a group context.
 */
interface ChannelerSlabScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun isChannellerSlabLoopDungeonConditionSelected(context: GroupEventContext, conditionId: Int): Boolean
}