package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Potion activity.
 * These are only callable from a group context.
 */
interface PotionScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getPotionDungeonAffixParams(context: GroupEventContext): List<Int>?
}