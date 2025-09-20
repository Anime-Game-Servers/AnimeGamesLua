package org.anime_game_servers.lua.models

class BooleanLuaValueJvm(private val value: Boolean) : BooleanLuaValue(value) {

    override fun <T> asObject(type: Class<T>): T? {
        return if (type == Boolean::class.java) type.cast(value) as T else null
    }
    override fun <K, V> asMap(keyType: Class<K>, valueType: Class<V>) = null
    override fun <T> asList(type: Class<T>) = null

    companion object {
        @JvmField
        val TRUE = BooleanLuaValueJvm(true)
        @JvmField
        val FALSE = BooleanLuaValueJvm(false)
    }
}
