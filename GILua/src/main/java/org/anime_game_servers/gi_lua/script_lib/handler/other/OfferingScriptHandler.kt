package org.anime_game_servers.gi_lua.script_lib.handler.other

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions related to the [offering](https://genshin-impact.fandom.com/wiki/Offering_System) system
 * These are only callable from a lua group context.
 */
interface OfferingScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getOfferingLevel(context: GroupEventContext, offeringId: Int): Int
}
