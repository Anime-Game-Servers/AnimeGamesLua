package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames

data class SceneBossChest(
    @field:LuaNames("life_time")
    val lifeTime: Int = 0,
    @field:LuaNames("monster_config_id")
    val monsterConfigId: Int = 0,
    val resin: Int = 0,
    @field:LuaNames("take_num")
    val takeNum: Int = 0
)
