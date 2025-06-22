package org.anime_game_servers.gi_lua.script_lib.handler.entites

import org.anime_game_servers.gi_lua.models.constants.EntityType
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions used in GroupScripts related to Gadgets.
 * These are only callable from a lua group context.
 */
interface GroupRegionScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getRegionEntityCount(context: GroupEventContext, regionEId: Int, entityType: EntityType): Int
    fun getRegionConfigId(context: GroupEventContext, regionEId: Int): Int
    fun isInRegion(context: GroupEventContext, uid: Int, regionId: Int): Boolean
}