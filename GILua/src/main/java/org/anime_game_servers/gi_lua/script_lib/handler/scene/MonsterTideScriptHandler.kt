package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable


data class PoolMonsterTideConfig(
    @field:LuaNames("total_count")
    val totalCount: Int,
    @field:LuaNames("min_count")
    val minCount: Int,
    @field:LuaNames("max_count")
    val maxCount: Int,
    val tag: Int,
    @field:LuaNames("fill_time")
    val fillTime: Int,
    @field:LuaNames("fill_count")
    val fillCount: Int,
    @field:LuaNames("is_ordered")
    val isOrdered: Boolean,
    @field:LuaNames("is_tag_bit_match")
    val isTagBitMatch: Boolean,
    @field:LuaNames("sgv_map")
    val sgvMap: Map<String, Number>?,
){
    companion object {
        fun fromLuaTable(table: LuaTable): PoolMonsterTideConfig? {
            return PoolMonsterTideConfig(
                totalCount = table.optInt("total_count", 0),
                minCount = table.optInt("min_count", 0),
                maxCount = table.optInt("max_count", 0),
                tag = table.optInt("tag", 0),
                fillTime = table.optInt("fill_time", 0),
                fillCount = table.optInt("fill_count", 0),
                isOrdered = table.optBoolean("is_ordered", false),
                isTagBitMatch = table.optBoolean("is_tag_bit_match", false),
                sgvMap = table.getTable("sgv_map")?.let {
                    it.getKeys().associateWith { key -> it.optInt(key, 0) }
                }
            )
        }
    }
}

/**
 * Handler for scriptlib functions with the Monstertides.
 * These are only callable from a lua group context.
 */
interface MonsterTideScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun autoMonsterTide(
        context: GroupEventContext,
        tideId: Int,
        groupId: Int,
        ordersConfigId: List<Int>,
        tideCount: Int,
        sceneLimit: Int,
        param6: Int
    ): Int

    fun killMonsterTide(context: GroupEventContext, groupId: Int, tideId: Int): Int
    fun autoPoolMonsterTide(
        context: GroupEventContext,
        index: Int,
        groupId: Int,
        monsterPool: List<Int>,
        routeId: Int,
        routePoints: List<Int>,
        monsterAffix: List<Int>,
        monsterPoolParam: PoolMonsterTideConfig
    ): Int

    fun clearPoolMonsterTide(context: GroupEventContext, groupId: Int, tideNum: Int): Int
    fun endMonsterTide(context: GroupEventContext, groupId: Int, tideIndex: Int, endType: Int): Int
    fun endPoolMonsterTide(context: GroupEventContext, groupId: Int, index: Int): Int
    fun pauseAutoMonsterTide(context: GroupEventContext, groupId: Int, monsterTideIndex: Int): Int
    fun pauseAutoPoolMonsterTide(context: GroupEventContext, groupId: Int, tideStage: Int): Int
    fun resumeAutoPoolMonsterTide(context: GroupEventContext, groupId: Int, tideStage: Int): Int
    fun continueAutoMonster(context: GroupEventContext, groupId: Int, tideNum: Int): Int
}
