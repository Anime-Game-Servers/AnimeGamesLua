package org.anime_game_servers.gi_lua.models.scene.group

data class SceneGarbage (
    val monsters: List<SceneMonster>? = null,
    val gadgets: List<SceneGadget>? = null,
    val regions: List<SceneRegion>? = null,
    val triggers: List<SceneTrigger>? = null
) {
    fun isEmpty() = gadgets.isNullOrEmpty()
}
fun SceneGarbage?.isEmpty() = this == null || this.isEmpty()
