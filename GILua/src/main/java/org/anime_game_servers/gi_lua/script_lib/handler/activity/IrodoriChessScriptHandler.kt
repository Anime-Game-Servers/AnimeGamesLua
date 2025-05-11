package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the IrodoriChess activity.
 * These are only callable from a group context.
 */
interface IrodoriChessScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun addIrodoriChessBuildingPoints(context: GroupEventContext, groupId: Int, playIndex: Int, points: Int): Int
    fun addIrodoriChessTowerServerGlobalValue(context: GroupEventContext, groupId: Int, playIndex: Int, gadgetId: Int, sgvDeltas: Map<String, Int>): Int
    fun getIrodoriChessSelectedCards(context: GroupEventContext, groupId: Int, playIndex: Int): Int
    fun destroyIrodoriChessTower(context: GroupEventContext, entityId: Int, groupId: Int, playIndex: Int): Int
    fun forceSetIrodoriFoundationTowers(context: GroupEventContext, configIdGearMap: Map<Int, Int>, groupId: Int, playIndex: Int): Int
}