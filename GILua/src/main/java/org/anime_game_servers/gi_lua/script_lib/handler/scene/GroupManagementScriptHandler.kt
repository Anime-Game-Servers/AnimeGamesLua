package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.FlowSuiteOperatePolicy
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

data class RefreshGroupParams(
    @LuaNames("group_id")
    val groupId: Int,
    @LuaNames("suite")
    val suiteId: Int,
    @LuaNames("refresh_level_revise")
    val refreshLevelRevise: Int = 0,
    @LuaNames("exclude_prev")
    val excludePrev: Boolean = false,
    @LuaNames("is_force_random_suite")
    val isForceRandomSuite: Boolean = false,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): RefreshGroupParams? {
            val groupId = table.optInt("group_id", 0)
            val suiteId = table.optInt("suite", 0)
            val refreshLevelRevise = table.optInt("refresh_level_revise", 0)
            val excludePrev = table.optBoolean("exclude_prev", false)
            val isForceRandomSuite = table.optBoolean("is_force_random_suite", false)
            return RefreshGroupParams(groupId, suiteId, refreshLevelRevise, excludePrev, isForceRandomSuite)
        }
    }
}

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

    /*
    * flow suite
    * Flow suites have the IO type [IOType.GROUP_IO_TYPE_FLOW] and use SuiteDisks instead of normal suites
    */
    fun goToFlowSuite(context: GroupEventContext, groupId: Int, suite: Int): Int
    fun setFlowSuite(context: GroupEventContext, groupId: Int, suite: Int): Int

    fun addExtraFlowSuite(
        context: GroupEventContext,
        groupId: Int,
        suiteId: Int,
        flowSuitePolicy: FlowSuiteOperatePolicy
    ): Int

    fun removeExtraFlowSuite(
        context: GroupEventContext,
        groupId: Int,
        suiteId: Int,
        flowSuitePolicy: FlowSuiteOperatePolicy
    ): Int

    fun killExtraFlowSuite(
        context: GroupEventContext,
        groupId: Int,
        suiteId: Int,
        flowSuitePolicy: FlowSuiteOperatePolicy
    ): Int


    /* Group link bundle */
    fun activateGroupLinkBundle(context: GroupEventContext, groupId: Int): Int
    fun activateGroupLinkBundleByBundleId(context: GroupEventContext, bundleId: Int): Int
    fun deactivateGroupLinkBundle(context: GroupEventContext, groupId: Int): Int
    fun deactivateGroupLinkBundleByBundleId(context: GroupEventContext, bundleId: Int): Int
    fun finishGroupLinkBundle(context: GroupEventContext, groupId: Int): Int
    /**
     * updates group link bundle show state bool and sends GroupLinkMarkUpdateNotify to all in world
     */
    fun updateBundleMarkShowStateByGroupId(context: GroupEventContext?, groupId: Int, isShow: Boolean): Int

    /* group variables */
    fun createGroupVariable(context: GroupEventContext, varName: String, value: Int): Int
    fun getGroupVariableValue(context: GroupEventContext, varName: String): Int
    fun getGroupVariableValueByGroup(context: GroupEventContext, varName: String, groupId: Int): Int
    fun setGroupVariableValue(context: GroupEventContext, varName: String, value: Int): Int
    fun setGroupVariableValueByGroup(context: GroupEventContext, key: String, value: Int, groupId: Int): Int
    fun changeGroupVariableValue(context: GroupEventContext, varName: String, value: Int): Int
    fun changeGroupVariableValueByGroup(context: GroupEventContext, varName: String, value: Int, groupId: Int): Int

    /* group temp value*/
    fun setGroupTempValue(context: GroupEventContext, name: String, value: Int, groupId: Int): Int
    fun getGroupTempValue(context: GroupEventContext, name: String, groupId: Int): Int
    fun changeGroupTempValue(context: GroupEventContext, name: String, diff: Int, groupId: Int): Int

    /* group trigger */
    // returns the trigger count from a GroupTrigger that's currently executed
    fun createGroupTrigger(context: GroupEventContext, triggerName: String): Int
    fun getCurTriggerCount(context: GroupEventContext): Int

    /* group entity checks */
    fun getGroupAliveMonsterList(context: GroupEventContext, groupId: Int): IntArray?
    fun getGroupMonsterCountByGroupId(context: GroupEventContext, groupId: Int): Int
    fun getGroupMonsterCount(context: GroupEventContext): Int
    // Checks if an entity with that configId exists in the group with the id groupId
    fun checkIsInGroup(context: GroupEventContext, groupId: Int, configId: Int): Boolean

    /* misc */
    /**
     * Set the actions and triggers to designated group
     */
    fun refreshGroup(context: GroupEventContext, params: RefreshGroupParams): Int
    fun setGroupReplaceable(context: GroupEventContext, groupId: Int, value: Boolean): Int
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
        context: GroupEventContext,
        groupId: Int,
        functionName: String,
        callParams: List<Int>
    ): Int

    /**
     * TODO better parameter handling
     * Calls a lua function in the specified group. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * If the group is not loaded yet, this should force load the group and then call the function.
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    fun executeGroupLua(
        context: GroupEventContext,
        groupId: Int,
        functionName: String,
        callParams: List<Int>
    ): Int
}