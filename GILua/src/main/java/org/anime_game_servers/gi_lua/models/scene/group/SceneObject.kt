package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.interfaces.IntKey
import org.anime_game_servers.gi_lua.models.PositionImpl
import org.anime_game_servers.gi_lua.models.constants.EntityType
import org.anime_game_servers.gi_lua.models.scene.SceneMeta

abstract class SceneObject : IntKey {
    @field:LuaNames("config_id")
    val configId: Int = 0

    @field:LuaNames("area_id")
    val areaId: Int = 0

    val pos: PositionImpl? = null
    val rot: PositionImpl? = null

    /**
     * not set by lua
     */
    @Transient
    var groupId: Int = 0
        internal set

    @Transient
    var blockId: Int = 0
        internal set

    @Transient
    var sceneMeta: SceneMeta? = null
        internal set

    override fun getIntKey() = configId
    abstract val type: EntityType?
}
