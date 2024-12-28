package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the SeaLamp activity.
 * These are only callable from a group context.
 */
interface LanternRiteScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getLanternRiteValue(context: GroupEventContext): Int
    fun setLanternRiteValue(context: GroupEventContext, value: Int): Int
}