package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

@ToString
@Getter
public class SceneInitConfig {
	private int suite;
    @LuaNames("end_suite")
    private int endSuite;
    @LuaNames("io_type")
    private int ioType ;
    @LuaNames("sub_flow_type")
    private int subFlowType;
    @LuaNames("secure_suite_index")
    private int secureSuiteIndex;
    @LuaNames("rand_suite")
    private boolean randSuite;
}
