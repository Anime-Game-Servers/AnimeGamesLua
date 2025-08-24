package org.anime_game_servers.luaj_engine

import org.luaj.vm2.LuaValue

class LuaJValue internal constructor(private val engine: LuaJEngine, private val value: LuaValue) :
    org.anime_game_servers.lua.engine.LuaValue {
    override fun isNull(): Boolean {
        return value.isnil()
    }

    override fun isBoolean(): Boolean {
        return value.isboolean()
    }

    override fun isInteger(): Boolean {
        return value.isint()
    }

    override fun isLong(): Boolean {
        return value.islong()
    }

    override fun isDouble(): Boolean {
        return value.isnumber()
    }

    override fun isFloat(): Boolean {
        return value.isnumber()
    }

    override fun isString(): Boolean {
        return value.isstring()
    }

    override fun isTable(): Boolean {
        return value.istable() || value.isuserdata()
    }

    override fun asBoolean(): Boolean {
        return value.toboolean()
    }

    override fun asInteger(): Int {
        return value.toint()
    }

    override fun asLong(): Long {
        return value.tolong()
    }

    override fun asDouble(): Double {
        return value.todouble()
    }

    override fun asFloat(): Float {
        return value.tofloat()
    }

    override fun asString(): String {
        return value.toString()
    }

    override fun <T> asObject(type: Class<T>): T? {
        if (!value.istable() && !value.isuserdata()) {
            return null
        }

        if (value.isuserdata()) {
            return value.checkuserdata(type) as T?
        }

        return engine.serializer.toObject<T?>(type, value.checktable())
    }
}
