package org.anime_game_servers.gi_lua.script_lib.handler.other

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions for general regional play functions.
 * Regional play includes things like death zones
 * These are only callable from a lua group context.
 */
interface RegionalPlayScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getRegionalPlayVarValue(context: GroupEventContext, uid: Int, type: Int): Int
    fun addRegionalPlayVarValue(context: GroupEventContext, uid: Int, regionId: Int, delta: Int): Int
}
