package org.anime_game_servers.gi_lua.models.scene.block;

import lombok.Getter;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.gi_lua.models.PositionImpl;
import org.anime_game_servers.gi_lua.models.constants.GroupLoadStrategy;
import org.anime_game_servers.gi_lua.models.scene.SceneMeta;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Getter
public class SceneGroupInfo {
    private int id;
    @LuaNames("refresh_id")
    private int refreshId;
    private int area;
    @Nullable
    private PositionImpl pos;
    @Nullable @LuaNames("is_replaceable")
    private SceneReplaceable isReplaceable;
    @LuaNames("dynamic_load")
    private boolean dynamicLoad = false;
    @Nullable
    private SceneBusiness business;
    @Nullable @LuaNames("life_cycle")
    private GroupLifecycle lifeCycle = GroupLifecycle.FULL_TIME__CYCLE;
    @LuaNames("activity_revise_level_grow_id")
    private int activityReviseLevelGrowId;
    @LuaNames("rely_start_world_level_limit_activity_id")
    private int relyStartWorldLevelLimitActivityId; // SceneScriptConfig LuaConfigMgr
    /**
     * seems to be linked to SceneRegion.visionTypeList
     */
    @LuaNames("vision_type")
    private int visionType;
    @LuaNames("across_block")
    private boolean acrossBlock = false;
    @LuaNames("unload_when_disconnect")
    private boolean unloadWhenDisconnect = false;
    @LuaNames("ignore_world_level_revise")
    private boolean ignoreWorldLevelRevise = false;
    @LuaNames("force_unload_nodelay")
    private boolean forceUnloadNodelay = false;
    @LuaNames("force_clean_sub_entity")
    private boolean forceCleanSubEntity = false;
    @LuaNames("is_load_by_vision_type")
    private boolean isLoadByVisionType = false;
    @LuaNames("load_strategy")
    private GroupLoadStrategy loadStrategy;
    @LuaNames("forbid_monster_die")
    private Set<String> forbidMonsterDie; //todo find enum values
    @LuaNames("related_level_tag_series_list")
    private List<Integer> relatedLevelTagSeriesList;
    @LuaNames("group_tag_list")
    private List<Integer> groupTagList;
    private List<Integer> weathers;

    // internal variables
    transient int blockId;
    transient int activityId;
    transient SceneMeta sceneMeta;

    public int getBusinessType() {
        return this.business == null ? 0 : this.business.getType();
    }

    public boolean isReplaceable() {
        return this.isReplaceable != null && this.isReplaceable.isValue();
    }
}
