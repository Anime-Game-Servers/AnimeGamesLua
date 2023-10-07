package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.gi_lua.models.constants.EntityType;

import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
@ToString
@Getter
public class SceneGadget extends SceneObject {
    private int gadget_id;
    private int state;
    private int point_type;
    private SceneBossChest boss_chest;
    private int drop_id;
    private int chest_drop_id;
    private int interact_id;
    private boolean isOneoff = false;
    private int draft_id;
    private int route_id;
    private boolean start_route = true;
    private boolean is_use_point_array  = false;
    private boolean persistent = false;
    private boolean showcutscene = false;
    private int owner; // cfg id
    private boolean autopick = false;
    private Explore explore;
    private List<Integer> arguments;
    private int oneoff_reset_version;
    private boolean is_guest_can_operate = false;
    private boolean is_blossom_chest = false;
    private boolean is_enable_interact = true;
    private int talk_state;
    private int fishing_id;
    private List<Integer> fishing_areas;
    private CrucibleConfig crucible_config;
    private OfferingConfig offering_config;
    private WorktopConfig worktop_config;
    private Map<String, Float> server_global_value_config;

    @Override
    public EntityType getType() {
        return EntityType.GADGET;
    }
}
