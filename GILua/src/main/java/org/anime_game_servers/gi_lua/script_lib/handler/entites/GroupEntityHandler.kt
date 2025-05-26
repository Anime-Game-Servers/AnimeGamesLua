package org.anime_game_servers.gi_lua.script_lib.handler.entites

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.models.constants.EntityType
import org.anime_game_servers.gi_lua.models.constants.GroupKillPolicy
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.script_lib.handler.parameter.KillByConfigIdParams

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


    fun killGroupEntityByCfgIds(context: GroupEventContext, groupId: Int, monsters: IntArray, gadgets: IntArray): Int
    fun killGroupEntityByPolicy(context: GroupEventContext, groupId: Int, policy: GroupKillPolicy): Int
    fun killEntityByConfigId(context: GroupEventContext, params: KillByConfigIdParams): Int
    fun removeEntityByConfigId(context: GroupEventContext, groupId: Int, entityType: EntityType, configId: Int): Int
}