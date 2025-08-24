package org.anime_game_servers.gi_lua.models.constants

import org.anime_game_servers.core.base.annotations.lua.LuaStatic


@LuaStatic
enum class FollowType {
    FOLLOW_TYPE_INIT_FOLLOW_POS,
    FOLLOW_TYPE_SET_FOLLOW_POS,
    FOLLOW_TYPE_SET_ABS_FOLLOW_POS,
}
