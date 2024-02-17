package org.anime_game_servers.gi_lua.script_lib.handler.entites

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * Handler for scriptlib functions used in GroupScripts related to Gadgets.
 * These are only callable from a gadget controller context.
 */
interface GroupGadgetHandler<GroupEventContext : GroupEventLuaContext> {


    /**
     * Returns the state of a gadget based on the group id and config id
     * @param context The context of the group event
     * @param groupId group to search for the gadget in, 0 for the caller group.
     * @param configId config id of the gadget in the group.
     */
    fun GetGadgetStateByConfigId(context: GroupEventContext, groupId: Int, configId: Int): Int

    /**
     * Returns the hp in percent of a gadget based on the group id and config id
     * @param context The context of the group event
     * @param groupId group to search for the gadget entity in, 0 for the caller group.
     * @param configId config id of the gadget in the group.
     */
    fun GetGadgetHpPercent(context: GroupEventContext, groupId: Int, configId: Int): Int

    /**
     * Returns a float global value from the gadgets ability definitions.
     * @param context The context of the group event
     * @param groupId group to search for the gadget entity in, 0 for the caller group.
     * @param configId config id of the gadget in the group.
     * @param abilitySGVName name of the abilities svg value to get the float value from.
     * */
    fun GetGadgetAbilityFloatValue(
        context: GroupEventContext,
        groupId: Int,
        configId: Int,
        abilitySGVName: String
    ): Float


    /**
     * Retrieves and returns the gadget id of a gadget entity based on the entity id.
     * @param context The context of the group event
     * @param entityId The entity id of the gadget requested.
     */
    fun GetGadgetIdByEntityId(context: GroupEventContext, entityId: Int): Int


    /**
     * Returns the config id of the gadget with the eid (gadget_eid)
     * @param context The context of the group event
     * @param gadgetEid The entity id of the gadget requested. Table with `gadget_eid` in lua.
     */
    fun GetGadgetConfigId(context: GroupEventContext, gadgetEid: Int): Int

    /**
     * Change the state of a gadget in the defined group
     * @param context The context of the group event
     * @param groupId The group containing the target gadget or the caller group if 0
     * @param configId config id of a gadget in the target group
     * @param gadgetState target state for the gadget
     */
    fun SetGroupGadgetStateByConfigId(context: GroupEventContext, groupId: Int, configId: Int, gadgetState: Int): Int

    /**
     * Change the state of a gadget in the current group
     * @param context The context of the group event
     * @param configId config id of a gadget in the current caller group
     * @param gadgetState target state for the gadget
     */
    fun SetGadgetStateByConfigId(context: GroupEventContext, configId: Int, gadgetState: Int): Int

    /**
     * Change the state of a gadget in the current group, based in the parametersTable
     * @param context The context of the group event
     * @param configId config id of a gadget in the current caller group
     * @param gadgetState target state for the gadget
     */
    fun ChangeGroupGadget(context: GroupEventContext, configId: Int, gadgetState: Int): Int


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
    fun ExecuteGadgetLua(
        context: GroupEventContext, groupId: Int, gadgetCfgId: Int,
        activityType: Int, var4: Int, val5: Int
    ): Int
}