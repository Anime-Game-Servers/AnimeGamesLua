package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.gi_lua.models.constants.EntityType;

import java.util.List;

@ToString
@Getter
public class SceneMonster extends SceneObject{
	private int monster_id;
    private int pose_id;
    private String pose_logic_state;
    private int drop_id;
    private boolean disableWander;
    private int title_id;
    private int special_name_id;
    private List<Integer> affix;
    private boolean isElite;
    private int climate_area_id;
    private int ai_config_id;
    private int kill_score;
    private int speed_level;
    private long tag;
    private boolean is_light_config;

    @Override
    public EntityType getType() {
        return EntityType.MONSTER;
    }
}
