package org.anime_game_servers.gi_lua.script_lib.handler.other

import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext

data class GameTime(
    val hour: Int,
    val minutes: Int
)

/**
 * Handler for time related scriptlib functions
 * These are only callable from a lua group context.
 */
interface TimeScriptHandler<GroupEventContext : GroupEventLuaContext> {

    /* Server Time */
    fun getServerTime(context: GroupEventContext): Long
    fun getServerTimeByWeek(context: GroupEventContext): Long

    /* game Time */
    fun getGameHour(context: GroupEventContext): Int
    fun getGameTimePassed(context: GroupEventContext): GameTime

    // sends PlayerGameTimeByLuaNotify after game time update
    fun skipTeyvatTime(context: GroupEventContext, time: Int, rate: Int): Int

    /* SceneTime */
    fun getSceneTimeSeconds(context: GroupEventContext): Int
}
