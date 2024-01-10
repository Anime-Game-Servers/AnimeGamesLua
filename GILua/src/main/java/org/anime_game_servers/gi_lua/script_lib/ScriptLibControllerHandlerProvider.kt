package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.script_lib.handler.GadgetControllerHandler

interface ScriptLibControllerHandlerProvider<GadgetEntity, ControllerEventContext : ControllerLuaContext<GadgetEntity>> {
    fun getGadgetControllerHandler(): GadgetControllerHandler<GadgetEntity, ControllerEventContext>?
}