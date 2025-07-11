package org.anime_game_servers.gi_lua.script_lib.handler.other

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions in tower/spiralAbyss.
 * These are only callable from a lua group context.
 */
interface TowerScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun towerCountTimeStatus(context: GroupEventContext, isDone: Int): Int
    fun towerMirrorTeamSetUp(context: GroupEventContext, team: Int): Int
}
