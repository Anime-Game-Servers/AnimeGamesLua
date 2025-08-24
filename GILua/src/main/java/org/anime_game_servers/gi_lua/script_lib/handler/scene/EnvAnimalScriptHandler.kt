package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

enum class EnvAnimalType{
    NONE,
    PAUSE,
    ACTIVE
}

/**
 * Handler for scriptlib functions related to [leyline blossoms](https://genshin-impact.fandom.com/wiki/Ley_Line_Blossom)
 * These are only callable from a lua group context.
 */
interface EnvAnimalScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun switchSceneEnvAnimal(context: GroupEventContext, type: EnvAnimalType): Int
}
