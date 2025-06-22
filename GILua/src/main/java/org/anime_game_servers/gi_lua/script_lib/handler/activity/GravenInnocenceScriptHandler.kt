package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Graven Innocence activity.
 * These are only callable from a group context.
 */
interface GravenInnocenceScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun invalidGravenPhotoBundleMark(context: GroupEventContext, groupBundleId: Int): Int
}