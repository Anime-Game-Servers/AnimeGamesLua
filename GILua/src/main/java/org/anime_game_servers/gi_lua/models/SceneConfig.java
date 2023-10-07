package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.gi_lua.models.Position;

@ToString
@Getter
public class SceneConfig {
	private Position vision_anchor;
    private Position born_pos;
    private Position born_rot;
    private Position begin_pos;
    private Position size;
    private float die_y;
}
