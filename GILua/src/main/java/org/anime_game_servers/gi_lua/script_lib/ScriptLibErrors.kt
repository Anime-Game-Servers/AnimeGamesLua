package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.core.base.interfaces.IntValueEnum

enum class ScriptLibErrors(private val _value:Int): IntValueEnum {
    NOT_IMPLEMENTED(-100),
    INVALID_PARAMETER(-101),
    INVALID_PARAMETER_TABLE_CONTENT(-102),
    ;

    override fun getValue() = _value
}