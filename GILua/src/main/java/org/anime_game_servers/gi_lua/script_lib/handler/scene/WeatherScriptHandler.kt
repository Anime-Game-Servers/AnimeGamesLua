package org.anime_game_servers.gi_lua.script_lib.handler.scene

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.JsonClimateType
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

private val scriptLogger = logger {}

data class ModifyClimatePolygonParams(
    @LuaNames("climate_type")
    val climateType: JsonClimateType = NORMAL,
    @LuaNames("meter_inherit_ratio")
    val meterInheritRatio: Float = 0f,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): ModifyClimatePolygonParams? {
            return table.run {
                val climateTypeIndex = optInt("climate_type", 0)
                if (climateTypeIndex >= JsonClimateType.entries.size || climateTypeIndex < 0) {
                    scriptLogger.error { "[ModifyClimatePolygonParamTable] Invalid climate type $climateTypeIndex" }
                    return null
                }
                val climateType = JsonClimateType.entries[climateTypeIndex]
                ModifyClimatePolygonParams(
                    climateType = climateType,
                    meterInheritRatio = optFloat("meter_inherit_ratio", 0f)
                )
            }
        }
    }
}
/**
 * Handler for scriptlib functions used for the weather system.
 * These are only callable from a lua group context.
 */
interface WeatherScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun setWeatherAreaState(context: GroupEventContext, weatherAreaId: Int, openWeather: Boolean): Int
    fun enterWeatherArea(context: GroupEventContext, weatherAreaId: Int): Int
    fun modifyClimatePolygonParamTable(context: GroupEventContext, climateAreaId: Int, climateTable: ModifyClimatePolygonParams): Int
}
