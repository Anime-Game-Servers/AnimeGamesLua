package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.gi_lua.models.Position;
import org.anime_game_servers.gi_lua.models.constants.EntityType;
import org.anime_game_servers.gi_lua.models.scene.group.SceneGroup;

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

    protected Position pos;
    protected Position rot;
    /**
     * not set by lua
     */
    protected transient SceneGroup group;

    public abstract EntityType getType();
}
