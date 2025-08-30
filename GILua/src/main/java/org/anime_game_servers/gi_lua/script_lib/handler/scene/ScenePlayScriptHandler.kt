package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.MultistagePlayType
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

data class PrestartScenePlayBattleParams(
    var duration: Int,
    @LuaNames("start_cd")
    var startCd: Int,
    @LuaNames("progres_stage")
    var progresStage: List<Int>,
    @LuaNames("group_id")
    var groupId: Int,
    var mode: Int = 0,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): PrestartScenePlayBattleParams? {
            val duration = table.optInt("duration", 0)
            val startCd = table.optInt("start_cd", 0)
            val progresStage = table.getTable("progres_stage")?.getAsIntArray() ?: return null
            val groupId = table.optInt("group_id", 0)
            val mode = table.optInt("mode", 0)

            return PrestartScenePlayBattleParams(
                duration = duration,
                startCd = startCd,
                progresStage = progresStage.toList(),
                groupId = groupId,
                mode = mode
            )
        }
    }
}

data class InitSceneMultistagePlayParams(
    val rounds: Int,
    @LuaNames("init_building_points")
    val initBuildingPoints: Int,
    @LuaNames("ban_card_tag_list")
    val banCardTagList: List<Int>? = null,
    @LuaNames("gallery_stage_count")
    val galleryStageCount: Int,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): InitSceneMultistagePlayParams? {
            val rounds = table.optInt("rounds", 0)
            val initBuildingPoints = table.optInt("init_building_points", 0)
            val banCardTagList = table.getTable("ban_card_tag_list")?.getAsIntArray()
            val galleryStageCount = table.optInt("gallery_stage_count", 0)

            return InitSceneMultistagePlayParams(
                rounds = rounds,
                initBuildingPoints = initBuildingPoints,
                banCardTagList = banCardTagList?.toList(),
                galleryStageCount = galleryStageCount
            )
        }
    }
}

data class SetSceneMultiStagePlayValuesParams(
    var round: Int,
    var rounds: Int,
    @LuaNames("left_monsters")
    var leftMonsters: Int,
    @LuaNames("max_escapable_monsters")
    var maxEscapableMonsters: Int,
    @LuaNames("escaped_monsters")
    var escapedMonsters: Int,
    @LuaNames("stage_bonus_building_points")
    var stageBonusBuildingPoints: Int,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): SetSceneMultiStagePlayValuesParams? {
            val round = table.optInt("round", 0)
            val rounds = table.optInt("rounds", 0)
            val leftMonsters = table.optInt("left_monsters", 0)
            val maxEscapableMonsters = table.optInt("max_escapable_monsters", 0)
            val escapedMonsters = table.optInt("escaped_monsters", 0)
            val stageBonusBuildingPoints = table.optInt("stage_bonus_building_points", 0)

            return SetSceneMultiStagePlayValuesParams(
                round = round,
                rounds = rounds,
                leftMonsters = leftMonsters,
                maxEscapableMonsters = maxEscapableMonsters,
                escapedMonsters = escapedMonsters,
                stageBonusBuildingPoints = stageBonusBuildingPoints
            )
        }
    }
}

data class StartSceneMultiStagePlayStageParams(
    @LuaNames("preview_stage_index")
    var previewStageIndex: Int,
    @LuaNames("preview_display_duration")
    var previewDisplayDuration: Int,
    @LuaNames("gallery_stage_index")
    var galleryStageIndex: Int,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): StartSceneMultiStagePlayStageParams? {
            val previewStageIndex = table.optInt("preview_stage_index", 0)
            val previewDisplayDuration = table.optInt("preview_display_duration", 0)
            val galleryStageIndex = table.optInt("gallery_stage_index", 0)

            return StartSceneMultiStagePlayStageParams(
                previewStageIndex = previewStageIndex,
                previewDisplayDuration = previewDisplayDuration,
                galleryStageIndex = galleryStageIndex
            )
        }
    }
}

/**
 * Handler for scriptlib functions with the single and multistage scene play.
 * These are only callable from a lua group context.
 */
interface ScenePlayScriptHandler<GroupEventContext : GroupEventLuaContext> {
    // scene play
    fun createScenePlayGeneralRewardGadget(context: GroupEventContext, groupId: Int, configId: Int): Int
    fun failScenePlayBattle(context: GroupEventContext, groupId: Int): Int
    fun getScenePlayBattleHostUid(context: GroupEventContext, groupId: Int): Int
    fun getScenePlayBattleType(context: GroupEventContext, groupId: Int): Int
    fun getScenePlayBattleUidValue(context: GroupEventContext, groupId: Int, uid: Int, key: String): Int
    fun prestartScenePlayBattle(context: GroupEventContext, sceneParam: PrestartScenePlayBattleParams): Int
    fun scenePlayBattleUidOp(
        context: GroupEventContext,
        groupId: Int,
        configId: Int,
        uidList: List<Int>,
        buffType: Int,
        paramString: String,
        paramList: List<Int>,
        paramTargetList: List<Int>,
        index: Int,
        duration: Int
    ): Int

    fun setScenePlayBattlePlayTeamEntityGadgetId(context: GroupEventContext, groupId: Int, gadgetId: Int): Int
    fun setScenePlayBattleUidValue(context: GroupEventContext, groupId: Int, uid: Int, key: String, value: Int): Int
    fun addScenePlayBattleProgress(context: GroupEventContext, groupId: Int, progress: Int): Int

    // multistage play
    fun endSceneMultiStagePlay(context: GroupEventContext, playIndex: Int, isSucc: Boolean): Int
    fun endSceneMultiStagePlayStage(
        context: GroupEventContext,
        playIndex: Int,
        stageName: String,
        isSucc: Boolean
    ): Int

    fun getSceneMultiStagePlayUidValue(
        context: GroupEventContext,
        groupId: Int,
        index: Int,
        name: String,
        uid: Int
    ): Int

    fun initSceneMultistagePlay(
        context: GroupEventContext,
        index: Int,
        playType: MultistagePlayType,
        paramTable: InitSceneMultistagePlayParams,
        uidList: List<Int>
    ): Int

    fun setSceneMultiStagePlayUidValue(
        context: GroupEventContext,
        groupId: Int,
        index: Int,
        tag: String,
        value: Int
    ): Int

    fun setSceneMultiStagePlayValue(
        context: GroupEventContext,
        index: Int,
        tag: String,
        value: Int,
        isNotify: Boolean
    ): Int

    fun setSceneMultiStagePlayValues(
        context: GroupEventContext,
        index: Int,
        paramTable: SetSceneMultiStagePlayValuesParams,
        isNotify: Boolean
    ): Int

    fun startSceneMultiStagePlayStage(
        context: GroupEventContext,
        index: Int,
        time: Int,
        key: String,
        paramTable: StartSceneMultiStagePlayStageParams
    ): Int

    fun addSceneMultiStagePlayUidValue(
        context: GroupEventContext,
        groupId: Int,
        param2: Int,
        param3: String,
        uid: Int,
        param5: Int
    ): Int

    fun createFoundation(
        context: GroupEventContext,
        uidList: List<Int>,
        configId: Int,
        groupId: Int,
        playIndex: Int
    ): Int

    fun createFoundations(
        context: GroupEventContext,
        foundationParamsMap: Map<Int, Int>,
        groupId: Int,
        playIndex: Int
    ): Int
}
