package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.EntityType
import org.anime_game_servers.gi_lua.models.constants.VisionLevelType

data class SceneMonster(
    @field:LuaNames("monster_id")
    val monsterId: Int = 0,

    @field:LuaNames("pose_id")
    val poseId: Int = 0,

    @field:LuaNames("pose_logic_state")
    val poseLogicState: String? = null,


    val disableWander: Boolean = false,

    @field:LuaNames("title_id")
    val titleId: Int = 0,

    @field:LuaNames("special_name_id")
    val specialNameId: Int = 0,
    val affix: List<Int>? = null,
    val isElite: Boolean = false,

    @field:LuaNames("climate_area_id")
    val climateAreaId: Int = 0,

    @field:LuaNames("ai_config_id")
    val aiConfigId: Int = 0,

    @field:LuaNames("kill_score")
    val killScore: Int = 0,

    @field:LuaNames("speed_level")
    val speedLevel: Int = 0,
    val tag: Long = 0,

    @field:LuaNames("is_light_config")
    val isLightConfig: Boolean = false,

    @field:LuaNames("sight_group_index")
    val sightGroupIndex: Int = 0,

    override val level: Int = 0,

    @field:LuaNames("vision_level")
    override val visionLevel: VisionLevelType = VISION_LEVEL_NORMAL,

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

    ) : SceneObject(), SceneCreature {
    override val type: EntityType = MONSTER
}
