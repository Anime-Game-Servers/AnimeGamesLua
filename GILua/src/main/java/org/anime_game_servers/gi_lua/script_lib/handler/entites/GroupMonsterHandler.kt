package org.anime_game_servers.gi_lua.script_lib.handler.entites

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.utils.ScriptUtils.toVector
import org.anime_game_servers.lua.engine.LuaTable

data class MonsterFaceAvatarParameters(
    var entityId: Int,
    var monsters: List<Int>,
    var ranges: Pair<Int, Int>,
    var angle: Int
)

data class CreateMonsterParameters(
    var configId: Int,
    var delayTime: Int = 0,
    var level: Int = 0,
    var affixList: List<Int>? = null,
    var pos: Vector? = null,
    var rot: Vector? = null,
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