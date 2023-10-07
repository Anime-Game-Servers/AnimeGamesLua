package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SceneInitConfig {
	private int suite;
    private int end_suite;
    private int io_type ;
    private boolean rand_suite;
}
