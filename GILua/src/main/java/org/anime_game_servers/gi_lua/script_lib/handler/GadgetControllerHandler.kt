package org.anime_game_servers.gi_lua.script_lib.handler

import org.anime_game_servers.gi_lua.script_lib.ControllerLuaContext
import org.anime_game_servers.gi_lua.script_lib.handler.parameter.KillByConfigIdParams

/**
 * Handler for scriptlib functions used in EntityControllers, which use the ControllerEventContext.
 * These are only callable from a gadget controller context.
 */
interface GadgetControllerHandler<GadgetEntity, ControllerEventContext : ControllerLuaContext<GadgetEntity>> {
    /**
     * Changes the state of the gadget that called the controller.
     */
    fun setGadgetState(context: ControllerEventContext, gadgetState: Int): Int

    /**
     * Change the state of a gadget in the contexts current group
     * @param context A Gadget controller context
     * @param configId config id of a gadget in the current caller group
     * @param gadgetState target state for the gadget
     */
    fun setGadgetStateByConfigId(context: ControllerEventContext, configId: Int, gadgetState: Int): Int

    /**
     * Returns the current state of the gadget that called the controller.
     */
    fun getGadgetState(context: ControllerEventContext): Int

    /**
     * Return the arguments passed to the calling gadget in the script definition.
     */
    fun getGadgetArguments(context: ControllerEventContext):List<Int>

    /**
     * Resets the gadgets state to its state when born state.
     */
    fun resetGadgetState(context: ControllerEventContext, gadgetState: Int): Int

    fun setGearStartValue(context: ControllerEventContext, startValue: Int): Int

    fun getGearStartValue(context: ControllerEventContext): Int

    fun setGearStopValue(context: ControllerEventContext, startValue: Int): Int

    fun getGearStopValue(context: ControllerEventContext): Int

    fun getGadgetStateBeginTime(context: ControllerEventContext): Int

    /**
     * Returns the config id of the gadget that called the controller.
     */
    fun getContextGadgetConfigId(context: ControllerEventContext): Int

    /**
     * Returns the group id of the gadget that called the controller.
     */
    fun getContextGroupId(context: ControllerEventContext): Int

    /**
     * Sets a gadget interacteable based on the config id and group id.
     */
    fun setGadgetEnableInteract(context: ControllerEventContext, groupId: Int, configId: Int, enable: Boolean): Int

    fun dropSubfield(context: ControllerEventContext, subfieldName: String): Int

    fun getGatherConfigIdList(context: ControllerEventContext): List<Int>


    fun killEntityByConfigId(context: ControllerEventContext, params: KillByConfigIdParams): Int

    fun getContextGadgetEntityId(context: ControllerEventContext): Int

    /* used in OnPlayStageChange and OnClientExecuteReq */
    fun gadgetLuaNotifyGroup(context: ControllerEventContext, var1: Int, var2: Int, var3: Int): Int
}