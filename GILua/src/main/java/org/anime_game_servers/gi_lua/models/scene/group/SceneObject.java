package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.gi_lua.models.PositionImpl;
import org.anime_game_servers.gi_lua.models.scene.SceneMeta;
import org.anime_game_servers.gi_lua.models.constants.EntityType;

@ToString
@Getter
public abstract class SceneObject {
    @LuaNames("config_id")
    protected int configId;
    protected int level;
    @LuaNames("area_id")
    protected int areaId;
    @LuaNames("vision_level")
    protected int visionLevel = 0;
    @LuaNames("mark_flag")
    protected int markFlag;
    @LuaNames("drop_tag")
    protected String dropTag;
    @LuaNames("guest_ban_drop")
    protected int guestBanDrop;
    @LuaNames("oneoff_reset_version")
    protected int oneOffResetVersion;
    @LuaNames("sight_group_index")
    protected int sightGroupIndex;

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
