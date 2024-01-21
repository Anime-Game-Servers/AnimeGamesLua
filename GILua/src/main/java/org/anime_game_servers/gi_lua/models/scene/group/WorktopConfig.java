package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

import java.util.Set;

@Getter
public class WorktopConfig {
    @LuaNames("init_options")
    private Set<Integer> initOptions;
    @LuaNames("is_persistent")
    private boolean isPersistent = false;
}
