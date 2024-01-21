package org.anime_game_servers.gi_lua.models.scene;

import lombok.Data;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

import java.util.List;

@Data
public class SceneGroupReplacement {
    int id;
    @LuaNames("replace_groups")
    List<Integer> replaceGroups;
}
