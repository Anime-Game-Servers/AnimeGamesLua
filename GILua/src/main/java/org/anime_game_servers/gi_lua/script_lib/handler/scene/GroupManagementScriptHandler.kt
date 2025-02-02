package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.gi_lua.models.constants.FlowSuiteOperatePolicy
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

/**
 * Handler for scriptlib functions used to manage the states of groups
 * These are only callable from a lua group context.
 */
interface GroupManagementScriptHandler<GroupEventContext : GroupEventLuaContext> {

    /* group suite */
    fun goToGroupSuite(context: GroupEventContext, groupId: Int, suite: Int): Int
    fun getGroupSuite(context: GroupEventContext, groupId: Int): Int

    /**
     *
     * @param context
     * @param groupId either 0 for the current group, or a group id
     * @param suite
     * @return
     */
    fun addExtraGroupSuite(context: GroupEventContext, groupId: Int, suite: Int): Int
    fun removeExtraGroupSuite(context: GroupEventContext, groupId: Int, suite: Int): Int
    fun killExtraGroupSuite(context: GroupEventContext, groupId: Int, suite: Int): Int

    /* flow suite */
    fun goToFlowSuite(context: GroupEventContext, groupId: Int, suite: Int): Int
    fun setFlowSuite(context: GroupEventContext, groupId: Int, suite: Int): Int

    fun addExtraFlowSuite(
        context: GroupEventContext,
        groupId: Int,
        suiteId: Int,
        flowSuitePolicy: FlowSuiteOperatePolicy?
    ): Int

    fun removeExtraFlowSuite(
        context: GroupEventContext,
        groupId: Int,
        suiteId: Int,
        flowSuitePolicy: FlowSuiteOperatePolicy?
    ): Int

    fun killExtraFlowSuite(
        context: GroupEventContext,
        groupId: Int,
        suiteId: Int,
        flowSuitePolicy: FlowSuiteOperatePolicy?
    ): Int


    /* Group link bundle */
    fun activateGroupLinkBundle(context: GroupEventContext, groupId: Int): Int
    fun activateGroupLinkBundleByBundleId(context: GroupEventContext, bundleId: Int): Int
    fun deactivateGroupLinkBundle(context: GroupEventContext, groupId: Int): Int
    fun deactivateGroupLinkBundleByBundleId(context: GroupEventContext, bundleId: Int): Int
    fun finishGroupLinkBundle(context: GroupEventContext, groupId: Int): Int

    /* group variables */
    fun createGroupVariable(context: GroupEventContext, varName: String, value: Int): Int
    fun getGroupVariableValue(context: GroupEventContext, varName: String): Int
    fun getGroupVariableValueByGroup(context: GroupEventContext, varName: String, groupId: Int): Int
    fun setGroupVariableValue(context: GroupEventContext, varName: String, value: Int): Int
    fun setGroupVariableValueByGroup(context: GroupEventContext, key: String, value: Int, groupId: Int): Int
    fun changeGroupVariableValue(context: GroupEventContext, varName: String, value: Int): Int
    fun changeGroupVariableValueByGroup(context: GroupEventContext, varName: String, value: Int, groupId: Int): Int

    /* group temp value*/
    fun setGroupTempValue(context: GroupEventContext, name: String, value: Int, var3Table: LuaTable?): Int
    fun getGroupTempValue(context: GroupEventContext, name: String, var2: LuaTable?): Int
    fun changeGroupTempValue(context: GroupEventContext, name: String, diff: Int, var3: LuaTable?): Int


    /* misc */
    /**
     * Set the actions and triggers to designated group
     */
    fun refreshGroup(context: GroupEventContext, table: LuaTable?): Int
    fun setGroupReplaceable(context: GroupEventContext, groupId: Int, value: Boolean): Int
    fun createGroupTrigger(context: GroupEventContext, triggerName: String): Int
    fun setGroupDead(context: GroupEventContext, groupId: Int): Int
    fun isGroupRegisteredInCurScene(context: GroupEventContext, groupId: Int): Int
    fun unfreezeGroupLimit(context: GroupEventContext, forceId: Int): Int

    /* group lua execution */
    /**
     * TODO better parameter handling and verify active handling
     * Calls a lua function in the specified group if the group is active. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    fun executeActiveGroupLua(
        context: GroupEventLuaContext,
        groupId: Int,
        functionName: String?,
        callParamsTable: LuaTable?
    ): Int

    /**
     * TODO better parameter handling
     * Calls a lua function in the specified group. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    fun executeGroupLua(
        context: GroupEventLuaContext?,
        groupId: Int,
        functionName: String?,
        callParamsTable: LuaTable?
    ): Int
}