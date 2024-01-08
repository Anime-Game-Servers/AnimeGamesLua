package org.anime_game_servers.gi_lua.script_lib

/**
 * Lua context for calls to a Gadget Controller scripts functions.
 * This context always contains a reference to the entity that calls the controller function
 */
interface ControllerLuaContext<GadgetEntity> : LuaContext {
    val gadget:GadgetEntity

    fun <T: ControllerLuaContext<GadgetEntity>> getScriptLibHandlerProvider(): ScriptLibControllerHandlerProvider<GadgetEntity, T>
}
