package org.anime_game_servers.gi_lua.models.scene.group

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.interfaces.IntKey
import org.anime_game_servers.gi_lua.models.scene.SceneMeta

data class SceneTrigger (
    val name: String? = null,

    @field:LuaNames("config_id")
    val configId: Int = 0,
    val event: Int = 0,

    @field:LuaNames("trigger_count")
    val triggerCount: Int = 1,
    val source: String? = null,
    val condition: String? = null,
    val action: String? = null,
    val tag: String? = null,

    @field:LuaNames("tlog_tag")
    val tlogTag: String? = null,

    @field:LuaNames("forbid_guest")
    val forbidGuest: Boolean = true,

    // not directly part of the lua table
    @Transient
    var groupId: Int = 0,

    @Transient
    var blockId: Int = 0,

    @Transient
    var sceneMeta: SceneMeta? = null,
): IntKey {

    override fun getIntKey() = configId

    companion object {
        /**
         * When the trigger count is set to this, it only gets unregistered on a return of != 0 from the trigger function.
         */
        const val INF_TRIGGERS: Int = 0
    }
}
