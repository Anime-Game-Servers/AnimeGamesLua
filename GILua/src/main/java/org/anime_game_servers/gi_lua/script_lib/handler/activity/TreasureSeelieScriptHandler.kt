package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the TreasureSeelie activity.
 * These are only callable from a group context.
 */
interface TreasureSeelieScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun treasureSeelieCollectOrbsNotify(context: GroupEventContext, lightLevel: Int, var2: Int): Int
    fun getTreasureSeelieDayByGroupId(context: GroupEventContext, groupId: Int): Int
}