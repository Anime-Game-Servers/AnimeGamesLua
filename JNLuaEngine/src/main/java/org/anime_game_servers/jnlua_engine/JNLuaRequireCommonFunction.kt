package org.anime_game_servers.jnlua_engine

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import kotlinx.io.asInputStream
import org.anime_game_servers.lua.engine.RequireMode
import org.anime_game_servers.lua.engine.ScriptConfig
import org.terasology.jnlua.LuaState
import org.terasology.jnlua.NamedJavaFunction
import java.io.IOException
import javax.annotation.Nonnull

private val logger = logger {}

class JNLuaRequireCommonFunction private constructor(private val scriptConfig: ScriptConfig) : NamedJavaFunction {
    override fun invoke(luaState: LuaState): Int {
        if (scriptConfig.enableIncludeWorkaround == RequireMode.DISABLED) {
            return 0
        }

        val requiredName = luaState.checkString(1)
        luaState.remove(1)
        val params = scriptConfig.scriptLoader.getRequireScriptParams(requiredName)
        try {
            scriptConfig.scriptLoader.openScript(params).use { includeScript ->
                if (includeScript == null) {
                    logger.error { "Require script not found. ${params.getBasePath()}" }
                    return 1
                }
                val stream = includeScript.asInputStream()
                luaState.load(stream, requiredName, "t")
                luaState.call(0, 0)
            }
        } catch (e: IOException) {
            logger.error(e) { "Error on loading require script. ${params.getBasePath()}" }
            return 2
        }

        return 0
    }

    override fun getName(): String {
        return "require"
    }

    companion object {
        private var INSTANCE: JNLuaRequireCommonFunction? = null

        @JvmStatic
        fun getInstance(@Nonnull scriptConfig: ScriptConfig): JNLuaRequireCommonFunction {
            if (INSTANCE == null || INSTANCE?.scriptConfig != scriptConfig) {
                INSTANCE = JNLuaRequireCommonFunction(scriptConfig)
            }
            return INSTANCE!!
        }
    }
}
