package org.anime_game_servers.jnlua_engine

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.lua.engine.LuaEngine
import org.anime_game_servers.lua.engine.LuaScript
import org.anime_game_servers.lua.engine.LuaTable
import org.anime_game_servers.lua.engine.ScriptConfig
import org.anime_game_servers.lua.models.ScriptType
import org.terasology.jnlua.JavaFunction
import org.terasology.jnlua.LuaState
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import javax.script.SimpleBindings

class JNLuaEngine(override val scriptConfig: ScriptConfig) : LuaEngine {
    val manager = ScriptEngineManager()

    override val serializer: JNLuaSerializer = JNLuaSerializer()

    val bindings = SimpleBindings()

    init {
        bindings["print"] = JavaFunction { luaState: LuaState ->
            logger.debug { "[LUA] print " + luaState.checkString(1) }
            1
        }
    }

    override fun <T : Enum<*>> addGlobalEnum(name: String, enumArray: Array<T>): Boolean {
        val table: MutableMap<String, Int> = HashMap()
        Arrays.stream(enumArray).forEach { e: T ->
            val fieldName = getEnumFieldName(e)
            val value = getEnumFieldValue(e)
            table[fieldName] = value
            table[fieldName.uppercase(Locale.getDefault())] = value
        }
        bindings[name] = table
        return true
    }

    override fun addGlobalStaticClass(name: String, staticClass: Class<*>): Boolean {
        try {
            bindings[name] = StaticClassWrapper(staticClass)
            return true
        } catch (e: Exception) {
            logger.error(e) { "Failed to add static class to lua engine: $name" }
        }
        return false
    }

    override fun addObject(name: String, `object`: Any): Boolean {
        bindings[name] = `object`
        return false
    }

    override fun getScript(scriptPath: Path, scriptType: ScriptType): LuaScript? {
        if (!Files.exists(scriptPath)) return null

        try {
            return JNLuaScript(this, scriptPath, scriptType)
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ScriptException) {
            throw RuntimeException(e)
        }
    }

    override fun getTable(table: Any): LuaTable? {
        return JNLuaTable((table as AbstractMap<*, *>))
    }

    override fun createTable(): LuaTable {
        return JNLuaTable(HashMap<Any, Any>())
    }

    companion object {
        private val logger = logger(JNLuaEngine::class.java.name)
    }
}
