package org.anime_game_servers.gi_lua.test.models

import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.models.scene.group.SceneGroup
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.script_lib.ScriptLibGroupHandlerProvider
import org.anime_game_servers.gi_lua.script_lib.handler.activity.AsterScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.activity.CrystalLinkScriptHandler
import org.anime_game_servers.lua.engine.LuaEngine

data class LuaGroupContextImpl(
    override val engine: LuaEngine, val scriptArgs: ScriptArgs, val ouid: Int = 0
) : GroupEventLuaContext {
    var scriptlibHandlerProvider: ScriptLibGroupHandlerProvider<LuaGroupContextImpl>? = TestScriptLibHandlerProvider()
    override fun getGroupInstance(): SceneGroup {
        TODO("Not yet implemented")
    }

    override fun getArgs() = scriptArgs

    override fun <T : GroupEventLuaContext> getScriptLibHandlerProvider(): ScriptLibGroupHandlerProvider<T> =
        scriptlibHandlerProvider as ScriptLibGroupHandlerProvider<T>

    override fun uid(): Int = scriptArgs.uid
    override fun sourceEntityId(): Int = scriptArgs.sourceEid
    override fun targetEntityId(): Int = scriptArgs.targetEid
    override fun ownerUid(): Int = ouid
}

class TestScriptLibHandlerProvider : ScriptLibGroupHandlerProvider<LuaGroupContextImpl> {
    var asterScriptHandler: AsterScriptHandler<LuaGroupContextImpl>? = null
    var crystalLinkScriptHandler: CrystalLinkScriptHandler<LuaGroupContextImpl>? = null

    override fun getAsterHandler() = asterScriptHandler

    override fun getCrystalLinkHandler() = crystalLinkScriptHandler
}