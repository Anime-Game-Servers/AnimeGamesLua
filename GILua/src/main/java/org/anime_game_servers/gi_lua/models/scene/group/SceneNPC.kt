package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.EntityType

data class SceneNPC(
    @field:LuaNames("npc_id")
    val npcId: Int = 0,
    val room: Int = 0,
    val pointID: Int = 0,
    ) : SceneObject() {
    override val type: EntityType = NPC
}
