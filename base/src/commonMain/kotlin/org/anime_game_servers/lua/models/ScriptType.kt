package org.anime_game_servers.lua.models

enum class ScriptType(
    val addDefaultGlobals: Boolean,
    val precompile: Boolean
) {
    DATA_STORAGE(false, false),
    STATIC_EXECUTABLE(false, true),
    EXECUTABLE(true, true),
    ONE_TIME_EXECUTABLE(true, false),
    INCLUDE(true, true);
}