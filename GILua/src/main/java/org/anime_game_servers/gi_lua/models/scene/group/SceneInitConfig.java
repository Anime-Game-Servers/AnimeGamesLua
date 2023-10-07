package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SceneInitConfig {
	private int suite;
    private int end_suite;
    private int io_type ;
    private int sub_flow_type;
    private int secure_suite_index;
    private boolean rand_suite;
}
