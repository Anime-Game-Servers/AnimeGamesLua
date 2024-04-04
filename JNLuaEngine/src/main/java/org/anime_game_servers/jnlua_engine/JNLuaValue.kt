package org.anime_game_servers.jnlua_engine

import org.anime_game_servers.lua.engine.LuaValue

class JNLuaValue internal constructor(private val engine: JNLuaEngine, private val value: Any?) : LuaValue {
    override fun isNull(): Boolean {
        return value == null
    }

    override fun isBoolean(): Boolean {
        return value is Boolean
    }

    override fun isInteger(): Boolean {
        return value is Int
    }

    override fun isLong(): Boolean {
        return value is Long
    }

    override fun isDouble(): Boolean {
        return value is Double
    }

    override fun isFloat(): Boolean {
        return value is Float
    }

    override fun isString(): Boolean {
        return value is String
    }

    override fun isTable(): Boolean {
        return value is Map<*, *> || value is JNLuaTable || value is Collection<*> || value != null && value !is Number && value !is Boolean && value !is String
    }

    override fun asBoolean(): Boolean {
        return when(value){
            is Boolean -> value
            is Number -> value.toInt() != 0
            is String -> value.toBoolean()
            else -> false
        }
    }

    override fun asInteger(): Int {
        return when(value){
            is Number -> value.toInt()
            is Boolean -> if(value) 1 else 0
            is String -> value.toInt()
            else -> 0
        }
    }

    override fun asLong(): Long {
        return when(value){
            is Number -> value.toLong()
            is Boolean -> if(value) 1L else 0L
            is String -> value.toLong()
            else -> 0L
        }
    }

    override fun asDouble(): Double {
        return when(value){
            is Number -> value.toDouble()
            is Boolean -> if(value) 1.0 else 0.0
            is String -> value.toDouble()
            else -> 0.0
        }
    }

    override fun asFloat(): Float {
        return when(value){
            is Number -> value.toFloat()
            is Boolean -> if(value) 1f else 0f
            is String -> value.toFloat()
            else -> 0f
        }
    }

    override fun asString(): String? {
        return when(value){
            is String -> value
            is Number -> value.toString()
            is Boolean -> value.toString()
            else -> null
        }
    }

    override fun <T> asObject(type: Class<T>): T? {
        if (value == null || !isTable()) {
            return null
        }

        if(type.isAssignableFrom(value.javaClass)){
            return value as T
        }

        // TODO expects LuaValueProxy
        return engine.serializer.toObject(type, value)
    }
}
