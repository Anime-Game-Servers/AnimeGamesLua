package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.gi_lua.models.constants.EntityType;

import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
@ToString
@Getter
public class SceneGadget extends SceneObject {
    @LuaNames("gadget_id")
    private int gadgetId;
    private int state;
    @LuaNames("point_type")
    private int pointType;
    @LuaNames("boss_chest")
    private SceneBossChest bossChest;
    @LuaNames("drop_id")
    private int dropId;
    @LuaNames("chest_drop_id")
    private int chestDropId;
    @LuaNames("interact_id")
    private int interactId;
    @LuaNames("isOneoff")
    private boolean isOneOff = false;
    @LuaNames("draft_id")
    private int draftId;
    @LuaNames("route_id")
    private int routeId;
    @LuaNames("start_route")
    private boolean startRoute = true;
    @LuaNames("is_use_point_array")
    private boolean isUsePointArray  = false;
    private boolean persistent = false;
    @LuaNames("showcutscene")
    private boolean showCutscene = false;
    private int owner; // cfg id
    private boolean autopick = false;
    private Explore explore;
    private List<Integer> arguments;
    @LuaNames("oneoff_reset_version")
    private int oneOffResetVersion;
    @LuaNames("is_guest_can_operate")
    private boolean isGuestCanOperate = false;
    @LuaNames("is_blossom_chest")
    private boolean isBlossomChest = false;
    @LuaNames("is_enable_interact")
    private boolean isEnableInteract = true;
    @LuaNames("talk_state")
    private int talkState;
    @LuaNames("fishing_id")
    private int fishingId;
    @LuaNames("fishing_areas")
    private List<Integer> fishingAreas;
    @LuaNames("crucible_config")
    private CrucibleConfig crucibleConfig;
    @LuaNames("offering_config")
    private OfferingConfig offeringConfig;
    @LuaNames("worktop_config")
    private WorktopConfig worktopConfig;
    @LuaNames("server_global_value_config")
    private Map<String, Float> serverGlobalValueConfig;

    @Override
    public EntityType getType() {
        return EntityType.GADGET;
    }
}
