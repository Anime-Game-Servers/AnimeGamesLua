package org.anime_game_servers.gi_lua.models.loader

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.lua.engine.BaseScriptLoader
import org.anime_game_servers.lua.engine.LuaEngine

@JvmDefaultWithCompatibility
interface GIScriptLoader : BaseScriptLoader {

    fun addDefaultsForEngine(luaEngine: LuaEngine) {
        LuaEngine.registerNamespace("org.anime_game_servers.core.gi")
        LuaEngine.registerNamespace("org.anime_game_servers.gi_lua")
        luaEngine.addGlobals()
    }

    override fun getRequireScriptParams(scriptName: String): BaseScriptLoader.ScriptLoadParams {
        val path = scriptName.substringBeforeLast('/', "").let {
            if (it.isEmpty()) "" else "$it/"
        }
        val file = scriptName.substringAfterLast('/').let {
            if(!it.endsWith(".lua")) "${it}.lua" else it
        }
        return CommonScriptLoadParams(path, file)
    }

    companion object {
        val logger: KLogger = logger(GIScriptLoader::class.java.name)
    }
}
