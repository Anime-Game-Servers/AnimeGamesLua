package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames

data class SceneMonsterPool (
    @field:LuaNames("pool_id")
    val poolId: Int = 0,

    @field:LuaNames("rand_weight")
    val randWeight: Int = 0,
)
