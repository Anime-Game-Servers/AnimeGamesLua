package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.interfaces.IntKey
import org.anime_game_servers.core.base.interfaces.StringKey

// todo move all to val when possible

data class SuiteDisk(
    var monsters: List<SuiteDiskMonster> = emptyList(),
    var gadgets: List<SuiteDiskGadget> = emptyList(),
    var regions: Set<Int> = emptySet(),
    var npcs: Set<Int> = emptySet(),
    var triggers: Set<String> = emptySet(),
    var variables: List<SuiteDiskVariable> = emptyList()
)


data class SuiteDiskVariable(
    @field:LuaNames("config_id")
    var configId: Int = 0,
    var name: String = "",
    var value: Int = 0,
    @field:LuaNames("no_refresh")
    var noRefresh:Boolean = false,
) : IntKey, StringKey {
    override fun getIntKey() = configId
    override fun getStringKey() = name
}

data class SuiteDiskMonster(
    @field:LuaNames("config_id")
    var configId: Int = 0,
): IntKey {
    override fun getIntKey() = configId
}

data class SuiteDiskGadget(
    @field:LuaNames("config_id")
    var configId: Int = 0,
    var state: Int = 0,
    @field:LuaNames("platform_info")
    var platformInfo:PlatformInfo? = null,
): IntKey {
    override fun getIntKey() = configId
}

data class PlatformInfo(
    @field:LuaNames("point_id")
    var pointId: Int = 0,
    @field:LuaNames("move_type")
    var moveType: Int = 0,
    @field:LuaNames("route_id")
    var routeId: Int = 0,
    @field:LuaNames("route_index")
    var routeIndex: Int = 0,
    @field:LuaNames("is_started")
    var isStarted: Boolean = false,
)