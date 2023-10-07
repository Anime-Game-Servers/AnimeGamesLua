package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;

import java.util.Set;

@Getter
public class WorktopConfig {
    private Set<Integer> init_options;
    private boolean is_persistent = false;
}
