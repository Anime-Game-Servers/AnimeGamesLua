package org.anime_game_servers.gi_lua.script_lib.handler.parameter

import org.anime_game_servers.gi_lua.models.constants.EntityType

data class KillByConfigIdParams(
    val configId: Int = 0,
    val groupId: Int = 0,
    val entityType: EntityType = EntityType.NONE
)
