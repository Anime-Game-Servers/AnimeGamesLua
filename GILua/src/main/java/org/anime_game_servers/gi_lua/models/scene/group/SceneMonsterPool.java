package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

@Getter
public class SceneMonsterPool {
    @LuaNames("pool_id")
    private int poolId;
    @LuaNames("rand_weight")
    private int randWeight;
}
