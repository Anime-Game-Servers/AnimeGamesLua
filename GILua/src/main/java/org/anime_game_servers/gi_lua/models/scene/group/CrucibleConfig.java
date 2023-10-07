package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;

import java.util.List;

@Getter
public class CrucibleConfig {
    private int duration;
    private int start_cd;
    private int mp_play_id;
    private List<Integer> mp_play_id_list;
}
