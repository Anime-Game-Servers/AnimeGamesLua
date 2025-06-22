package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable
import java.util.*

data class ActivityOpenAndCloseTime (
    val openTime: Long,
    val closeTime: Long
) {
    constructor(openTime: Date, closeTime: Date) : this(
        openTime = openTime.time,
        closeTime = closeTime.time
    )

    fun toLuaTable(targetTable: LuaTable){
        targetTable.set(1, openTime)
        targetTable.set(2, closeTime)
    }
}

/**
 * This handles script lib functions for genera activity script functions.
 * These are only callable from a group context.
 */
interface GeneralActivityScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getActivityOpenAndCloseTimeByScheduleId(context: GroupEventContext, scheduleId: Int): ActivityOpenAndCloseTime?
    fun tryRecordActivityPushTips(context: GroupEventContext, pushTipId: Int): Int
}