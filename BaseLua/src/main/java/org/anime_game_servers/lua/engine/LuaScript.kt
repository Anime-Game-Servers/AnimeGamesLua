package org.anime_game_servers.lua.engine

import javax.annotation.Nonnull
import javax.script.ScriptException

interface LuaScript {
    fun hasMethod(@Nonnull methodName: String): Boolean

    @Throws(ScriptException::class, NoSuchMethodException::class)
    fun callMethod(@Nonnull methodName: String, vararg args: Any?): LuaValue?

    @Throws(ScriptException::class)
    fun evaluate()
    fun <T> getGlobalVariable(name: String, type: Class<T>): T?
    fun <T> getGlobalVariableList(name: String, type: Class<T>): List<T>
    fun <T> getGlobalVariableMap(name: String, type: Class<T>): Map<String, T>
    val engine: LuaEngine?
}
