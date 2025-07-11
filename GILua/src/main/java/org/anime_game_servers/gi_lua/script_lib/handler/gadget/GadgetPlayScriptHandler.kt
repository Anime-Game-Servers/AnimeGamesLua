package org.anime_game_servers.gi_lua.script_lib.handler.gadget

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

/**
 * This handles script lib functions that are specific to the Gadget play system in groups.
 * These are only callable from a group context.
 */
interface GadgetPlayScriptHandler<GroupEventContext : GroupEventLuaContext> {

    fun startGadgetPlay(context: GroupEventContext, groupId: Int, configId: Int): Int

    fun gadgetPlayUidOp(
        context: GroupEventContext,
        groupId: Int,
        gadgetCrucibleCfgId: Int,
        uidList: List<Int>,
        op: Int,
        paramString: String,
        paramList: List<Int>,
    ): Int
}