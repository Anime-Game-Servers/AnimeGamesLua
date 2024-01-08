package org.anime_game_servers.lua.models

import org.anime_game_servers.lua.engine.LuaValueJvm

class IntLuaValueJvm(value: Int) : IntLuaValue(value), LuaValueJvm {
    override fun <T> asObject(type: Class<T>): T? {
        return if (type == Number::class.java) type.cast(value) as T? else null
    }

    companion object {
        @JvmField
        val ZERO = IntLuaValueJvm(0)
        @JvmField
        val ONE = IntLuaValueJvm(1)
        @JvmField
        val N_ONE = IntLuaValueJvm(-1)
    }
}
