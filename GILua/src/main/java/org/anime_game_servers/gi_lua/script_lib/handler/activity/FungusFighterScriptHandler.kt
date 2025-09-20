package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

data class FungusFighterTrainingParams(
    @field:LuaNames("rand_index")
    var randIndex: Int,
    @field:LuaNames("monster_pool_list")
    var monsterPoolList: List<Int>,
)

/**
 * This handles script lib functions that are specific to the FungusFighter activity.
 * These are only callable from a group context.
 */
interface FungusFighterScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun setCurFungusFighterTrainingParams(context: GroupEventContext, params: FungusFighterTrainingParams): Int
    fun getCurFungusFighterPlotConfigIdList(context: GroupEventContext): List<Int>
    fun getCurFungusFighterTrainingParams(context: GroupEventContext): List<Int>
    fun getCurFungusFighterTrainingValidBackupFungusIdList(context: GroupEventContext): List<Int>
    fun isFungusCaptured(context: GroupEventContext, uid: Int, fungusMonsterId: Int): Int
}