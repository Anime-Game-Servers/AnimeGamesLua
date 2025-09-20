package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.gi_lua.models.constants.EntityType

data class ScenePoint(
    val tag: Long = 0
) : SceneObject() {
    override val type: EntityType = NONE
}
