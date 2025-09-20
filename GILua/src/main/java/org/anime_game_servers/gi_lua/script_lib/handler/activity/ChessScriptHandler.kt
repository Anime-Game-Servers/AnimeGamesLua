package org.anime_game_servers.gi_lua.script_lib.handler.activity

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

data class ChessPoolInfo(
    @field:LuaNames("pool_id")
    val poolId: Int,
    @field:LuaNames("entrance_point_id")
    val entrancePointId: Int,
)

data class ChessRoundInfo(
    @field:LuaNames("round_id")
    val roundId: Int,
    @field:LuaNames("pool_info_list")
    val poolInfoList: List<ChessPoolInfo>,
)

data class ChessPreviewInfo(
    @field:LuaNames("entrance_point_map")
    val entrancePointMap: Map<Int, Int>,
    @field:LuaNames("exit_point_id_list")
    val exitPointIdList: List<Int>,
    @field:LuaNames("entrance_detail_list")
    val entranceDetailList: List<ChessRoundInfo>,
    @field:LuaNames("ban_card_tag_list")
    val banCardTagList: List<Int>?,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): ChessPreviewInfo? {
            val entrancePointMapTable = table.getTable("entrance_point_map") ?: return null
            val entranceDetailListTable = table.getTable("entrance_detail_list") ?: return null
            val exitPointIdList = table.getTable("exit_point_id_list")?.getAsIntArray()?.toList() ?: return null
            val banCardTagList = table.getTable("ban_card_tag_list")?.getAsIntArray()?.toList()


            val keys = entrancePointMapTable.getKeys().map { it.toInt() }
            val entrancePointMap = keys.associateWith { entrancePointMapTable.getInt(it) }

            val entranceDetailList = mutableListOf<ChessRoundInfo>()
            for(i in 1..entranceDetailListTable.getSize()) {
                val roundInfoTable = entranceDetailListTable.getTable(i) ?: continue
                val roundId = roundInfoTable.optInt("round_id", -1)
                val poolInfoList = mutableListOf<ChessPoolInfo>()
                val poolInfoListTable = roundInfoTable.getTable("pool_info_list") ?: continue
                for(j in 1..poolInfoListTable.getSize()) {
                    val poolInfoTable = poolInfoListTable.getTable(j) ?: continue
                    val poolId = poolInfoTable.optInt("pool_id", -1)
                    val entrancePointId = poolInfoTable.optInt("entrance_point_id", -1)
                    poolInfoList.add(ChessPoolInfo(poolId, entrancePointId))
                }
                entranceDetailList.add(ChessRoundInfo(roundId, poolInfoList))
            }

            return ChessPreviewInfo(entrancePointMap, exitPointIdList, entranceDetailList, banCardTagList)
        }
    }
}

/**
 * This handles script lib functions that are specific to the Chess activity.
 * These are only callable from a group context.
 */
interface ChessScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun addChessBuildingPoints(
        context: GroupEventContext,
        groupId: Int,
        playIndex: Int,
        uid: Int,
        pointsToAdd: Int
    ): Int

    fun getChessMonsterPoolIdVecByRound(context: GroupEventContext, groupId: Int, playIndex: Int, waveNumber: Int): List<Int>?
    fun setChessMystery(context: GroupEventContext, groupId: Int, playIndex: Int, chessPreviewInfo: ChessPreviewInfo): Int
}