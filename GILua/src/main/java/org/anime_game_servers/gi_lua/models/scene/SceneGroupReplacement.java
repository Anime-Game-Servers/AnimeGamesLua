package org.anime_game_servers.gi_lua.models.scene;

import lombok.Data;

import java.util.List;

@Data
public class SceneGroupReplacement {
    int id;
    List<Integer> replace_groups;
}
