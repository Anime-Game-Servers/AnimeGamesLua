package org.anime_game_servers.gi_lua.models.scene.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupLifecycle {
    public static final GroupLifecycle FULL_TIME__CYCLE = new GroupLifecycle(0, 24);
    private int from;
    private int to;
}
