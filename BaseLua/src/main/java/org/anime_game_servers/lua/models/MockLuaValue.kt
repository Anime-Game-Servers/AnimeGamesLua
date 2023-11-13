package org.anime_game_servers.lua.models

import org.anime_game_servers.lua.engine.LuaValue

abstract class MockLuaValue : LuaValue {
    override fun isNull() = false
    override fun isBoolean() = false
    override fun isInteger() = false
    override fun isLong() = false
    override fun isDouble() = false
    override fun isFloat() = false
    override fun isString() = false
    override fun isTable() = false
}
