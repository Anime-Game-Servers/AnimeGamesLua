package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.models.scene.group.SceneGroup

interface GroupEventLuaContext : LuaContext {
    fun getGroupInstance(): SceneGroup
    fun getArgs(): ScriptArgs


    // fields used by some scripts
    val uid: Int
    @LuaNames("source_entity_id")
    val sourceEntityId: Int
    @LuaNames("target_entity_id")
    val targetEntityId: Int
    /*override fun uid() = getArgs().uid
    override fun source_entity_id() = getArgs().source_eid
    override fun target_entity_id() = getArgs().target_eid*/

    fun <T: GroupEventLuaContext> getScriptLibHandler(): ScriptLibHandler<T>?
    fun <T: GroupEventLuaContext> getScriptLibHandlerProvider(): ScriptLibGroupHandlerProvider<T>

}