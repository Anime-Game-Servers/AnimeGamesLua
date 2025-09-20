package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.VisionLevelType

interface SceneCreature {
    val level: Int
    @LuaNames("vision_level")
    val visionLevel: VisionLevelType
    @LuaNames("mark_flag")
    val markFlag: Int
    @LuaNames("isOneoff")
    val isOneOff: Boolean
    @LuaNames("oneoff_reset_version")
    val oneOffResetVersion: Int
    @LuaNames("drop_tag")
    val dropTag: String?
    @LuaNames("drop_id")
    val dropId: Int
    @LuaNames("guest_ban_drop")
    val guestBanDrop: Int
    @LuaNames("server_global_value_config")
    val serverGlobalValueConfig: MutableMap<String, Float>?
}
