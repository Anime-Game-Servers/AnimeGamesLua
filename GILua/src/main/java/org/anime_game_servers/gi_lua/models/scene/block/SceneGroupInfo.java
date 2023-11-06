package org.anime_game_servers.gi_lua.models.scene.block;

import lombok.Getter;
import org.anime_game_servers.gi_lua.models.PositionImpl;
import org.anime_game_servers.gi_lua.models.scene.SceneMeta;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Getter
public class SceneGroupInfo {
    private int id;
    private int refresh_id;
    private int area;
    @Nullable
    private PositionImpl pos;
    @Nullable
    private SceneReplaceable is_replaceable;
    private final boolean dynamic_load = false;
    @Nullable
    private SceneBusiness business;
    @Nullable
    private GroupLifecycle life_cycle = GroupLifecycle.FULL_TIME__CYCLE;
    private int activity_revise_level_grow_id;
    private int rely_start_world_level_limit_activity_id; // SceneScriptConfig LuaConfigMgr
    private int vision_type;
    private boolean across_block = false;
    private boolean unload_when_disconnect = false;
    private boolean ignore_world_level_revise = false;
    private boolean force_unload_nodelay = false;
    private boolean force_clean_sub_entity = false;
    private boolean is_load_by_vision_type = false;
    private int load_strategy;
    private Set<String> forbid_monster_die; //todo find enum values
    private List<Integer> related_level_tag_series_list;
    private List<Integer> group_tag_list;
    private List<Integer> weathers;

    // internal variables
    transient int blockId;
    transient int activityId;
    transient SceneMeta sceneMeta;

    public int getBusinessType() {
        return this.business == null ? 0 : this.business.getType();
    }

    public boolean isReplaceable() {
        return this.is_replaceable != null && this.is_replaceable.isValue();
    }
}
