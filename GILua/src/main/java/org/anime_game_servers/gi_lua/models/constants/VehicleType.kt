package org.anime_game_servers.gi_lua.models.constants

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.annotations.lua.LuaStatic


@LuaStatic
enum class VehicleType {
    @LuaNames("None")
    NONE,

    /**
     * Unknown usecase, probably unused
     */
    @LuaNames("Ship")
    SHIP,

    /**
     * The skiff is the [Waverider](https://genshin-impact.fandom.com/wiki/Waverider)
     */
    @LuaNames("Skiff", "Waverider")
    SKIFF,

    /**
     * [Sorush](https://genshin-impact.fandom.com/wiki/Sorush)
     */
    @LuaNames("Sorush")
    SORUSH,

    /**
     * Vehicle is a saurian, that can be taken over by the player for the [Saurian Indwelling](https://genshin-impact.fandom.com/wiki/Saurian_Indwelling) mechanic
     */
    @LuaNames("Natsaurus", "Saurian")
    NATSAURUS
}
