package org.anime_game_servers.lua.engine

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.annotations.lua.LuaStatic
import org.anime_game_servers.core.base.interfaces.IntKey
import org.anime_game_servers.core.base.interfaces.IntValueEnum
import org.anime_game_servers.lua.models.ScriptType
import org.anime_game_servers.lua.serialize.Serializer
import org.reflections.Reflections
import java.nio.file.Path

interface LuaEngine {
    val scriptConfig: ScriptConfig

    fun <T : Enum<*>> addGlobalEnum(name: String, enumArray: Array<T>): Boolean

    fun addGlobalStaticClass(name: String, staticClass: Class<*>): Boolean

    fun addObject(name: String, `object`: Any): Boolean

    val serializer: Serializer?

    fun getScript(scriptName: String, scriptType: ScriptType): LuaScript? {
        val scriptPath = scriptConfig.scriptLoader.getScriptPath(scriptName) ?: return null
        return getScript(scriptPath, scriptType)
    }

    fun getScript(scriptPath: Path, scriptType: ScriptType): LuaScript?

    fun getTable(table: Any): LuaTable?

    fun createTable(): LuaTable

    fun getEnumFieldName(enumValue: Enum<*>): String {
        return enumValue::class.java.getField(enumValue.name).getAnnotation(LuaNames::class.java)?.let {
            it.names.firstOrNull() ?: enumValue.name
        } ?: enumValue.name
    }
    fun getEnumFieldValue(enumValue: Enum<*>): Int {
        return when (enumValue){
            is IntValueEnum -> enumValue.getValue()
            is IntKey -> enumValue.getIntKey()
            else -> enumValue.ordinal
        }
    }

    private fun getLuaName(classObject: Class<*>): String {
        return classObject.getAnnotation(LuaNames::class.java)?.let {
            it.names.firstOrNull() ?: classObject.simpleName
        } ?: classObject.simpleName
    }

    fun addGlobals() {
        registeredEnums.forEach { enumClass ->
            val enumArray = enumClass.enumConstants
            addGlobalEnum(getLuaName(enumClass), enumArray)
        }
        registeredStaticClasses.forEach { staticClass ->
            addGlobalStaticClass(getLuaName(staticClass), staticClass)
        }
    }

    companion object {

        var registeredEnums : MutableList<Class<Enum<*>>> = mutableListOf()
        var registeredStaticClasses : MutableList<Class<*>> = mutableListOf()
        @JvmStatic
        fun registerNamespace(namespace: String){
            val reflector = Reflections(namespace)
            registerDefaults(reflector)
        }
        @JvmStatic
        fun registerDefaults(reflector: Reflections){
            reflector.getTypesAnnotatedWith(LuaStatic::class.java).forEach { annotatedClass ->
                when{
                    annotatedClass.isAnnotation -> return@forEach
                    annotatedClass.isEnum -> {
                        registeredEnums.add(annotatedClass as Class<Enum<*>>)
                    }
                    else -> registeredStaticClasses.add(annotatedClass)
                }
            }
        }
    }
}
