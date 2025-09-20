package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.FlowGroupSubType
import org.anime_game_servers.gi_lua.models.constants.IOType

data class SceneInitConfig (
    val suite: Int = 0,

    @field:LuaNames("end_suite")
    val endSuite: Int = 0,

    @field:LuaNames("io_type")
    val ioType: IOType = GROUP_IO_TYPE_DEFAULT,

    @field:LuaNames("sub_flow_type")
    val subFlowType: FlowGroupSubType = GROUP_SUB_FLOW_TYPE_DEFAULT,

    @field:LuaNames("secure_suite_index")
    val secureSuiteIndex: Int = 0,

    @field:LuaNames("rand_suite")
    val randSuite: Boolean = false,
)
