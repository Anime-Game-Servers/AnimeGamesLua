package org.anime_game_servers.gi_lua.models.scene;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.gi_lua.models.PositionImpl;

@ToString
@Getter
public class SceneConfig {
    @LuaNames("vision_anchor")
    private PositionImpl visionAnchor;
    @LuaNames("born_pos")
    private PositionImpl bornPos;
    @LuaNames("born_rot")
    private PositionImpl bornRot;
    @LuaNames("begin_pos")
    private PositionImpl beginPos;
    private PositionImpl size;
    @LuaNames("die_y")
    private float dieY;
}
