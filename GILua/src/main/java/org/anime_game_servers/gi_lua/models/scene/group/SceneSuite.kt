package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames

data class SceneSuite (
    // make it refer the default empty list to avoid NPE caused by some group
    val monsters: List<Int> = emptyList(),
    val gadgets: List<Int> = emptyList(),
    val triggers: List<String> = emptyList(),
    val regions: List<Int> = emptyList(),
    val npcs: List<Int> = emptyList(),

    @field:LuaNames("rand_weight")
    val randWeight: Int = 0,

    @field:LuaNames("ban_refresh")
    val banRefresh: Boolean = false
){
    @Transient
    var sceneMonsters = emptyList<SceneMonster>()
        internal set

    @Transient
    var sceneGadgets = emptyList<SceneGadget>()
        internal set

    @Transient
    var sceneTriggers = emptyList<SceneTrigger>()
        internal set

    @Transient
    var sceneRegions = emptyList<SceneRegion>()
        internal set

    @Transient
    var sceneNPCs = emptyList<SceneNPC>()
        internal set

    fun init(sceneGroup: SceneGroup) {
        val monsters: Map<Int, SceneMonster>? = sceneGroup.monsters
        if (monsters != null) {
            this.sceneMonsters = this.monsters.stream()
                .filter { key: Int -> monsters.containsKey(key) }
                .map<SceneMonster> { key: Int -> monsters[key] }
                .toList()
        }

        val gadgets: Map<Int, SceneGadget>? = sceneGroup.gadgets
        if (gadgets != null) {
            this.sceneGadgets = this.gadgets.stream()
                .filter { key: Int -> gadgets.containsKey(key) }
                .map<SceneGadget> { key: Int -> gadgets[key] }
                .toList()
        }

        val triggers: Map<String, SceneTrigger>? = sceneGroup.triggers
        if (triggers != null) {
            this.sceneTriggers = this.triggers.stream()
                .filter { key: String -> triggers.containsKey(key) }
                .map<SceneTrigger> { key: String -> triggers[key] }
                .toList()
        }
        val regions: Map<Int, SceneRegion>? = sceneGroup.regions
        if (regions != null) {
            this.sceneRegions = this.regions.stream()
                .filter { key: Int -> regions.containsKey(key) }
                .map<SceneRegion> { key: Int -> regions[key] }
                .toList()
        }
        val npcs: Map<Int, SceneNPC>? = sceneGroup.npcs
        if (npcs != null) {
            this.sceneNPCs = this.npcs.stream()
                .filter { key: Int -> npcs.containsKey(key) }
                .map<SceneNPC> { key: Int -> npcs[key] }
                .toList()
        }
    }
}
