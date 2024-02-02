package org.anime_game_servers.lua.models

class MutableBoolean(private var value: Boolean){
    fun getValue(): Boolean {
        return value
    }

    fun setValue(value: Boolean) {
        this.value = value
    }
}
