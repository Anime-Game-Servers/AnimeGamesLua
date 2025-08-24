package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions used for the [Hunting/Bounty](https://genshin-impact.fandom.com/wiki/Bounty) system.
 * These are only callable from a lua group context.
 */
interface HuntingScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun refreshHuntingClueGroup(context: GroupEventContext): Int
    fun getHuntingMonsterExtraSuiteIndexVec(context: GroupEventContext): Int
}
