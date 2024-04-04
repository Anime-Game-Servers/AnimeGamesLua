package org.anime_game_servers.luaj_engine

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import kotlinx.io.asInputStream
import kotlinx.io.buffered
import org.anime_game_servers.lua.engine.*
import org.anime_game_servers.lua.models.ScriptType
import org.anime_game_servers.luaj_engine.coerse.CoerceJavaToLua
import org.luaj.vm2.lib.ResourceFinder
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
                if (scriptConfig.enableIncludeWorkaround == RequireMode.DISABLED)
                    return ByteArrayInputStream(ByteArray(0))

                val params = scriptConfig.scriptLoader.getRequireScriptParams(filename)
                val stream = scriptConfig.scriptLoader.openScript(params)?.buffered()?.asInputStream()
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
            var instance = staticClass.kotlin.objectInstance
            if (instance == null) {
                instance = staticClass.getConstructor().newInstance()
            }
            context.globals[name] = CoerceJavaToLua.coerce(instance)
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
            return LuaJScript(this, scriptPath, scriptType)
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ScriptException) {
            throw RuntimeException(e)
        }
    }

    override fun getTable(table: Any): LuaTable {
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
