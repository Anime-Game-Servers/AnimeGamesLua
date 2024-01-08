package org.anime_game_servers.luaj_engine

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.lua.engine.LuaEngine
import org.anime_game_servers.lua.engine.LuaScript
import org.anime_game_servers.lua.engine.LuaTable
import org.anime_game_servers.lua.engine.ScriptConfig
import org.anime_game_servers.lua.models.ScriptType
import org.luaj.vm2.lib.ResourceFinder
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.script.LuajContext
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.InvocationTargetException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class LuaJEngine(override val scriptConfig: ScriptConfig) : LuaEngine {
    private val manager = ScriptEngineManager()
    private val context: LuajContext

    val engine: ScriptEngine = manager.getEngineByName("luaj")

    override val serializer: LuaJSerializer = LuaJSerializer()

    init {
        context = engine.context as LuajContext

        // Set engine to replace require as a temporary fix to missing scripts
        context.globals.finder = object : ResourceFinder {
            override fun findResource(filename: String): InputStream {
                val scriptPath = scriptConfig.scriptLoader.getScriptPath(filename)
                val stream = scriptConfig.scriptLoader.openScript(scriptPath!!)
                return stream ?: ByteArrayInputStream(ByteArray(0))
            }

            override fun useRawParamString(): Boolean {
                return true
            }
        }
    }

    override fun <T : Enum<*>> addGlobalEnum(name: String, enumArray: Array<T>): Boolean {
        val table = org.luaj.vm2.LuaTable()
        Arrays.stream(enumArray).forEach { e: T ->
            val fieldName = getEnumFieldName(e)
            val value = getEnumFieldValue(e)
            table[fieldName] = value
            table[fieldName.uppercase(Locale.getDefault())] = value
        }
        context.globals[name] = table
        return true
    }

    override fun addGlobalStaticClass(name: String, staticClass: Class<*>): Boolean {
        try {
            context.globals[name] = CoerceJavaToLua.coerce(staticClass.getConstructor().newInstance())
            return true
        } catch (e: InstantiationException) {
            logger.error(e) { "Failed to add static class to lua engine: $name" }
        } catch (e: IllegalAccessException) {
            logger.error(e) { "Failed to add static class to lua engine: $name" }
        } catch (e: InvocationTargetException) {
            logger.error(e) { "Failed to add static class to lua engine: $name" }
        } catch (e: NoSuchMethodException) {
            logger.error(e) { "Failed to add static class to lua engine: $name" }
        }
        return false
    }

    override fun addObject(name: String, `object`: Any): Boolean {
        context.globals[name] = CoerceJavaToLua.coerce(`object`)
        return true
    }

    override fun getScript(scriptPath: Path, scriptType: ScriptType): LuaScript? {
        if (!Files.exists(scriptPath)) return null

        try {
            return LuaJScript(this, scriptPath)
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ScriptException) {
            throw RuntimeException(e)
        }
    }

    override fun getTable(table: Any): LuaTable? {
        if (table is org.luaj.vm2.LuaTable) return LuaJTable(this, table)
        throw IllegalArgumentException("Table must be a LuaTable")
    }

    override fun createTable(): LuaTable {
        return LuaJTable(this, org.luaj.vm2.LuaTable())
    }

    companion object {
        private val logger = logger(LuaJEngine::class.java.name)
    }
}
