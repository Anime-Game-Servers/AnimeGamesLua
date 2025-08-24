package org.anime_game_servers.gi_lua.models.constants

import org.anime_game_servers.core.base.annotations.lua.LuaStatic


@LuaStatic
enum class JsonClimateType {
    NORMAL,
    COLD,
    HOT,
    COLD_MONSTER,
    HOT_MONSTER,
    SEA_MIST,
    HERO_COURSE,
    SEA_MIST_SURROUNDING,
    TATARI_REGION,
    TRANS_CLIMATE,
    SEIRAI_STORM,
    TSURUMI_MIST,
    TSURUMI_THUNDER,
    TSURUMI_REGIONAL_MIST,
}
