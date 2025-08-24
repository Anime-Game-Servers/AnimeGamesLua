package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext



/**
 * Handler for scriptlib functions with the single and multistage scene play.
 * These are only callable from a lua group context.
 */
interface SceneGadgetChainScriptHandler<GroupEventContext : GroupEventLuaContext> {
    // GadgetChainExcelConfigData
    fun getChainLevel(context: GroupEventContext, uid: Int, chainId: Int): Int
    fun setChainLevel(context: GroupEventContext, chainId: Int, level: Int, isNotify: Boolean): Int
}
