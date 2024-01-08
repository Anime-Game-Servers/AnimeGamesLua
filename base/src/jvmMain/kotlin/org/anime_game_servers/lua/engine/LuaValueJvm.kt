package org.anime_game_servers.lua.engine

interface LuaValueJvm : LuaValue {
    fun <T> asObject(type: Class<T>): T?
}
