package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

data class RefreshBlossomGroupParams(
    @field:LuaNames("group_id")
    val groupId: Int,
    @field:LuaNames("suite_id")
    val suiteId: Int,
    @field:LuaNames("exclude_prev")
    val excludePrev: Boolean,
    @field:LuaNames("is_delay_unload")
    val isDelayUnload: Boolean,
){
    companion object {
        fun fromLuaTable(table: LuaTable): RefreshBlossomGroupParams? {
            return RefreshBlossomGroupParams(
                groupId = table.optInt("group_id", 0),
                suiteId = table.optInt("group_id", -1),
                excludePrev = table.optBoolean("exclude_prev", false),
                isDelayUnload = table.optBoolean("is_delay_unload", false),
            )
        }
    }
}

/**
 * Handler for scriptlib functions related to [leyline blossoms](https://genshin-impact.fandom.com/wiki/Ley_Line_Blossom)
 * These are only callable from a lua group context.
 */
interface BlossomScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun createBlossomChestByGroupId(context: GroupEventContext, groupId: Int, chestConfigId: Int): Int
    fun getBlossomScheduleStateByGroupId(context: GroupEventContext, groupId: Int): Int
    fun setBlossomScheduleStateByGroupId(context: GroupEventContext, groupId: Int, state: Int): Int
    fun addBlossomScheduleProgressByGroupId(context: GroupEventContext, groupId: Int): Int
    fun refreshBlossomGroup(context: GroupEventContext, params: RefreshBlossomGroupParams): Int
    fun refreshBlossomDropRewardByGroupId(context: GroupEventContext, groupId: Int): Int
    fun getBlossomRefreshTypeByGroupId(context: GroupEventContext, groupId: Int): Int
}
