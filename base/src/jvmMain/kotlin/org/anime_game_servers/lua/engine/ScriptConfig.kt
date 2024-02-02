package org.anime_game_servers.lua.engine

data class ScriptConfig(
    val scriptLoader: BaseScriptLoader,
    val enableIncludeWorkaround: RequireMode = RequireMode.DISABLED,
)

enum class RequireMode {
    /**
     * The include/require function is disabled.
     */
    DISABLED,

    /**
     * The include/require function is enabled.
     */
    ENABLED,

    /**
     * The include/require function is enabled and the workaround is enabled.
     */
    ENABLED_WITH_WORKAROUND,
}
