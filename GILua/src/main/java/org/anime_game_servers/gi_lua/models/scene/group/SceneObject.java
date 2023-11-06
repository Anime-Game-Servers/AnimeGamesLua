package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.gi_lua.models.PositionImpl;
import org.anime_game_servers.gi_lua.models.SceneMeta;
import org.anime_game_servers.gi_lua.models.constants.EntityType;

@ToString
@Getter
public abstract class SceneObject {
    protected int config_id;
    protected int level;
    protected int area_id;
    protected int vision_level = 0;
    protected int mark_flag;
    protected String drop_tag;
    protected int guest_ban_drop;
    protected int oneoff_reset_version;
    protected int sight_group_index;
    // server_global_value_config, might be group only

    protected PositionImpl pos;
    protected PositionImpl rot;
    /**
     * not set by lua
     */
    protected transient int groupId;
    protected transient int blockId;
    protected transient SceneMeta sceneMeta;

    public abstract EntityType getType();
}
