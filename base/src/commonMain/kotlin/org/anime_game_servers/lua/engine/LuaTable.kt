package org.anime_game_servers.lua.engine

/**
 * Interface for the abstraction of lua tables, to allow working with different engines.
 * Working with the key getter, its always the lua index (so starting with 1 for int indexes),
 * When converting to an array, the array will end up as normal 0 indexed array
 */
interface LuaTable {
    fun has(key: String): Boolean

    fun get(key: String): Any?

    fun get(key: Int): Any?

    fun getInt(key: String): Int

    fun optInt(key: String, defaultValue: Int): Int

    fun getInt(key: Int): Int

    fun optInt(key: Int, defaultValue: Int): Int

    operator fun set(key: Int, value: Int)
    operator fun set(key: String, value: Int)

    fun getLong(key: String): Long

    fun optLong(key: String, defaultValue: Long): Long

    fun getLong(key: Int): Long

    fun optLong(key: Int, defaultValue: Long): Long

    operator fun set(key: Int, value: Long)

    operator fun set(key: String, value: Long)

    fun getDouble(key: String): Double

    fun optDouble(key: String, defaultValue: Double): Double

    fun getDouble(key: Int): Double

    fun optDouble(key: Int, defaultValue: Double): Double

    operator fun set(key: Int, value: Double)

    operator fun set(key: String, value: Double)

    fun getFloat(key: String): Float

    fun optFloat(key: String, defaultValue: Float): Float

    fun getFloat(key: Int): Float

    fun optFloat(key: Int, defaultValue: Float): Float

    operator fun set(key: Int, value: Float)

    operator fun set(key: String, value: Float)

    fun getBoolean(key: String): Boolean

    fun optBoolean(key: String, defaultValue: Boolean): Boolean

    fun getBoolean(key: Int): Boolean

    fun optBoolean(key: Int, defaultValue: Boolean): Boolean

    operator fun set(key: Int, value: Boolean)

    operator fun set(key: String, value: Boolean)

    fun getString(key: String): String?

    fun optString(key: String, defaultValue: String?): String?

    fun getString(key: Int): String?

    fun optString(key: Int, defaultValue: String?): String?

    operator fun set(key: Int, value: String)

    operator fun set(key: String, value: String)

    fun getTable(key: String): LuaTable?

    fun getTable(key: Int): LuaTable?

    operator fun set(key: Int, value: LuaTable)

    operator fun set(key: String, value: LuaTable)

    fun getKeys(): Set<String>

    fun getAsIntArray(): IntArray
    fun getAsFloatArray(): FloatArray
    fun getAsDoubleArray(): DoubleArray
    fun getAsStringArray(): Array<String>
    fun <T> asObject(type: Class<T>): T?

    fun getSize(): Int

    fun getRawTable(): Any
}
