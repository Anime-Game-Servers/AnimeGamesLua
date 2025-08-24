package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions related to [RandomEvents](https://genshin-impact.fandom.com/wiki/Random_Event)
 * These are only callable from a lua group context.
 */
interface RandTaskScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun finishRandTask(context: GroupEventContext, optionId: Int, isSucc: Boolean): Int
}
