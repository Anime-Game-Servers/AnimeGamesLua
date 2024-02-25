package org.anime_game_servers.jnlua_engine

import org.anime_game_servers.lua.engine.LuaTable
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream

class JNLuaTable internal constructor(table: AbstractMap<*, *>) : LuaTable {
    var table: AbstractMap<Any, Any>

    init {
        this.table = table as AbstractMap<Any, Any>
    }

    override fun has(key: String): Boolean {
        return table.containsKey(key)
    }

    override fun get(key: String): Any? {
        return table[key]
    }

    override fun get(key: Int): Any? {
        return table[key]
    }

    override fun getInt(key: String): Int {
        return (table[key] as Number).toInt()
    }

    override fun optInt(key: String, defaultValue: Int): Int {
        val value = table[key]
        return (value as? Number)?.toInt() ?: defaultValue
    }

    override fun getInt(key: Int): Int {
        return (table[key] as Number).toInt()
    }

    override fun optInt(key: Int, defaultValue: Int): Int {
        val value = table[key]
        return (value as? Number)?.toInt() ?: defaultValue
    }

    override fun set(key: Int, value: Int) {
        table[key] = value
    }

    override fun set(key: String, value: Int) {
        table[key] = value
    }

    override fun getDouble(key: String): Double {
        return (table[key] as Number).toDouble()
    }

    override fun optDouble(key: String, defaultValue: Double): Double {
        val value = table[key]
        return (value as? Number)?.toDouble() ?: defaultValue
    }

    override fun getDouble(key: Int): Double {
        return (table[key] as Number).toDouble()
    }

    override fun optDouble(key: Int, defaultValue: Double): Double {
        val value = table[key]
        return (value as? Number)?.toDouble() ?: defaultValue
    }

    override fun getFloat(key: String): Float {
        return (table[key] as Number).toFloat()
    }

    override fun optFloat(key: String, defaultValue: Float): Float {
        val value = table[key]
        return (value as? Number)?.toFloat() ?: defaultValue
    }

    override fun getFloat(key: Int): Float {
        return (table[key] as Number).toFloat()
    }

    override fun optFloat(key: Int, defaultValue: Float): Float {
        val value = table[key]
        return (value as? Number)?.toFloat() ?: defaultValue
    }

    override fun set(key: Int, value: Float) {
        table[key] = value
    }

    override fun set(key: String, value: Float) {
        table[key] = value
    }

    override fun set(key: Int, value: Double) {
        table[key] = value
    }

    override fun set(key: String, value: Double) {
        table[key] = value
    }

    override fun getBoolean(key: String): Boolean {
        return table[key] as Boolean
    }

    override fun optBoolean(key: String, defaultValue: Boolean): Boolean {
        val value = table[key]
        return value as? Boolean ?: defaultValue
    }

    override fun getBoolean(key: Int): Boolean {
        return table[key] as Boolean
    }

    override fun optBoolean(key: Int, defaultValue: Boolean): Boolean {
        val value = table[key]
        return value as? Boolean ?: defaultValue
    }

    override fun set(key: Int, value: Boolean) {
        table[key] = value
    }

    override fun set(key: String, value: Boolean) {
        table[key] = value
    }

    override fun getString(key: String): String {
        return table[key] as String
    }

    override fun optString(key: String, defaultValue: String?): String? {
        val value = table[key]
        return value as? String ?: defaultValue
    }

    override fun getString(key: Int): String {
        return table[key] as String
    }

    override fun optString(key: Int, defaultValue: String?): String? {
        val value = table[key]
        return value as? String ?: defaultValue
    }

    override fun set(key: Int, value: String) {
        table[key] = value
    }

    override fun set(key: String, value: String) {
        table[key] = value
    }

    override fun getTable(key: String): LuaTable? {
        return (table[key] as? AbstractMap<*, *>?)?.let { JNLuaTable(it) }
    }

    override fun getTable(key: Int): LuaTable? {
        return (table[key] as? AbstractMap<*, *>?)?.let { JNLuaTable(it) }
    }

    override fun set(key: Int, value: LuaTable) {
        table[key] = value.getRawTable()
    }

    override fun set(key: String, value: LuaTable) {
        table[key] = value.getRawTable()
    }

    override fun getKeys(): Set<String> {
        //TODO check if this is correct
        return table.keys.let {
            it as? Set<String>
        } ?: IntStream.rangeClosed(1, table.size).mapToObj { i: Int -> java.lang.String.valueOf(i) }
            .collect(Collectors.toSet())
    }

    override fun getAsIntArray(): IntArray {
        val result = IntArray(table.size)
        for (i in 0 until table.size) {
            result[i] = (table[i + 1] as Int)
        }
        return result
    }

    override fun getSize(): Int {
        return table.size
    }

    override fun getRawTable(): Any {
        return table
    }
}
