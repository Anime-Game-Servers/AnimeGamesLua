package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the WinterCamp activity.
 * These are only callable from a group context.
 */
interface WinterCampScriptHandler <GroupEventContext : GroupEventLuaContext> {
    fun winterCampGetBattleGroupBundleId(context: GroupEventContext): Int
    fun winterCampGetExploreGroupBundleId(context: GroupEventContext): Int
    fun winterCampSnowDriftInteract(context: GroupEventContext, cfgId: Int): Int
}