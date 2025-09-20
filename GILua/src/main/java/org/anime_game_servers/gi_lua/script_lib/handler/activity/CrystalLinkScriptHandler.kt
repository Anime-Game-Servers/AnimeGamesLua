package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

data class CrystalLinkTeamSetupParams(
    @field:LuaNames("init_gallery_progress")
    val initGalleryProgress: Int = 0,
){
    companion object {
        fun fromLuaTable(table: LuaTable): CrystalLinkTeamSetupParams? {
            val initGalleryProgress = table.optInt("init_gallery_progress", -1)
            if (initGalleryProgress == -1) {
                return null // Invalid or missing data
            }
            return CrystalLinkTeamSetupParams(
                initGalleryProgress = initGalleryProgress
            )
        }
    }
}

/**
 * This handles script lib functions that are specific to the Crystal Link activity.
 * These are only callable from a group context.
 */
interface CrystalLinkScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun crystalLinkDungeonTeamSetUp(context: GroupEventContext, configId: Int, setupParams: CrystalLinkTeamSetupParams): Int
}