package org.anime_game_servers.gi_lua.script_lib.handler.entites

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

data class MonsterFaceAvatarParameters(
    var entityId: Int,
    var monsters: List<Int>,
    var ranges: Pair<Int, Int>,
    var angle: Int
)

interface GroupMonsterHandler<GroupEventContext : GroupEventLuaContext> {
    fun createMonster(context: GroupEventContext, table: LuaTable?): Int
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