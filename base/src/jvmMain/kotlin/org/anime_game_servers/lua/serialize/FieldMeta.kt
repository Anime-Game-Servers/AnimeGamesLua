package org.anime_game_servers.lua.serialize

import java.lang.reflect.Field

data class FieldMeta(
    val name: String? = null,
    val setter: String? = null,
    val index: Int = 0,
    val type: Class<*>? = null,
    val field: Field? = null
)
