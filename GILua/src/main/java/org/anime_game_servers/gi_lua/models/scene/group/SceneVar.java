package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

@ToString
@Getter
public class SceneVar {
    @LuaNames("config_id")
    private int configId;
    private String name;
    private int value;
    @LuaNames("no_refresh")
    private boolean noRefresh;
}
