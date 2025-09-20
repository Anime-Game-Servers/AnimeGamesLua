package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.EntityType
import org.anime_game_servers.gi_lua.models.constants.VisionLevelType

data class SceneGadget(
    @field:LuaNames("gadget_id")
    val gadgetId: Int = 0,
    val state: Int = 0,

    @field:LuaNames("point_type")
    val pointType: Int = 0,

    @field:LuaNames("boss_chest")
    val bossChest: SceneBossChest? = null,


    @field:LuaNames("chest_drop_id")
    val chestDropId: Int = 0,

    @field:LuaNames("interact_id")
    val interactId: Int = 0,

    @field:LuaNames("draft_id")
    val draftId: Int = 0,

    @field:LuaNames("route_id")
    val routeId: Int = 0,

    @field:LuaNames("start_route")
    val startRoute: Boolean = true,

    @field:LuaNames("is_use_point_array")
    val isUsePointArray: Boolean = false,
    val persistent: Boolean = false,

    @field:LuaNames("showcutscene")
    val showCutscene: Boolean = false,
    val owner: Int = 0, // cfg id
    val autopick: Boolean = false,
    val explore: Explore? = null,
    val arguments: MutableList<Int?>? = null,

    @field:LuaNames("is_guest_can_operate")
    val isGuestCanOperate: Boolean = false,

    @field:LuaNames("is_blossom_chest")
    val isBlossomChest: Boolean = false,

    @field:LuaNames("is_enable_interact")
    val isEnableInteract: Boolean = true,

    @field:LuaNames("talk_state")
    val talkState: Int = 0,

    @field:LuaNames("fishing_id")
    val fishingId: Int = 0,

    @field:LuaNames("fishing_areas")
    val fishingAreas: MutableList<Int?>? = null,

    @field:LuaNames("crucible_config")
    val crucibleConfig: CrucibleConfig? = null,

    @field:LuaNames("offering_config")
    val offeringConfig: OfferingConfig? = null,

    @field:LuaNames("worktop_config")
    val worktopConfig: WorktopConfig? = null,


    override val level: Int = 0,
    @field:LuaNames("vision_level")
    override val visionLevel: VisionLevelType = VisionLevelType.VISION_LEVEL_NORMAL,
    @field:LuaNames("mark_flag")
    override val markFlag: Int = 0,
    @field:LuaNames("isOneoff")
    override val isOneOff: Boolean = false,
    @field:LuaNames("oneoff_reset_version")
    override val oneOffResetVersion: Int = 0,
    @field:LuaNames("drop_tag")
    override val dropTag: String? = null,
    @field:LuaNames("drop_id")
    override val dropId: Int = 0,

    @field:LuaNames("guest_ban_drop")
    override val guestBanDrop: Int = 0,
    @field:LuaNames("server_global_value_config")
    override val serverGlobalValueConfig: MutableMap<String, Float>? = null,
) : SceneObject(), SceneCreature // todo add parent fields to constructor
{
    override val type: EntityType = GADGET
}
