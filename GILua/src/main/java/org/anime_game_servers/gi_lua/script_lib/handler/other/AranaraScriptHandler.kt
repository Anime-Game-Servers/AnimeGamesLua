package org.anime_game_servers.gi_lua.script_lib.handler.other

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions connected to the aranara collections.
 * These are only callable from a lua group context.
 */
interface AranaraScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getAranaraCollectableCountByTypeAndState(context: GroupEventContext, type: Int, state: Int): Int
    fun receiveAllAranaraCollectionByType(context: GroupEventContext, groupId: Int, type: Int): Int
}
