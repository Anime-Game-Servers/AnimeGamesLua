package org.anime_game_servers.gi_lua.script_lib.handler.entites

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext


interface AbilityScriptHandler<GroupEventContext : GroupEventLuaContext> {
    /* FloatValue */

    fun addEntityGlobalFloatValueByConfigId(
        context: GroupEventContext,
        cfgIdList: List<Int>,
        floatValueKey: String,
        value: Int
    ): Int

    fun setTeamEntityGlobalFloatValue(
        context: GroupEventContext,
        uidList: List<Int>,
        floatValueKey: String,
        value: Float
    ): Int

    fun addTeamEntityGlobalFloatValue(
        context: GroupEventContext,
        uidList: List<Int>,
        floatValueKey: String,
        value: Int
    ): Int

    /**
     * Returns a float global value from the gadgets ability definitions.
     * @param context The context of the group event
     * @param groupId group to search for the gadget entity in, 0 for the caller group.
     * @param configId config id of the gadget in the group.
     * @param floatValueKey name of the abilities svg value to get the float value from.
     * */
    fun getGadgetAbilityFloatValue(
        context: GroupEventContext,
        groupId: Int,
        configId: Int,
        floatValueKey: String
    ): Float

    fun getMonsterAbilityFloatValue(
        context: GroupEventContext,
        groupId: Int,
        configId: Int,
        floatValueKey: String
    ): Float

    fun getTeamAbilityFloatValue(context: GroupEventContext, uid: Int, floatValueKey: String): Float


    /* SGV */

    fun setEntityServerGlobalValueByConfigId(context: GroupEventContext, cfgId: Int, sgvName: String, value: Int): Int
    fun setEntityServerGlobalValueByEntityId(
        context: GroupEventContext,
        entityId: Int,
        sgvName: String,
        value: Int
    ): Int

    fun setTeamServerGlobalValue(context: GroupEventContext, sceneUid: Int, sgvName: String, value: Int): Int
    fun addTeamServerGlobalValue(context: GroupEventContext, ownerId: Int, sgvName: String, value: Int): Int
    fun getTeamServerGlobalValue(context: GroupEventContext, ownerId: Int, sgvName: String, value: Int): Int

    fun setGroupLogicStateValue(context: GroupEventContext, sgvName: String, value: Int): Int
}