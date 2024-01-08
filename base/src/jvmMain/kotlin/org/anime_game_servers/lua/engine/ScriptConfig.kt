package org.anime_game_servers.lua.engine

data class ScriptConfig(
    val scriptLoader: BaseScriptLoader,
    val enableIncludeWorkaround: Boolean = false,
)
