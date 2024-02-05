package org.anime_game_servers.lua.models

import org.anime_game_servers.lua.engine.Class
import kotlin.jvm.JvmField

open class BooleanLuaValue(private val value: Boolean) : MockLuaValue {
    override fun isBoolean() = true

    override fun asBoolean() = value
    override fun asInteger() = if (value) 1 else 0
    override fun asLong() = if (value) 1L else 0L
    override fun asDouble() = if (value) 1.0 else 0.0
    override fun asFloat() = if (value) 1f else 0f
    override fun asString() = value.toString()

    override fun <T> asObject(type: Class<T>): T? = null

    companion object {
        @JvmField
        val TRUE = BooleanLuaValue(true)
        @JvmField
        val FALSE = BooleanLuaValue(false)
    }
}
