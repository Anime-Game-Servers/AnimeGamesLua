package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions that are specific to the SummerTime activity.
 * These are only callable from a group context.
 */
interface SummerTimeScriptHandler <GroupEventContext : GroupEventLuaContext> {
    /**
     * Unlocks a float signal gadget with the specified config id in the specified group.
     */
    fun UnlockFloatSignal(context: GroupEventContext, groupId: Int, signalGadgetConfigId: Int): Int
}