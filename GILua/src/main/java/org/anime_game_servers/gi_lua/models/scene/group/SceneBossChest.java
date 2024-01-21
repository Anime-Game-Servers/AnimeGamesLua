package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

@Getter
@ToString
public class SceneBossChest {
    @LuaNames("life_time")
    private int lifeTime;
    @LuaNames("monster_config_id")
    private int monsterConfigId;
    private int resin;
    @LuaNames("take_num")
    private int takeNum;
}
