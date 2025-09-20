package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames

data class CrucibleConfig (
    val duration: Int = 0,
    @field:LuaNames("start_cd")
    val startCd: Int = 0,
    @field:LuaNames("mp_play_id")
    val mpPlayId: Int = 0,
    @field:LuaNames("progress_stage")
    val progressStageList: List<Int>? = null
)
