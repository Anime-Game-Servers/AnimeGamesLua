package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames

data class WorktopConfig (
    @field:LuaNames("init_options")
    val initOptions: Set<Int>? = null,
    @field:LuaNames("is_persistent")
    val isPersistent: Boolean = false
)
