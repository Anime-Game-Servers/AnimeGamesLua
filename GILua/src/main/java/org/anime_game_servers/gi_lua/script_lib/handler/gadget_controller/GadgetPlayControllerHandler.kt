package org.anime_game_servers.gi_lua.script_lib.handler.gadget_controller

import org.anime_game_servers.gi_lua.script_lib.ControllerLuaContext

/**
 * This handles script lib functions that are specific to the Gadget play system in the gadget controller context.
 * These are only callable from a group context.
 */
interface GadgetPlayControllerHandler<GadgetEntity, ControllerEventContext : ControllerLuaContext<GadgetEntity>> {
    fun gadgetPlayUidOp(
        context: ControllerEventContext,
        groupId: Int,
        gadgetCrucibleCfgId: Int,
        uidList: List<Int>,
        op: Int,
        paramString: String,
        paramList: List<Int>,
    ): Int

    fun getGadgetPlayProgress(context: ControllerEventContext, groupId: Int, configId: Int): Int
    fun getGadgetPlayStageBeginProgress(context: ControllerEventContext, groupId: Int, configId: Int): Int
    fun getGadgetPlayUidValue(context: ControllerEventContext, groupId: Int, configId: Int, uid: Int, name: String): Int
    fun setGadgetPlayUidValue(
        context: ControllerEventContext,
        groupId: Int,
        configId: Int,
        uid: Int,
        key: String,
        value: Int
    ): Int

    fun addGadgetPlayProgress(context: ControllerEventContext, groupId: Int, configId: Int, progressChange: Int): Int
}