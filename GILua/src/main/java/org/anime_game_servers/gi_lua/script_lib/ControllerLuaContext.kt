package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.script_lib.handler.GadgetControllerHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupGadgetHandler

/**
 * Lua context for calls to a Gadget Controller scripts functions.
 * This context always contains a reference to the entity that calls the controller function
 */
interface ControllerLuaContext<GadgetEntity> : LuaContext {
    val gadget:GadgetEntity

    fun <T: ControllerLuaContext<GadgetEntity>> getScriptLibHandlerProvider(): ScriptLibControllerHandlerProvider<GadgetEntity, T>

    fun <T>onGadgetControllerHandler(block: GadgetControllerHandler<GadgetEntity, ControllerLuaContext<GadgetEntity>>.() -> T) : T {
        return getScriptLibHandlerProvider<ControllerLuaContext<GadgetEntity>>().getGadgetControllerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
}
