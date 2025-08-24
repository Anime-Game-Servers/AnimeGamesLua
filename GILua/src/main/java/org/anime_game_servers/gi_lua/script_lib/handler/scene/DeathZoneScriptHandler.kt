package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions used for the [DeathZone/WitheringZone](https://genshin-impact.fandom.com/wiki/The_Withering) systems.
 * These are only callable from a lua group context.
 */
interface DeathZoneScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun changeDeathZone(context: GroupEventContext, deathZoneId: Int, isOpen: Boolean): Int
    fun getDeathZoneStatus(context: GroupEventContext, deathZoneId: Int): Int
}
