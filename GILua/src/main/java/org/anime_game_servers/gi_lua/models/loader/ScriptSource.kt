package org.anime_game_servers.gi_lua.models.loader

enum class ScriptSource(
    val baseDir: String,
    val needsId: Boolean
) {
    SCENE("Scene", true),
    SCENE_REPLACEMENT("Scene", false),
    ACTIVITY("Activity", true),
    COMMON("Common", false),
    GADGET("Gadget", false),
    SHARED_QUESTS("Quest/Share", false),;
}