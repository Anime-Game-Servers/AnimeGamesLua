package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the SeaLamp/Lantern Rite from cb3 activity.
 * These are only callable from a group context.
 */
interface SeaLampScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getSeaLampActivityPhase(context: GroupEventContext): Int
}