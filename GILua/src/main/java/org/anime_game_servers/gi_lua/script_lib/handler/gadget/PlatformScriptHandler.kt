package org.anime_game_servers.gi_lua.script_lib.handler.gadget

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

private val scriptLogger = logger {}

enum class RouteRecordMode {
    @LuaNames("None")
    NONE,

    @LuaNames("Prereach")
    PREREACH,

    @LuaNames("Reach")
    REACH
}

enum class RouteType {
    @LuaNames("OneWay")
    ONE_WAY,

    @LuaNames("Reciprocate")
    RECIPROCATE,

    @LuaNames("Loop")
    LOOP;
}

data class SetPlatformPointArrayParams(
    @LuaNames("route_type")
    val routeType: RouteType?,
    @LuaNames("turn_mode")
    val turnMode: Boolean = false,
    @LuaNames("record_mode")
    val recordMode: RouteRecordMode = NONE,
    @LuaNames("speed_level")
    val speedLevel: Int = 0,
    @LuaNames("arrive_range")
    val arriveRange: Float,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): SetPlatformPointArrayParams? {
            val routeTypeId = table.optInt("route_type", 0)
            if (routeTypeId < 0 || routeTypeId >= RouteType.entries.size) {
                scriptLogger.error { "[SetPlatformPointArrayParams] Invalid route type $routeTypeId" }
                return null
            }
            val recordModeId = table.optInt("record_mode", 0)
            if (recordModeId < 0 || recordModeId >= RouteRecordMode.entries.size) {
                scriptLogger.error { "[SetPlatformPointArrayParams] Invalid record type $recordModeId" }
                return null
            }

            val routeType = RouteType.entries[routeTypeId]
            val recordType = RouteRecordMode.entries[recordModeId]
            val turnMode = table.optBoolean("turn_mode", false)
            val speedLevel = table.optInt("speed_level", 0)
            val arriveRange = table.optFloat("arrive_range", 0f)
            return SetPlatformPointArrayParams(routeType, turnMode, recordType, speedLevel, arriveRange)
        }
    }
}

data class PlatformArrayInfo(
    val result: Int,
    val rot: Vector,
    val pos: Vector
)

/**
 * This handles script lib functions for handling platform gadgets.
 * These are only callable from a group context.
 */
interface PlatformScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getPlatformArrayInfoByPointId(context: GroupEventContext, arrayId: Int, pointId: Int): PlatformArrayInfo
    fun getPlatformPointArray(context: GroupEventContext, configId: Int): List<Int>
    fun setPlatformRouteIndexToNext(context: GroupEventContext, configId: Int): Int
    // Official seems to also support calling this for monsters
    fun setPlatformPointArray(
        context: GroupEventContext,
        entityConfigId: Int,
        pointArrayId: Int,
        pointList: List<Int>,
        params: SetPlatformPointArrayParams
    ): Int

    fun setPlatformRouteId(context: GroupEventContext, entityConfigId: Int, routeId: Int): Int
    fun startPlatform(context: GroupEventContext, configId: Int): Int
    fun stopPlatform(context: GroupEventContext, configId: Int): Int
}