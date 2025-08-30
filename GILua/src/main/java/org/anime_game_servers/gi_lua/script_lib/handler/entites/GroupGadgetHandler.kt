package org.anime_game_servers.gi_lua.script_lib.handler.entites

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.models.PositionImpl
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.utils.ScriptUtils.toVector
import org.anime_game_servers.lua.engine.LuaTable

data class RemainGadgetCountParameters(
    var groupId: Int,
    var gadgetIds: List<Int>?
) {
    companion object {
        fun fromLuaTable(table: LuaTable): RemainGadgetCountParameters? {
            val groupId = table.optInt("group_id", 0)
            val gadgetIds = table.getTable("gadget_id")?.getAsIntArray()?.toList()
            return RemainGadgetCountParameters(
                groupId = groupId,
                gadgetIds = gadgetIds
            )
        }
    }
}

data class CreateGadgetParameters(
    var configId: Int,
    var pos: Vector,
    var rot: Vector,
    var sgvMap: Map<String, Number>? = null,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): CreateGadgetParameters? {
            val configId = table.optInt("config_id", 0)
            val pos = table.getTable("pos")?.toVector()
            val rot = table.getTable("rot")?.toVector()
            val sgvKeys = table.getTable("sgv_key")
            val sgvValues = table.getTable("sgv_value")?.getAsIntArray()?.toList()
            var sgvMap: MutableMap<String, Number>? = null
            if (sgvKeys != null && sgvValues != null && sgvKeys.getSize() == sgvValues.size) {
                sgvMap = mutableMapOf()
                for (i in 0 until sgvKeys.getSize()) {
                    val key = sgvKeys.getString(i+1)
                    if(key == null) continue
                    val value = sgvValues[i]
                    sgvMap[key] = value
                }
            }
            return CreateGadgetParameters(
                configId = configId,
                pos = pos ?: PositionImpl(0f, 0f, 0f),
                rot = rot ?: PositionImpl(0f, 0f, 0f),
                sgvMap = sgvMap
            )
        }
    }
}

/**
 * Handler for scriptlib functions used in GroupScripts related to Gadgets.
 * These are only callable from a lua group context.
 */
interface GroupGadgetHandler<GroupEventContext : GroupEventLuaContext> {

    fun createGadget(context: GroupEventContext, configId: Int): Int
    fun createGadgetWithGlobalValue(context: GroupEventContext, configId: Int, sgvTable: Map<String, Number>): Int


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
    fun createGadgetByParamTable(context: GroupEventContext, creationParams: CreateGadgetParameters): Int

    fun createGadgetWave(
        context: GroupEventContext,
        areaId: Int,
        suitId: Int,
        offset: Int,
        boxSize: Vector,
        gadgetSize: Vector
    ): Int

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
    fun setWorktopOptionsByGroupId(context: GroupEventContext, groupId: Int, configId: Int, options: List<Int>): Int
    fun setWorktopOptions(context: GroupEventContext, options: List<Int>): Int
    fun delWorktopOptionByGroupId(context: GroupEventContext, groupId: Int, configId: Int, option: Int): Int
    fun delWorktopOption(context: GroupEventContext, option: Int): Int


    fun checkRemainGadgetCountByGroupId(context: GroupEventContext, parameters: RemainGadgetCountParameters): Int

    /* Lua */
    /**
     * Executes the OnClientExecuteReq function on a gadgets lua controller.
     * This seems to be used in only the Crucible activity
     * @param groupId group to find the gadget in
     * @param gadgetCfgId cfg id of the gadget in the group to execute lua in or 0 to get from context
     */
    fun executeGadgetLua(
        context: GroupEventContext, groupId: Int, gadgetCfgId: Int,
        param1: Int, param2: Int, param3: Int
    ): Int

}