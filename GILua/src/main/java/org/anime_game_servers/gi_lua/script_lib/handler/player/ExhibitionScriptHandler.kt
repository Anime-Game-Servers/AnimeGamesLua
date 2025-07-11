package org.anime_game_servers.gi_lua.script_lib.handler.player

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.gi_lua.models.constants.ExhibitionPlayType
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.script_lib.ScriptLib
import org.anime_game_servers.lua.engine.LuaTable

private val scriptLogger = logger {}

data class ExhibitionPlayTarget(
    val playType: ExhibitionPlayType,
    val galleryId: Int
) {
    companion object {
        fun fromLuaTable(targetPlayInfoTable: LuaTable): ExhibitionPlayTarget? {
            val exhibitionTypeIndex = targetPlayInfoTable.optInt("play_type", -1)
            val galleryId = targetPlayInfoTable.optInt("gallery_id", -1)
            if (exhibitionTypeIndex < 0 || exhibitionTypeIndex >= ExhibitionPlayType.entries.size) {
                scriptLogger.error { "[AddExhibitionAccumulableDataAfterSuccess] Invalid exhibition type $exhibitionTypeIndex" }
                return null
            }
            if (galleryId == -1) {
                scriptLogger.error { "[AddExhibitionAccumulableDataAfterSuccess] Invalid gallery id $galleryId" }
                return null
            }
            val exhibitionTypeEnum = ExhibitionPlayType.entries[exhibitionTypeIndex]
            return ExhibitionPlayTarget(exhibitionTypeEnum, galleryId)
        }
    }
}

/**
 * Handler for scriptlib functions for handling exhibition data. This seems to be used in dungeon and general gallery contexts.
 * These are only callable from a lua group context.
 */
interface ExhibitionScriptHandler<GroupEventContext : GroupEventLuaContext> {
    /**
     * @param context
     * @param uid
     * @param dataKey key under which the data is stored
     * @param value value to add
     * @return
     */
    fun addExhibitionAccumulableData(context: GroupEventContext, uid: Int, dataKey: String, value: Int): Int


    fun addExhibitionAccumulableDataAfterSuccess(
        context: GroupEventContext,
        uid: Int,
        dataKey: String,
        value: Int,
        targetPlayInfo: ExhibitionPlayTarget
    ): Int

    fun getExhibitionAccumulableData(context: GroupEventContext, uid: Int, exhibitionId: Int): Int


    /**
     * @param context
     * @param uid
     * @param dataKey key under which the data is stored
     * @param value value to add
     * @return
     */
    fun addExhibitionReplaceableData(context: GroupEventContext, uid: Int, dataKey: String, value: Int): Int

    /**
     * @param context
     * @param uid
     * @param dataKey key under which the data is stored
     * @param value value to add
     * @param targetPlayInfo info about the play that should succeed to add this data
     * @return
     */
    fun addExhibitionReplaceableDataAfterSuccess(
        context: GroupEventContext,
        uid: Int,
        dataKey: String,
        value: Int,
        targetPlayInfo: ExhibitionPlayTarget
    ): Int

    fun clearExhibitionReplaceableData(context: GroupEventContext, uid: Int, dataKey: String): Int
    fun getExhibitionReplaceableData(context: GroupEventContext, uid: Int, exhibitionId: Int): Int
}