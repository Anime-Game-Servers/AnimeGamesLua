package org.anime_game_servers.gi_lua.script_lib.handler.other

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.utils.ScriptUtils.toVector
import org.anime_game_servers.lua.engine.LuaTable

private val scriptLogger = logger {}

data class AssignPlayerUidOpNotifyParams(
    @LuaNames("param_index")
    val paramIndex: Int,
    @LuaNames("param_list")
    val paramList: List<Int>,
    @LuaNames("param_uid_list")
    val paramUidList: List<Int>,
    val duration: Int,
    @LuaNames("target_uid_list")
    val targetUidList: List<Int>,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): AssignPlayerUidOpNotifyParams? {
            return table.run {
                val paramList = getTable("param_list")?.getAsIntArray()?.toList()
                val paramUidList = getTable("param_uid_list")?.getAsIntArray()?.toList()
                val targetUidList = getTable("target_uid_list")?.getAsIntArray()?.toList()
                if (paramList == null || paramUidList == null || targetUidList == null) {
                    scriptLogger.error { "[AssignPlayerUidOpNotify] missing parameter $paramList  $paramUidList  $targetUidList" }
                    return null
                }
                AssignPlayerUidOpNotifyParams(
                    paramIndex = optInt("param_index", 0),
                    paramList = paramList,
                    paramUidList = paramUidList,
                    duration = optInt("duration", 0),
                    targetUidList = targetUidList,
                )
            }
        }
    }
}

data class ScenePlaySoundParams(
    @LuaNames("play_pos")
    val playPos: Vector,
    @LuaNames("sound_name")
    val soundName: String = "",
    @LuaNames("play_type")
    val playType: Int = 0,
    @LuaNames("is_broadcast")
    val isBroadcast: Boolean = false,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): ScenePlaySoundParams? {
            return table.run {
                val playPos = getTable("playPos")?.toVector()
                val soundName = optString("sound_name", null)
                if (playPos == null || soundName == null ) {
                    scriptLogger.error { "[ScenePlaySound] missing parameter $playPos  $soundName" }
                    return null
                }
                ScenePlaySoundParams(
                    playPos = playPos,
                    soundName = soundName,
                    playType = optInt("play_type", 0),
                    isBroadcast = optBoolean("is_broadcast", false)
                )
            }
        }
    }
}

data class AssignPlayerShowTemplateReminderParams(
    @LuaNames("param_vec")
    val paramVec: List<Int>,
    @LuaNames("param_uid_vec")
    val paramUidVec: List<Int>,
    @LuaNames("uid_vec")
    val uidVec: List<Int>,
    @LuaNames("is_need_cache")
    val isNeedCache: Boolean = false,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): AssignPlayerShowTemplateReminderParams? {
            return table.run {
                val paramVec = getTable("param_vec")?.getAsIntArray()?.toList()
                val paramUidVec = getTable("param_uid_vec")?.getAsIntArray()?.toList()
                val uidVec = getTable("uid_vec")?.getAsIntArray()?.toList()
                if (paramVec == null || paramUidVec == null || uidVec == null ) {
                    scriptLogger.error { "[AssignPlayerShowTemplateReminder] missing parameter $paramVec  $paramUidVec  $uidVec" }
                    return null
                }
                AssignPlayerShowTemplateReminderParams(
                    paramVec = paramVec,
                    paramUidVec = paramUidVec,
                    uidVec = uidVec,
                    isNeedCache = optBoolean("is_need_cache", false)
                )
            }
        }
    }
}

/**
 * Handler for misc scriptlib functions that just end up in a notify to the player/players without any
 * server state changes.
 * These are only callable from a lua group context.
 */
interface MiscNotifyScriptHandler<GroupEventContext : GroupEventLuaContext> {
    /**
     * just broadcasts PerformOperationNotify
     * target: all in scene
     */
    fun notifyAllPlayerPerformOperation(
        context: GroupEventContext,
        teamEntityId: Int,
        type: Int,
        effectIndex: Int,
        hunterPos: Vector,
        hunterRot: Vector
    ): Int

    /**
     * just broadcasts LuaEnvironmentEffectNotify
     * target: all in scene
     */
    fun setEnvironmentEffectState(
        context: GroupEventContext,
        index: Int,
        key: String,
        floatParamTable: FloatArray,
        intParam: IntArray
    ): Int

    /**
     * just sends SetLimitOptimizationNotify
     * target: [uid]
     */
    fun setLimitOptimization(context: GroupEventContext, uid: Int, isLimitOptimization: Boolean): Int

    /**
     * just broadcasts LuaSetOptionNotify
     * target: all in scene
     */
    fun setPlayerInteractOption(context: GroupEventContext, key: String): Int

    /**
     * just sends NormalUidOpNotify
     * target: [AssignPlayerUidOpNotifyParams.targetUidList]
     */
    fun assignPlayerUidOpNotify(context: GroupEventContext, params: AssignPlayerUidOpNotifyParams): Int

    /**
     * Sends ScenePlayerSoundNotify
     * taget: all in scene when [ScenePlaySoundParams.isBroadcast], else context.uid else host
     */
    fun scenePlaySound(context: GroupEventContext, soundInfoTable: ScenePlaySoundParams): Int

    /**
     * gets the message id for ServerMessageExcelConfigData via key and sends it via ServerMessageNotify
     * target: all in [targets]
     */
    fun sendServerMessageByLuaKey(context: GroupEventContext, messageKey: String, targets: List<Int>): Int


    //Reminder
    /**
     * Just sends DungeonShowReminderNotify
     * target: all in [uidList]
     */
    fun showReminderByUid(context: GroupEventContext, uidList: List<Int>, reminderId: Int): Int

    /**
     * Just broadcasts StopReminderNotify
     * target: all in scene
     */
    fun stopReminder(context: GroupEventContext, reminderId: Int): Int

    /**
     * Just broadcasts DungeonShowReminderNotify
     * target: all in scene
     */
    fun showReminder(context: GroupEventContext, reminderId: Int): Int

    /**
     * Just sends DungeonShowReminderNotify
     * target: everyone in [radius] around [position]
     */
    fun showReminderRadius(context: GroupEventContext, reminderId: Int, position: Vector, radius: Int): Int

    /**
     * Just broadcasts ShowTemplateReminderNotify
     * target: all in scene
     */
    fun showTemplateReminder(context: GroupEventContext, reminderId: Int, timerInfo: IntArray): Int

    /**
     * Just sends ShowTemplateReminderNotify,
     * target: [AssignPlayerShowTemplateReminderParams.uidVec]
     */
    fun assignPlayerShowTemplateReminder(context: GroupEventContext, reminderId: Int, params: AssignPlayerShowTemplateReminderParams): Int

    /**
     * Just sends ShowTemplateReminderNotify with set_is_revoke set to true
     * targets: if uidList.isNotEmpty uidList else all in scene
     */
    fun revokePlayerShowTemplateReminder(context: GroupEventContext, reminderId: Int, uidList: List<Int>): Int


    // guides
    /**
     * Just broadcasts ShowClientGuideNotify with guideName
     * target: all in scene
     */
    fun showClientGuide(context: GroupEventContext, guideName: String): Int

    /**
     * Just sends ShowClientTutorialNotify with tutorialId
     * taget: if uidList.isNotEmpty uidList else host
     */
    fun showClientTutorial(context: GroupEventContext, tutorialId: Int, uidList: List<Int>): Int


    // tips
    /**
     * Just sends CommonPlayerTipsNotify
     * target: host
     */
    fun showCommonPlayerTips(context: GroupEventContext, type: Int, keys: List<String>): Int

    /**
     * Just sends ShowCommonTipsNotify
     * target: host
     */
    fun sendShowCommonTipsToClient(context: GroupEventContext, title: String, content: String, closeTime: Int): Int

    /**
     * Just sends CloseCommonTipsNotify
     * target: host
     */
    fun sendCloseCommonTipsToClient(context: GroupEventContext): Int
}
