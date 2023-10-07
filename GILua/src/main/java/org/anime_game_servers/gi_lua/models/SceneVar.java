package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SceneVar {
    private int configId;
    private String name;
    private int value;
    private boolean no_refresh;
}
