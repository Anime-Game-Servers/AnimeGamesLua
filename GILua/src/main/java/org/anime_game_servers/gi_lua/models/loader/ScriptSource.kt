package org.anime_game_servers.gi_lua.models.loader

enum class ScriptSource(
    val baseDir: String,
    val needsId: Boolean
) {
    SCENE("Scene", true),
    ACTIVITY("Activity", true),
    COMMON("Common", false),
    GADGET("Gadget", false),
    SHARED_QUESTS("Quests/Shared", false),;
}