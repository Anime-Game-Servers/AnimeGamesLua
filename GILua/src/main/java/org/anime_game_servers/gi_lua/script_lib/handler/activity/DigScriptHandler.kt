package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Dig activity.
 * These are only callable from a group context.
 */
interface DigScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun digRetractAllWidget(context: GroupEventContext): Int
    fun digSetSearchingTarget(context: GroupEventContext, configId: Int): Int
}