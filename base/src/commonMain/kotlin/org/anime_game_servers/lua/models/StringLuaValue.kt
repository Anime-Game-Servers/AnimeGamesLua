package org.anime_game_servers.lua.models

import org.anime_game_servers.lua.engine.Class
import kotlin.jvm.JvmField

open class StringLuaValue(protected val value: String) : MockLuaValue {
    override fun isInteger() = true

    override fun asBoolean() = when(value.lowercase()){
        "true" -> true
        "false" -> false
        else -> try {
            value.toInt() != 0
        } catch (e: NumberFormatException) {
            false
        }
    }
    override fun asInteger() = value.toInt()
    override fun asLong() = value.toLong()
    override fun asDouble() = value.toDouble()
    override fun asFloat() = value.toFloat()
    override fun asString() = value

    override fun <T> asObject(type: Class<T>): T? = null
    override fun <K, V> asMap(keyType: Class<K>, valueType: Class<V>) = null
    override fun <T> asList(type: Class<T>) = null

    companion object {
        @JvmField
        val EMPTY = StringLuaValue("")
    }
}
