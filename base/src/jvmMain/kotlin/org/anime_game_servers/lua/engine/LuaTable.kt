package org.anime_game_servers.lua.engine

interface LuaTable {
    fun has(key: String): Boolean

    fun get(key: String): Any?

    fun get(key: Int): Any?

    fun getInt(key: String): Int

    fun optInt(key: String, defaultValue: Int): Int

    fun getInt(key: Int): Int

    fun optInt(key: Int, defaultValue: Int): Int

    fun set(key: Int, value: Int)

    fun set(key: String, value: Int)

    fun getDouble(key: String): Double

    fun optDouble(key: String, defaultValue: Double): Double

    fun getDouble(key: Int): Double

    fun optDouble(key: Int, defaultValue: Double): Double

    fun getFloat(key: String): Float

    fun optFloat(key: String, defaultValue: Float): Float

    fun getFloat(key: Int): Float

    fun optFloat(key: Int, defaultValue: Float): Float

    fun set(key: Int, value: Double)

    fun set(key: String, value: Double)

    fun getBoolean(key: String): Boolean

    fun optBoolean(key: String, defaultValue: Boolean): Boolean

    fun getBoolean(key: Int): Boolean

    fun optBoolean(key: Int, defaultValue: Boolean): Boolean

    fun set(key: Int, value: Boolean)

    fun set(key: String, value: Boolean)

    fun getString(key: String): String?

    fun optString(key: String, defaultValue: String?): String?

    fun getString(key: Int): String?

    fun optString(key: Int, defaultValue: String?): String?

    fun set(key: Int, value: String)

    fun set(key: String, value: String)

    fun getTable(key: String): LuaTable?

    fun getTable(key: Int): LuaTable?

    fun set(key: Int, value: LuaTable)

    fun set(key: String, value: LuaTable)

    fun getKeys(): Set<String?>

    fun getAsIntArray(): IntArray

    fun getSize(): Int

    fun getRawTable(): Any?
}
