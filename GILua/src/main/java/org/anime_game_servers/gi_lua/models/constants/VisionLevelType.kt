package org.anime_game_servers.gi_lua.models.constants

import org.anime_game_servers.core.base.annotations.lua.LuaStatic
import org.anime_game_servers.core.base.interfaces.IntValueEnum

@LuaStatic
enum class VisionLevelType(private val value: Int) : IntValueEnum {
    VISION_LEVEL_NORMAL(0),
    VISION_LEVEL_LITTLE_REMOTE(1),
    VISION_LEVEL_REMOTE(2),
    VISION_LEVEL_SUPER(3),
    VISION_LEVEL_NEARBY(4),
    VISION_LEVEL_SUPER_NEARBY(5),
    VISION_LEVEL_SUPER_NUM(6);

    override fun getValue() = value
    companion object {
        @JvmStatic
        fun getDefault() = VISION_LEVEL_NORMAL
    }
}
