package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.models.constants.SealBattleType

interface SealBattleParams {
    val sealBattleType: SealBattleType
    val radius: Int
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
    val kill_time: Int,
    val monster_group_id: Int,
    val maxProgress: Int,
) : SealBattleParams {
    override val sealBattleType : SealBattleType = SealBattleType.KILL_MONSTER
}
