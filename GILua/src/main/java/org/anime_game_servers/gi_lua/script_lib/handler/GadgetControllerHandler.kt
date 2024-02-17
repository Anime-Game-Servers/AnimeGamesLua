package org.anime_game_servers.gi_lua.script_lib.handler

import org.anime_game_servers.gi_lua.script_lib.ControllerLuaContext
import org.anime_game_servers.gi_lua.script_lib.handler.parameter.KillByConfigIdParams
import org.anime_game_servers.lua.engine.LuaTable

/**
 * Handler for scriptlib functions used in EntityControllers, which use the ControllerEventContext.
 * These are only callable from a gadget controller context.
 */
interface GadgetControllerHandler<GadgetEntity, ControllerEventContext : ControllerLuaContext<GadgetEntity>> {
    /**
     * Changes the state of the gadget that called the controller.
     */
    fun SetGadgetState(context: ControllerEventContext, gadgetState: Int): Int

    /**
     * Returns the current state of the gadget that called the controller.
     */
    fun GetGadgetState(context: ControllerEventContext): Int

    /**
     * Return the arguments passed to the calling gadget in the script definition.
     */
    fun GetGadgetArguments(context: ControllerEventContext):IntArray?

    /**
     * Resets the gadgets state to its state when born state.
     */
    fun ResetGadgetState(context: ControllerEventContext, gadgetState: Int): Int

    fun SetGearStartValue(context: ControllerEventContext, startValue: Int): Int

    fun GetGearStartValue(context: ControllerEventContext): Int

    fun SetGearStopValue(context: ControllerEventContext, startValue: Int): Int

    fun GetGearStopValue(context: ControllerEventContext): Int

    fun GetGadgetStateBeginTime(context: ControllerEventContext): Int

    /**
     * Returns the config id of the gadget that called the controller.
     */
    fun GetContextGadgetConfigId(context: ControllerEventContext): Int

    /**
     * Returns the group id of the gadget that called the controller.
     */
    fun GetContextGroupId(context: ControllerEventContext): Int

    /**
     * Sets a gadget interacteable based on the config id and group id.
     */
    fun SetGadgetEnableInteract(context: ControllerEventContext, groupId: Int, configId: Int, enable: Boolean): Int

    fun DropSubfield(context: ControllerEventContext, paramsTable: LuaTable?): Int

    fun GetGatherConfigIdList(context: ControllerEventContext): IntArray?


    fun KillEntityByConfigId(context: ControllerEventContext, params: KillByConfigIdParams): Int
}