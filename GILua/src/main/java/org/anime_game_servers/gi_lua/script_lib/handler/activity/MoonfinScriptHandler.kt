package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the Moonfin/[Lunar Real](https://genshin-impact.fandom.com/wiki/Lunar_Realm)
 * fishing activity.
 * These are only callable from a group context.
 */
interface MoonfinScriptHandler<GroupEventContext : GroupEventLuaContext> {
    // cleans up the fish pool gadget and player abilities and sends ExitFishingRsp
    fun stopFishing(context: GroupEventContext, uid: Int): Int
}