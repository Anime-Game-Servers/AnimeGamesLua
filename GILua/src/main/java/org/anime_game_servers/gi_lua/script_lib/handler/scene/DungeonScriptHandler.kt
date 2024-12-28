package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions used in
 * These are only callable from a lua group context.
 */
interface DungeonScriptHandler<GroupEventContext : GroupEventLuaContext> {

    fun causeDungeonFail(context: GroupEventContext): Int
    fun causeDungeonSuccess(context: GroupEventContext): Int

    fun enterPersistentDungeon(
        context: GroupEventContext,
        dungeonId: Int,
        var2: Int,
        position: Vector,
        rot: Vector
    ): Int

    /**
     * Activates a dungeon checkpoint.
     * @param context a group event lua context
     * @param pointId the scene point id of the dungeon checkpoint
     * @return 0 if successful, 1 if dungeon manager is null, 2 if dungeon manager failed to activate the checkpoint
     */
    fun activateDungeonCheckPoint(context: GroupEventContext, pointId: Int): Int


    fun getDungeonTeamPlayerNum(context: GroupEventContext): Int
    fun getDungeonTransaction(context: GroupEventContext): Int
    fun getOpeningDungeonListByRosterId(context: GroupEventContext, rosterId: Int): List<Int>
}