package org.anime_game_servers.gi_lua.test.models

import kotlinx.io.Source
import org.anime_game_servers.gi_lua.models.loader.GIScriptLoader
import org.anime_game_servers.lua.engine.BaseScriptLoader
import org.anime_game_servers.lua.engine.LuaEngine
import org.anime_game_servers.lua.engine.LuaScript
import org.anime_game_servers.lua.utils.asSource
import java.nio.file.Files
import java.nio.file.Path

abstract class TestScriptLoader : GIScriptLoader {
    abstract val engine: LuaEngine
    override fun getScriptPath(scriptName: String): Path? {
        val uri = ClassLoader.getSystemResource(scriptName).toURI()
        return Path.of(uri)
    }

    override fun openScript(params: BaseScriptLoader.ScriptLoadParams): Source? {
        val basePath: String = params.getBasePath()
        val scriptPath = getScriptPath(basePath) ?: return null
        if(Files.exists(scriptPath)){
            return scriptPath.asSource()
        }
        return null
    }

    override fun getScript(scriptLoadParams: BaseScriptLoader.ScriptLoadParams): LuaScript? {
        val basePath: String = scriptLoadParams.getBasePath()
        val scriptPath = getScriptPath(basePath) ?: return null
        return engine.getScript(scriptPath, scriptLoadParams.getScriptType());
    }
}
