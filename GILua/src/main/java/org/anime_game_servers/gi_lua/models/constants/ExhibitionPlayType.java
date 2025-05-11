package org.anime_game_servers.gi_lua.models.constants;


import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.core.base.annotations.lua.LuaStatic;

@LuaStatic
public enum ExhibitionPlayType {
    @LuaNames("Challenge")
    CHALLENGE,
    @LuaNames("Gallery")
    GALLERY,
}
