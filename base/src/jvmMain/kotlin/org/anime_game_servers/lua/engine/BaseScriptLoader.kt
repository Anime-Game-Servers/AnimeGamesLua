package org.anime_game_servers.lua.engine

import java.io.InputStream
import java.nio.file.Path

interface BaseScriptLoader {
    fun openScript(scriptPath: Path): InputStream?
    fun getScriptPath(scriptName: String): Path?
}
