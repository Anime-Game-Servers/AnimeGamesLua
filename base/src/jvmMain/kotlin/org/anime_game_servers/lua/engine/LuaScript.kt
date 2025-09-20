package org.anime_game_servers.lua.engine

import javax.script.ScriptException
import java.lang.Class

interface LuaScript {
    fun hasMethod(methodName: String): Boolean

    @Throws(ScriptException::class, NoSuchMethodException::class)
    fun callMethod(methodName: String, vararg args: Any?): LuaValue?

    @Throws(ScriptException::class)
    fun evaluate()
    fun <T> getGlobalVariable(name: String, type: Class<T>): T?
    fun <T> getGlobalVariableList(name: String, type: Class<T>): List<T>
    fun <K,V> getGlobalVariableMap(name: String, keyType: Class<K>, valueType: Class<V>): Map<K, V>
    val engine: LuaEngine?


}
