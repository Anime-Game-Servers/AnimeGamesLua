package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames

data class OfferingConfig (
    @field:LuaNames("offering_id")
    val offeringId: Int = 0
)
