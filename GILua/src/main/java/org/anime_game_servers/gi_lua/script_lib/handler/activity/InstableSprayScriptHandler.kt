package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Instable Spray activity.
 * These are only callable from a group context.
 */
interface InstableSprayScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun instableSprayGetSGVByBuffId(context: GroupEventContext, buffId: Int): String
    fun instableSprayRandomBuffs(context: GroupEventContext, galleryId: Int, stage: Int): List<Int>
}