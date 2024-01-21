package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.gi_lua.models.constants.EntityType;

import java.util.List;
import java.util.Map;

@ToString
@Getter
public class SceneMonster extends SceneObject{
    @LuaNames("monster_id")
	private int monsterId;
    @LuaNames("pose_id")
    private int poseId;
    @LuaNames("pose_logic_state")
    private String poseLogicState;
    @LuaNames("isOneoff")
    private boolean isOneOff = false;
    @LuaNames("drop_id")
    private int dropId;
    private boolean disableWander = false;
    @LuaNames("title_id")
    private int titleId;
    @LuaNames("special_name_id")
    private int specialNameId;
    private List<Integer> affix;
    private boolean isElite = false;
    @LuaNames("climate_area_id")
    private int climateAreaId;
    @LuaNames("ai_config_id")
    private int aiConfigId;
    @LuaNames("kill_score")
    private int killScore;
    @LuaNames("speed_level")
    private int speedLevel;
    private long tag;
    @LuaNames("is_light_config")
    private boolean isLightConfig = false;
    @LuaNames("oneoff_reset_version")
    private int oneOffResetVersion;
    @LuaNames("sight_group_index")
    private int sightGroupIndex;
    @LuaNames("server_global_value_config")
    private Map<String, Float> serverGlobalValueConfig;

    @Override
    public EntityType getType() {
        return EntityType.MONSTER;
    }
}
