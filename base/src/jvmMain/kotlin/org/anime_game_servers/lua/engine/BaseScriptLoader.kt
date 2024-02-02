package org.anime_game_servers.lua.engine

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.io.Source
import org.anime_game_servers.lua.models.ScriptType
import java.nio.file.Path
import javax.script.ScriptException

@JvmDefaultWithCompatibility
interface BaseScriptLoader {
    fun getScriptPath(scriptName: String): Path?
    fun getRequireScriptParams(scriptName: String): ScriptLoadParams
    fun openScript(params: ScriptLoadParams): Source?
    fun getScript(
        scriptLoadParams: ScriptLoadParams
    ): LuaScript?

    fun loadData(
        scriptLoadParams: ScriptLoadParams,
        parser: ScriptParser
    ): Boolean {
        val cs = getScript(scriptLoadParams)

        if (cs == null) {
            logger.warn { "No script found for " + scriptLoadParams.getBaseDirectory() }
            return false
        }

        // Eval script
        try {
            cs.evaluate()

            parser.parse(cs)
        } catch (exception: ScriptException) {
            logger.error(exception) { "An error occurred while running the script ${scriptLoadParams.getScriptName()}" }
            return false
        }
        return true
    }

    interface ScriptLoadParams {
        fun getBaseDirectory(): String
        fun getScriptName(): String
        fun getBasePath(): String = getBaseDirectory() + getScriptName()
        fun getScriptType(): ScriptType = ScriptType.DATA_STORAGE
    }

    interface ScriptParser {
        @Throws(ScriptException::class)
        fun parse(script: LuaScript)
    }


    companion object {
        val logger: KLogger = KotlinLogging.logger(BaseScriptLoader::class.java.name)
    }
}
