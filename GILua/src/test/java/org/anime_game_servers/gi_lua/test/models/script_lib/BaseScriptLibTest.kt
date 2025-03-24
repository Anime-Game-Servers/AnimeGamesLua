package org.anime_game_servers.gi_lua.test.models.script_lib

import org.anime_game_servers.gi_lua.test.models.TestScriptLoader
import org.anime_game_servers.lua.engine.LuaEngine
import org.anime_game_servers.lua.engine.LuaScript
import org.anime_game_servers.lua.models.ScriptType
import java.nio.file.Path

abstract class BaseScriptLibTest {
    abstract val scriptLoader : TestScriptLoader

    protected fun loadScript(scriptName: String): LuaScript {
        val engine = scriptLoader.engine
        LuaEngine.registerNamespace(this::class.java.packageName)
        scriptLoader.addDefaultsForEngine(engine)
        val uri = ClassLoader.getSystemResource(scriptName).toURI()
        val path = Path.of(uri)
        val script = engine.getScript(path, ScriptType.EXECUTABLE) ?: throw IllegalStateException("Script not found")
        script.evaluate()
        return script
    }
}