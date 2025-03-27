package org.anime_game_servers.gi_lua.models.constants;


import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.core.base.annotations.lua.LuaStatic;

@LuaStatic
public enum MultistagePlayType {
    @LuaNames("None")
    NONE,
    @LuaNames("Mechanicus")
    MECHANICUS,
    @LuaNames("FleurFair")
    FLEUR_FAIR,
    @LuaNames("HideAndSeek")
    HIDE_AND_SEEK,
    @LuaNames("BounceConjuring")
    BOUNCE_CONJURING,
    @LuaNames("Chess")
    CHESS,
    @LuaNames("IrodoriChess")
    IRODORI_CHESS,
    @LuaNames("CharAmusement")
    CHAR_AMUSEMENT,
    @LuaNames("BrickBreaker")
    BRICK_BREAKER,
    @LuaNames("CoinCollect")
    COIN_COLLECT,
    @LuaNames("LanV3Boat")
    LAN_V3_BOAT
}
