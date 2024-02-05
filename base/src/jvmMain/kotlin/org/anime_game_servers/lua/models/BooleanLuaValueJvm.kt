package org.anime_game_servers.lua.models

class BooleanLuaValueJvm(private val value: Boolean) : BooleanLuaValue(value) {

    override fun <T> asObject(type: Class<T>): T? {
        return if (type == Boolean::class.java) type.cast(value) as T else null
    }

    companion object {
        @JvmField
        val TRUE = BooleanLuaValueJvm(true)
        @JvmField
        val FALSE = BooleanLuaValueJvm(false)
    }
}
