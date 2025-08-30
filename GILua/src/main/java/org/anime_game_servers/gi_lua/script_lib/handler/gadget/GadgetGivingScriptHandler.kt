package org.anime_game_servers.gi_lua.script_lib.handler.gadget

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the giving system for gadgets.
 * These are only callable from a group context.
 */
interface GadgetGivingScriptHandler<GroupEventContext : GroupEventLuaContext> {
    /**
     * @param context
     * @param givingId The id if the giving element found in [GivingData]
     * @param groupId The groupdId of the group containing the gadget
     * @param gadgetCfgId The gadgets target configId
     * @return 0 if success, something else if failed
     */
    fun activeGadgetItemGiving(context: GroupEventContext, givingId: Int, groupId: Int, gadgetCfgId: Int): Int

    fun getGivingItemList(context: GroupEventContext, givingId: Int): List<Int>?
}