package org.anime_game_servers.gi_lua.script_lib.handler.scene

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.models.constants.CurveType
import org.anime_game_servers.gi_lua.models.constants.FollowType
import org.anime_game_servers.gi_lua.models.constants.FollowType.FOLLOW_TYPE_INIT_FOLLOW_POS
import org.anime_game_servers.gi_lua.models.constants.KeepRotType
import org.anime_game_servers.gi_lua.models.constants.KeepRotType.KEEP_ROT_X
import org.anime_game_servers.gi_lua.models.constants.VehicleType
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.utils.ScriptUtils.toVector
import org.anime_game_servers.lua.engine.LuaTable

private val scriptLogger = logger {}

data class BeginCameraSceneLookParams(
    @LuaNames("look_config_id", "look_configid")
    val lookConfigId: Int = 0,
    @LuaNames("look_pos")
    val lookPos: Vector? = null,
    val duration: Float = 1f,
    @LuaNames("is_force")
    val isForce: Boolean = false,
    @LuaNames("is_broadcast")
    val isBroadcast: Boolean = false,
    @LuaNames("is_recover_keep_current")
    val isRecoverKeepCurrent: Boolean = true,
    val delay: Int = 0,
    @LuaNames("is_allow_input")
    val isAllowInput: Boolean = false,
    @LuaNames("is_set_follow_pos")
    val isSetFollowPos: Boolean = false,
    @LuaNames("follow_pos")
    val followPos: Vector? = null,
    @LuaNames("is_force_walk")
    val isForceWalk: Boolean = false,
    @LuaNames("is_change_play_mode")
    val isChangePlayMode: Boolean = false,
    @LuaNames("screen_x")
    val screenX: Float = 0f,
    @LuaNames("screen_y")
    val screenY: Float = 0f,
    @LuaNames("is_set_screenXY")
    val isSetScreenXY: Boolean = false,
    @LuaNames("other_params")
    val otherParams: List<String>? = null,
    @LuaNames("keep_rot_type")
    val keepRotType: KeepRotType = KEEP_ROT_X,
    @LuaNames("custom_radius")
    val customRadius: Float = 0f,
    @LuaNames("is_abs_follow_pos")
    val isAbsFollowPos: Boolean = false,
    @LuaNames("disable_protect")
    val disableProtect: Boolean = false,
    @LuaNames("blend_type")
    val blendType: Int = 0,
    @LuaNames("blend_duration")
    val blendDuration: Float = 0f,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): BeginCameraSceneLookParams? {
            return table.run {
                val keepRotTypeIndex = optInt("keep_rot_type", 0)
                if (keepRotTypeIndex >= KeepRotType.entries.size || keepRotTypeIndex < 0) {
                    scriptLogger.error { "[BeginCameraSceneLook] Invalid keep rot type $keepRotTypeIndex" }
                    return null
                }
                val keepRotType = KeepRotType.entries[keepRotTypeIndex]
                BeginCameraSceneLookParams(
                    lookConfigId = optInt("look_config_id", -1).let {
                        if (it == -1) optInt("look_configid", 0) else it
                    },
                    lookPos = getTable("look_pos")?.toVector(),
                    duration = optFloat("duration", 1f),
                    isForce = optBoolean("is_force", false),
                    isBroadcast = optBoolean("is_broadcast", false),
                    isRecoverKeepCurrent = optBoolean("is_recover_keep_current", true),
                    delay = optInt("delay", 0),
                    isAllowInput = optBoolean("is_allow_input", false),
                    isSetFollowPos = optBoolean("is_set_follow_pos", false),
                    followPos = getTable("follow_pos")?.toVector(),
                    isForceWalk = optBoolean("is_force_walk", false),
                    isChangePlayMode = optBoolean("is_change_play_mode", false),
                    screenX = optFloat("screen_x", 0f),
                    screenY = optFloat("screen_y", 0f),
                    isSetScreenXY = optBoolean("is_set_screenXY", false),
                    otherParams = getTable("other_params")?.getAsStringArray()?.toList(),
                    keepRotType = keepRotType,
                    customRadius = optFloat("custom_radius", 0f),
                    isAbsFollowPos = optBoolean("is_abs_follow_pos", false),
                    disableProtect = optBoolean("disable_protect", false),
                    blendType = optInt("blend_type", 0),
                    blendDuration = optFloat("blend_duration", 0f),
                )
            }
        }
    }
}

data class BeginCameraSceneLookTemplateParams(
    @LuaNames("look_config_id", "look_configid")
    val lookConfigId: Int = 0,
    @LuaNames("look_pos")
    val lookPos: Vector? = null,
    @LuaNames("is_broadcast")
    val isBroadcast: Boolean = false,
    val delay: Int = 0,
    @LuaNames("follow_pos")
    val followPos: Vector? = null,
    @LuaNames("follow_type")
    val followType: FollowType = FOLLOW_TYPE_INIT_FOLLOW_POS,
    @LuaNames("other_params")
    val otherParams: List<String>? = null,
) {
    companion object {
        fun fromLuaTable(table: LuaTable): BeginCameraSceneLookTemplateParams? {
            return table.run {
                val followTypeIndex = optInt("follow_type", 0)
                if (followTypeIndex >= FollowType.entries.size || followTypeIndex < 0) {
                    scriptLogger.error { "[BeginCameraSceneLookWithTemplate] Invalid follow type $followTypeIndex" }
                    return null
                }
                val followType = FollowType.entries[followTypeIndex]
                BeginCameraSceneLookTemplateParams(
                    lookConfigId = optInt("look_config_id", -1).let {
                        if (it == -1) optInt("look_configid", 0) else it
                    },
                    lookPos = getTable("look_pos")?.toVector(),
                    isBroadcast = optBoolean("is_broadcast", false),
                    delay = optInt("delay", 0),
                    followPos = getTable("follow_pos")?.toVector(),
                    followType = followType,
                    otherParams = getTable("other_params")?.getAsStringArray()?.toList(),
                )
            }
        }
    }
}

/**
 * Handler for scriptlib functions interacting with the player/avatar in the scene, or get states about it
 * These are only callable from a lua group context.
 */
interface ScenePlayerScriptHandler<GroupEventContext : GroupEventLuaContext> {

    // state info
    fun isPlayerTransmittable(context: GroupEventContext, uid: Int): Boolean

    // checks if all characters of the players team are dead. if the player does not exist, it returns 1
    fun isPlayerAllAvatarDie(context: GroupEventContext, uid: Int): Boolean
    fun getPlayerVehicleType(context: GroupEventContext, uid: Int): VehicleType

    fun enterCurve(context: GroupEventContext, uid: Int, curveId: Int, pointId: Int, curveType: CurveType): Int

    // equipped widget
    fun setWidgetClientDetectorCoolDown(context: GroupEventContext, widgetId: Int, isSucc: Boolean): Int
    fun isWidgetEquipped(context: GroupEventContext, uid: Int, widgetId: Int): Boolean


    // position management
    /**
     * This teleports the player to another position.
     * @param context a group event lua context
     * @param targetUIds the uids of the players to teleport
     * @param pos the position to teleport to
     * @param rot the rotation to teleport with
     * @param radius The radius around the target to place the target players in. -1 if the players should be placed at the exact position
     * @param isSkipUi when true reason is SkipUi, otherwise reason is ENTER_REASON_LUA
     * @param sceneId if this -1, the sceneId was not set, and the teleport target is in the same Scene the target is in
     * @return
     */
    fun transPlayerToPos(
        context: GroupEventContext,
        targetUIds: List<Int>,
        pos: Vector,
        rot: Vector,
        radius: Int,
        isSkipUi: Boolean,
        sceneId: Int
    ): Int

    // same as transPlayerToPos, but always inside the current scene
    fun movePlayerToPos(
        context: GroupEventContext,
        targetUIds: List<Int>,
        pos: Vector,
        rot: Vector,
        radius: Int,
        isSkipUi: Boolean
    ): Int


    // Visual

    /**
     * sends/broadcasts BeginCameraSceneLookNotify based on the params
     * target: when broadcast: all in scene, when uid in context that user, otherwise host
     * when delayed wait with send via timer
     */
    fun beginCameraSceneLook(context: GroupEventContext, sceneLookParamsTable: BeginCameraSceneLookParams): Int

    /**
     * Similar to [beginCameraSceneLook] but sends BeginCameraSceneLookWithTemplateNotify and has fewer parameters
     *
     */
    fun beginCameraSceneLookWithTemplate(
        context: GroupEventContext,
        templateId: Int,
        camParam: BeginCameraSceneLookTemplateParams
    ): Int

    // broadcasts CutSceneBeginNotify in scene, sets isWaitOthers to waitTime != 0
    // if waitTime == 0 returns directly, otherwise waits for clients to send CutSceneFinishNotify or waitTime ticks to finish and sends CutSceneEndNotify
    fun playCutScene(context: GroupEventContext, cutsceneId: Int, waitTime: Int): Int
    fun playCutSceneWithParam(
        context: GroupEventContext,
        cutsceneId: Int,
        waitTime: Int,
        paramList: List<List<Double>>
    ): Int


    // state+notify
    // saves the state and sends CanUseSkillNotify
    fun setIsAllowUseSkill(context: GroupEventContext, canUse: Int): Int

}