package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.interfaces.IntKey
import org.anime_game_servers.core.base.interfaces.StringKey

data class SceneVar (
    @field:LuaNames("config_id")
    val configId: Int = 0,
    val name: String = "",
    val value: Int = 0,
    @field:LuaNames("no_refresh")
    val noRefresh: Boolean = false
) : IntKey, StringKey {
    override fun getIntKey() = configId
    override fun getStringKey() = name
}
