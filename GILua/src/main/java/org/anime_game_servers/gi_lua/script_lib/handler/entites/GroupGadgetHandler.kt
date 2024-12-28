package org.anime_game_servers.gi_lua.script_lib.handler.entites

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

/**
 * Handler for scriptlib functions used in GroupScripts related to Gadgets.
 * These are only callable from a lua group context.
 */
interface GroupGadgetHandler<GroupEventContext : GroupEventLuaContext> {

    fun createGadget(context: GroupEventContext, table: LuaTable?): Int

    /**
     * Spawn a gadget from the caller group at the specified position
     * @param configId The config id of the gadget in the calling group
     * @param pos The position to spawn the gadget at
     * @param rot The rotation of the gadget when spawned
     */
    fun createGadgetByConfigIdByPos(context: GroupEventContext, configId: Int, pos: Vector?, rot: Vector?): Int


    /**
     * TODO preparsed parameters
     * Spawns a gadget based on the caller groups gadget with cfg id matching the specified id. It also applies additional parameters based on the parameters
     * @param creationParams parameters to spawn a gadget with
     */
    fun createGadgetByParamTable(context: GroupEventContext, creationParams: LuaTable?): Int

    /**
     * Returns the state of a gadget based on the group id and config id
     * @param context The context of the group event
     * @param groupId group to search for the gadget in, 0 for the caller group.
     * @param configId config id of the gadget in the group.
     */
    fun getGadgetStateByConfigId(context: GroupEventContext, groupId: Int, configId: Int): Int

    /**
     * Returns the hp in percent of a gadget based on the group id and config id
     * @param context The context of the group event
     * @param groupId group to search for the gadget entity in, 0 for the caller group.
     * @param configId config id of the gadget in the group.
     */
    fun getGadgetHpPercent(context: GroupEventContext, groupId: Int, configId: Int): Int


    /**
     * Retrieves and returns the gadget id of a gadget entity based on the entity id.
     * @param context The context of the group event
     * @param entityId The entity id of the gadget requested.
     */
    fun getGadgetIdByEntityId(context: GroupEventContext, entityId: Int): Int


    /**
     * Returns the config id of the gadget with the eid (gadget_eid)
     * @param context The context of the group event
     * @param gadgetEid The entity id of the gadget requested. Table with `gadget_eid` in lua.
     */
    fun getGadgetConfigId(context: GroupEventContext, gadgetEid: Int): Int

    /**
     * Change the state of a gadget in the defined group
     * @param context The context of the group event
     * @param groupId The group containing the target gadget or the caller group if 0
     * @param configId config id of a gadget in the target group
     * @param gadgetState target state for the gadget
     */
    fun setGroupGadgetStateByConfigId(context: GroupEventContext, groupId: Int, configId: Int, gadgetState: Int): Int

    /**
     * Change the state of a gadget in the current group
     * @param context The context of the group event
     * @param configId config id of a gadget in the current caller group
     * @param gadgetState target state for the gadget
     */
    fun setGadgetStateByConfigId(context: GroupEventContext, configId: Int, gadgetState: Int): Int

    /**
     * Change the state of a gadget in the current group, based in the parametersTable
     * @param context The context of the group event
     * @param configId config id of a gadget in the current caller group
     * @param gadgetState target state for the gadget
     */
    fun changeGroupGadget(context: GroupEventContext, configId: Int, gadgetState: Int): Int

    /**
     * Sets a gadget interacteable based on the config id and group id.
     */
    fun setGadgetEnableInteract(context: GroupEventContext, groupId: Int, configId: Int, enable: Boolean): Int

    fun setGadgetTalkByConfigId(context: GroupEventContext, groupId: Int, configId: Int, talkId: Int): Int

    /* Stats */
    fun setGadgetHp(context: GroupEventContext, groupId: Int, configId: Int, hpPercent: Int): Int

    /* Worktop */
    fun setWorktopOptionsByGroupId(context: GroupEventContext, groupId: Int, configId: Int, options: LuaTable?): Int
    fun setWorktopOptions(context: GroupEventContext, table: LuaTable?): Int
    fun delWorktopOptionByGroupId(context: GroupEventContext, groupId: Int, configId: Int, option: Int): Int
    fun delWorktopOption(context: GroupEventContext, var1: Int): Int

    /* Lua */
    /**
     * // TODO identify unknown parameters and exact behaviour
     * Executes a lua function on a gadgets lua controller.
     * This seems to be used in only the Crucible activity
     * @param groupId group to find the gadget in
     * @param gadgetCfgId cfg id of the gadget in the group to execute lua in
     * @param activityType seems to be an activity type
     * @param var4 TODO
     * @param val5 TODO
     */
    fun executeGadgetLua(
        context: GroupEventContext, groupId: Int, gadgetCfgId: Int,
        activityType: Int, var4: Int, val5: Int
    ): Int

}