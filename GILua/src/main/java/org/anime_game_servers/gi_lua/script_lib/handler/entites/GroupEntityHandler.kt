package org.anime_game_servers.gi_lua.script_lib.handler.entites

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions used in GroupScripts related to Gadgets.
 * These are only callable from a lua group context.
 */
interface GroupEntityHandler<GroupEventContext : GroupEventLuaContext> {

    fun delAllSubEntityByOriginOwnerConfigId(context: GroupEventContext, configId: Int): Int
    fun getEntityIdByConfigId(context: GroupEventContext, configId: Int): Int

    fun getTeamEntityIdByUid(context: GroupEventContext, uid: Int): Int
    fun getAvatarEntityIdByUid(context: GroupEventContext, uid: Int): Int

    fun getConfigIdByEntityId(context: GroupEventContext, entityID: Int): Int
    fun getTeamUidByEntityId(context: GroupEventContext, entityID: Int): Int
    fun getUidByTeamEntityId(context: GroupEventContext, entityID: Int): Int

    fun getPosByEntityId(context: GroupEventContext, entityId: Int): Vector?

    fun getRotationByEntityId(context: GroupEventContext, entityId: Int): Vector?
}