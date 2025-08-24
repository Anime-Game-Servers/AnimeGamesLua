package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
// todo move all to val when possible

data class SuiteDisk(
    var monsters: List<SuiteDiscMonster> = emptyList(),
    var gadgets: List<SuiteDiscGadget> = emptyList(),
    var regions: Set<Int> = emptySet(),
    var npcs: Set<Int> = emptySet(),
    var triggers: Set<String> = emptySet(),
    var variables: List<SuiteDiscVariable> = emptyList()
)


data class SuiteDiscVariable(
    @LuaNames("config_id")
    var configId: Int = 0,
    var name: String? = null,
    var value: Int = 0,
    @LuaNames("no_refresh")
    var noRefresh:Boolean = false,
)

data class SuiteDiscMonster(
    @LuaNames("config_id")
    var configId: Int = 0,
)

data class SuiteDiscGadget(
    @LuaNames("config_id")
    var configId: Int = 0,
    var state: Int = 0,
    @LuaNames("platform_info")
    var platformInfo:PlatformInfo? = null,
)

data class PlatformInfo(
    @LuaNames("point_id")
    var pointId: Int = 0,
    @LuaNames("move_type")
    var moveType: Int = 0,
    @LuaNames("route_id")
    var routeId: Int = 0,
    @LuaNames("route_index")
    var routeIndex: Int = 0,
    @LuaNames("is_started")
    var isStarted: Boolean = false,
)