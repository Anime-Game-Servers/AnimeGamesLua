package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.script_lib.handler.GadgetControllerHandler
import org.anime_game_servers.gi_lua.script_lib.handler.gadget_controller.GadgetPlayControllerHandler

interface ScriptLibControllerHandlerProvider<GadgetEntity, ControllerEventContext : ControllerLuaContext<GadgetEntity>> {
    fun getGadgetControllerHandler(): GadgetControllerHandler<GadgetEntity, ControllerEventContext>?
    fun getGadgetPlayControllerHandler(): GadgetPlayControllerHandler<GadgetEntity, ControllerEventContext>?
}