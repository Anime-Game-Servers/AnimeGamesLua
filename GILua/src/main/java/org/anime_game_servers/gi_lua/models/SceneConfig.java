package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SceneConfig {
    private PositionImpl vision_anchor;
    private PositionImpl born_pos;
    private PositionImpl born_rot;
    private PositionImpl begin_pos;
    private PositionImpl size;
    private float die_y;
}
