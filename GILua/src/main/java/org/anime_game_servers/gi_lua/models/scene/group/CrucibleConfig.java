package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

import java.util.List;

@Getter
public class CrucibleConfig {
    private int duration;
    @LuaNames("start_cd")
    private int startCd;
    @LuaNames("mp_play_id")
    private int mpPlayId;
    @LuaNames("mp_play_id_list")
    private List<Integer> mpPlayIdList;
}
