package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.gi_lua.models.constants.SealBattleType
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

private fun LuaTable.parseSealBattleNoneParams(): SealBattleParams {
    val radius = optInt("radius", -1)
    val inAdd = optInt("in_add", -1)
    val outSub = optInt("out_sub", -1)
    val failTime = optInt("fail_time", -1)
    val maxProgress = optInt("max_progress", -1)
    // TODO check params and maybe return error?
    return DefaultSealBattleParams(radius, inAdd, outSub, failTime, maxProgress)
}

private fun LuaTable.parseSealBattleMonsterKillParams(): SealBattleParams {
    val radius = optInt("radius", -1)
    val killTime = optInt("kill_time", -1)
    val monsterGroupId = optInt("monster_group_id", -1)
    val maxProgress = optInt("max_progress", -1)
    // TODO check params and maybe return error?
    return MonsterSealBattleParams(radius, killTime, monsterGroupId, maxProgress)
}

private fun LuaTable.parseEnergySealBattleTimeParams(): SealBattleParams {
    val radius = optInt("radius", -1)
    val battleTime = optInt("battle_time", -1)
    val monsterGroupId = optInt("monster_group_id", -1)
    val defaultKillCharge = optInt("default_kill_charge", -1)
    val autoCharge = optInt("auto_charge", -1)
    val autoDecline = optInt("auto_decline", -1)
    val maxEnergy = optInt("max_energy", -1)
    // TODO check params and maybe return error?
    return EnergySealBattleParams(
        radius,
        battleTime,
        monsterGroupId,
        defaultKillCharge,
        autoCharge,
        autoDecline,
        maxEnergy
    )
}

interface SealBattleParams {
    val sealBattleType: SealBattleType
    val radius: Int
    companion object {
        fun fromSealBattleType(battleParams: LuaTable) : SealBattleParams? {
            val battleType = battleParams.optInt("battle_type", -1)
            if (battleType < 0 || battleType >= SealBattleType.entries.size) {
                return null
            }
            val battleTypeEnum = SealBattleType.entries[battleType]
            return when (battleTypeEnum) {
                SealBattleType.NONE -> battleParams.parseSealBattleNoneParams()
                SealBattleType.KILL_MONSTER -> battleParams.parseSealBattleMonsterKillParams()
                SealBattleType.ENERGY_CHARGE -> battleParams.parseEnergySealBattleTimeParams()
            }
        }
    }
}

data class EnergySealBattleParams(
    override val radius: Int,
    val battleTime: Int,
    val monsterGroupId: Int,
    val defaultKillCharge: Int,
    val autoCharge: Int,
    val autoDecline: Int,
    val maxEnergy: Int,
) : SealBattleParams {
    override val sealBattleType: SealBattleType = SealBattleType.ENERGY_CHARGE
}

data class DefaultSealBattleParams(
    override val radius: Int,
    val inAdd: Int,
    val outSub: Int,
    val failTime: Int,
    val maxProgress: Int,
) : SealBattleParams {
    override val sealBattleType: SealBattleType = SealBattleType.NONE
}

data class MonsterSealBattleParams(
    override val radius: Int,
    val killTime: Int,
    val monsterGroupId: Int,
    val maxProgress: Int,
) : SealBattleParams {
    override val sealBattleType : SealBattleType = SealBattleType.KILL_MONSTER
}


/**
 * Handler for scriptlib functions used in the SealBattle mechanic
 * These are only callable from a lua group context.
 */
interface SealBattleScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun startSealBattle(context: GroupEventContext, gadgetId: Int, params: SealBattleParams): Int
}