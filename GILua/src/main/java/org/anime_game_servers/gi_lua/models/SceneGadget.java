package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.gi_lua.models.constants.EntityType;

import java.util.List;

@ToString
@Getter
public class SceneGadget extends SceneObject{
    private int gadget_id;
    private int state;
    private int point_type;
    private SceneBossChest boss_chest;
    private int chest_drop_id;
    private int interact_id;
    private boolean isOneoff;
    private int draft_id;
    private int route_id;
    private boolean start_route = true;
    private boolean is_use_point_array  = false;
    private boolean persistent = false;
    private boolean showcutscene;
    private Explore explore;
    private List<Integer> arguments;

    @Override
    public EntityType getType() {
        return EntityType.GADGET;
    }
}
