package org.anime_game_servers.lua.engine

interface LuaValue {
    fun isNull() : Boolean
    fun isBoolean() : Boolean
    fun isInteger() : Boolean
    fun isLong() : Boolean
    fun isDouble() : Boolean
    fun isFloat() : Boolean
    fun isString() : Boolean
    fun isTable() : Boolean
    fun asBoolean(): Boolean
    fun asInteger(): Int
    fun asLong(): Long
    fun asDouble(): Double
    fun asFloat(): Float
    fun asString(): String?
}
