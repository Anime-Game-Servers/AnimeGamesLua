package org.anime_game_servers.gi_lua.script_lib.handler.entites

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.utils.ScriptUtils.toVector
import org.anime_game_servers.lua.engine.LuaTable

private val scriptLogger = logger {}

data class MonsterFaceAvatarParameters(
    @field:LuaNames("entity_id")
    var entityId: Int,
    var monsters: List<Int>,
    var ranges: Pair<Int, Int>,
    var angle: Int
) {
    companion object {
        fun fromLuaTable(table: LuaTable): MonsterFaceAvatarParameters? {
            val entityId = table.optInt("entity_id", -1)
            val monsters = table.getTable("monsters")?.getAsIntArray()?.toList()
            val ranges = table.getTable("ranges")?.let {
                val list = it.getAsIntArray()
                if(list.size != 2){
                    scriptLogger.error { "[MonsterFaceAvatarParameters] Invalid ranges size ${it.getSize()}" }
                    return null
                }
                Pair(list[0], list[1])
            }
            val angle = table.optInt("angle", 0)
            if(monsters == null || ranges == null || angle == -1 || entityId == -1){
                scriptLogger.error { "[MonsterFaceAvatarParameters] Invalid or missing monsters, ranges, angle or entityId" }
                return null
            }
            return MonsterFaceAvatarParameters(
                entityId = entityId,
                monsters = monsters,
                ranges = ranges,
                angle = angle
            )
        }
    }
}

data class CreateMonsterParameters(
    @field:LuaNames("config_id")
    var configId: Int,
    @field:LuaNames("delay_time")
    var delayTime: Int = 0,
    var level: Int = 0,
    @field:LuaNames("affix_list")
    var affixList: List<Int>? = null,
    var pos: Vector? = null,
    var rot: Vector? = null,
    @field:LuaNames("server_global_value")
    var sgvTable: Map<String, Number>? = null
) {
    companion object {
        fun fromLuaTable(table: LuaTable): CreateMonsterParameters? {
            val configId = table.optInt("config_id", 0)
            val delayTime = table.optInt("delay_time", 0)
            val level = table.optInt("level", 0)
            val affixList = table.getTable("affix_list")?.getAsIntArray()?.toList()
            val pos = table.getTable("pos")?.toVector()
            val rot = table.getTable("rot")?.toVector()
            val sgvTable = table.getTable("server_global_value")
            val sgvMap = sgvTable?.getKeys()?.let { keys ->
                keys.associateWith { key -> sgvTable.getInt(key) }
            }
            return CreateMonsterParameters(
                configId = configId,
                delayTime = delayTime,
                level = level,
                affixList = affixList,
                pos = pos,
                rot = rot,
                sgvTable = sgvMap
            )
        }
    }
}

interface GroupMonsterHandler<GroupEventContext : GroupEventLuaContext> {
    fun createMonster(context: GroupEventContext, parameters: CreateMonsterParameters): Int
    fun createMonsterWithGlobalValue(context: GroupEventContext, configId: Int, sgvTable: Map<String, Number>): Int
    fun createMonsterByConfigIdByPos(context: GroupEventContext, configId: Int, pos: Vector, rot: Vector): Int
    fun createMonsterFaceAvatar(context: GroupEventContext, params: MonsterFaceAvatarParameters): Int
    fun createMonstersFromMonsterPool(context: GroupEventContext, poolName: String): Int

    fun getMonsterIdByEntityId(context: GroupEventContext, entityId: Int): Int
    fun getMonsterConfigId(context: GroupEventContext, entityId: Int): Int

    fun getMonsterHpPercent(context: GroupEventContext, groupId: Int, configId: Int): Int
    fun getMonsterAffixListByConfigId(context: GroupEventContext, groupId: Int, configId: Int): List<Int>

    fun lockMonsterHp(context: GroupEventContext, configId: Int): Int
    fun unlockMonsterHp(context: GroupEventContext, configId: Int): Int
    fun setMonsterHp(context: GroupEventContext, groupId: Int, configId: Int, hpPercent: Int): Int
    fun setMonsterAIByGroup(context: GroupEventContext, aiId: Int, cfgId: Int, groupId: Int): Int
}