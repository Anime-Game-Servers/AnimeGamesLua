package org.anime_game_servers.gi_lua.script_lib.handler.gadget

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

/**
 * This handles script lib functions for handling vehicle gadgets.
 * These are only callable from a group context.
 */
interface VehicleScriptHandler<GroupEventContext : GroupEventLuaContext> {
    /**
     * Spawn a vehicle gadget with the given parameters
     * @param uid The uid that will become the owner of the vehicle
     * @param gadgetId The gadgetId of the vehicle gadget to spawn
     * @param position The position to spawn the vehicle at
     * @param rot The rotation to spawn the vehicle with
     */
    fun createVehicle(context: GroupEventContext, uid: Int, gadgetId: Int, position: Vector, rot: Vector): Int
}