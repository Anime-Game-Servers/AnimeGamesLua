package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext


/**
 * Handler for scriptlib functions related to different timer systems in lua, including
 * the time axes and group timers.
 * Both can be used to run tasks at specific intervals or run something delayed, but
 * TimeAxis allows more granular control, with things like multiple different intervals, pausing and resuming
 * Group timers are using the EVENT_TIMER_EVENT event for reaction, while Timeaxes use the  EVENT_TIME_AXIS_PASS event.
 * These are only callable from a lua group context.
 */
interface TimersScriptHandler<GroupEventContext : GroupEventLuaContext> {
    // group timers
    fun createGroupTimerEvent(context: GroupEventContext, groupID: Int, source: String, time: Double): Int
    fun cancelGroupTimerEvent(context: GroupEventContext, groupID: Int, source: String): Int

    // Time axis
    fun endAllTimeAxis(context: GroupEventContext): Int
    fun continueTimeAxis(context: GroupEventContext, timeAxisKey: String): Int
    fun pauseTimeAxis(context: GroupEventContext, timeAxisKey: String): Int
    fun initTimeAxis(
        context: GroupEventContext,
        timeAxisKey: String,
        timers: List<Float>,
        loop: Boolean
    ): Int

    fun endTimeAxis(context: GroupEventContext, timeAxisKey: String): Int
}
