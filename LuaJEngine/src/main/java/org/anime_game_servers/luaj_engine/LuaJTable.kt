package org.anime_game_servers.luaj_engine

import org.anime_game_servers.lua.engine.LuaTable
import org.luaj.vm2.LuaValue

class LuaJTable internal constructor(private val engine: LuaJEngine, private val table: org.luaj.vm2.LuaTable) :
    LuaTable {
    override fun has(key: String): Boolean {
        return !table.get(key).isnil()
    }

    override fun get(key: String): Any {
        return LuaJValue(engine, table.get(key))
    }

    override fun get(key: Int): Any {
        return LuaJValue(engine, table.get(key))
    }

    override fun getInt(key: String): Int {
        return table.get(key).checkint()
    }

    override fun optInt(key: String, defaultValue: Int): Int {
        return table.get(key).optint(defaultValue)
    }

    override fun getInt(key: Int): Int {
        return table.get(key).checkint()
    }

    override fun optInt(key: Int, defaultValue: Int): Int {
        return table.get(key).optint(defaultValue)
    }

    override fun set(key: Int, value: Int) {
        table.set(key, LuaValue.valueOf(value))
    }

    override fun set(key: String, value: Int) {
        table.set(key, LuaValue.valueOf(value))
    }

    override fun getLong(key: String): Long {
        return table.get(key).checklong()
    }

    override fun optLong(key: String, defaultValue: Long): Long {
        return table.get(key).optlong(defaultValue)
    }

    override fun getLong(key: Int): Long {
        return table.get(key).checklong()
    }

    override fun optLong(key: Int, defaultValue: Long): Long {
        return table.get(key).optlong(defaultValue)
    }

    override fun set(key: Int, value: Long) {
        table.set(key, LuaValue.valueOf(value.toDouble()))
    }

    override fun set(key: String, value: Long) {
        table.set(key, LuaValue.valueOf(value.toDouble()))
    }

    override fun getDouble(key: String): Double {
        return table.get(key).checkdouble()
    }

    override fun optDouble(key: String, defaultValue: Double): Double {
        return table.get(key).optdouble(defaultValue)
    }

    override fun getDouble(key: Int): Double {
        return table.get(key).checkdouble()
    }

    override fun optDouble(key: Int, defaultValue: Double): Double {
        return table.get(key).optdouble(defaultValue)
    }

    override fun getFloat(key: String): Float {
        return table.get(key).tofloat()
    }

    override fun optFloat(key: String, defaultValue: Float): Float {
        return table.get(key).optdouble(defaultValue.toDouble()).toFloat()
    }

    override fun getFloat(key: Int): Float {
        return table.get(key).tofloat()
    }

    override fun optFloat(key: Int, defaultValue: Float): Float {
        return table.get(key).optdouble(defaultValue.toDouble()).toFloat()
    }

    override fun set(key: Int, value: Double) {
        table.set(key, LuaValue.valueOf(value))
    }

    override fun set(key: String, value: Double) {
        table.set(key, LuaValue.valueOf(value))
    }

    override fun set(key: Int, value: Float) {
        table.set(key, LuaValue.valueOf(value.toDouble()))
    }

    override fun set(key: String, value: Float) {
        table.set(key, LuaValue.valueOf(value.toDouble()))
    }

    override fun getBoolean(key: String): Boolean {
        return table.get(key).toboolean()
    }

    override fun optBoolean(key: String, defaultValue: Boolean): Boolean {
        return table.get(key).optboolean(defaultValue)
    }

    override fun getBoolean(key: Int): Boolean {
        return table.get(key).toboolean()
    }

    override fun optBoolean(key: Int, defaultValue: Boolean): Boolean {
        return table.get(key).optboolean(defaultValue)
    }

    override fun set(key: Int, value: Boolean) {
        table.set(key, LuaValue.valueOf(value))
    }

    override fun set(key: String, value: Boolean) {
        table.set(key, LuaValue.valueOf(value))
    }

    override fun getString(key: String): String? {
        return table.get(key).tojstring()
    }

    override fun optString(key: String, defaultValue: String?): String? {
        return table.get(key).optjstring(defaultValue)
    }

    override fun getString(key: Int): String {
        return table.get(key).toString()
    }

    override fun optString(key: Int, defaultValue: String?): String? {
        return table.get(key).optjstring(defaultValue)
    }

    override fun set(key: Int, value: String) {
        table.set(key, LuaValue.valueOf(value))
    }

    override fun set(key: String, value: String) {
        table.set(key, LuaValue.valueOf(value))
    }

    override fun getTable(key: String): LuaTable? {
        val variable = table.get(key)
        if (!variable.istable()) {
            return null
        }
        return LuaJTable(engine, variable.checktable())
    }

    override fun getTable(key: Int): LuaTable? {
        val variable = table.get(key)
        if (!variable.istable()) {
            return null
        }
        return LuaJTable(engine, variable.checktable())
    }

    override fun set(key: Int, value: LuaTable) {
        val rawTable: Any = value.getRawTable()
        if (rawTable is org.luaj.vm2.LuaTable) table.set(key, rawTable)
    }

    override fun set(key: String, value: LuaTable) {
        val rawTable: Any = value.getRawTable()
        if (rawTable is org.luaj.vm2.LuaTable) table.set(key, rawTable)
    }

    override fun getKeys(): Set<String> {
        return table.keys().map { it.tojstring() }.toSet()
    }

    override fun getAsIntArray(): IntArray {
        val array = IntArray(table.length())
        for (i in array.indices) {
            array[i] = table.get(i + 1).optint(0)
        }
        return array
    }

    override fun getAsFloatArray(): FloatArray {
        val array = FloatArray(table.length())
        for (i in array.indices) {
            array[i] = table.get(i + 1).optdouble(0.0).toFloat()
        }
        return array
    }

    override fun getAsDoubleArray(): DoubleArray {
        val array = DoubleArray(table.length())
        for (i in array.indices) {
            array[i] = table.get(i + 1).optdouble(0.0)
        }
        return array
    }

    override fun getAsStringArray(): Array<String> {
        val result = mutableListOf<String>()
        for (i in 0 until table.length()) {
            result.add(i, table.get(i + 1).optjstring(""))
        }
        return result.toTypedArray()
    }

    override fun getSize(): Int {
        return table.length()
    }

    override fun getRawTable(): Any {
        return table
    }
}
