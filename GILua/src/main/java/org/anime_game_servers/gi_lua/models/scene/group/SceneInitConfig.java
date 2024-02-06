package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.gi_lua.models.constants.FlowGroupSubType;
import org.anime_game_servers.gi_lua.models.constants.IOType;

@ToString
@Getter
public class SceneInitConfig {
	private int suite;
    @LuaNames("end_suite")
    private int endSuite;
    @LuaNames("io_type")
    private IOType ioType = IOType.GROUP_IO_TYPE_DEFAULT;
    @LuaNames("sub_flow_type")
    private FlowGroupSubType subFlowType = FlowGroupSubType.GROUP_SUB_FLOW_TYPE_DEFAULT;
    @LuaNames("secure_suite_index")
    private int secureSuiteIndex;
    @LuaNames("rand_suite")
    private boolean randSuite;
}
