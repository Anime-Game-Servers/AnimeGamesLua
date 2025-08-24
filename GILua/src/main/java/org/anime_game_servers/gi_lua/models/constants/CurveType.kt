package org.anime_game_servers.gi_lua.models.constants

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.annotations.lua.LuaStatic


@LuaStatic
enum class CurveType {
    @LuaNames("None")
    DEFAULT,

    /**
     * Likely used for the 3.8 [Choo-Choo Cart](https://genshin-impact.fandom.com/wiki/Choo-Choo_Cart)
     */
    @LuaNames("RollerCoaster")
    ROLLER_COASTER,

    /**
     * These are the [Currents](https://genshin-impact.fandom.com/wiki/Current) added in Fontain
     */
    @LuaNames("OceanCurrent")
    OCEAN_CURRENT,

    /**
     * Likely the in natlan introduced [Spiritways](https://genshin-impact.fandom.com/wiki/Spiritway)
     */
    @LuaNames("ArcLight")
    ARC_LIGHT,
}
