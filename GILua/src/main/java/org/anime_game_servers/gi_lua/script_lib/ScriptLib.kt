@file:Suppress("unused", "FunctionName")

package org.anime_game_servers.gi_lua.script_lib

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.core.base.annotations.lua.LuaStatic
import org.anime_game_servers.gi_lua.models.constants.*
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreType
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreUIType
import org.anime_game_servers.gi_lua.script_lib.handler.ScriptLibStaticHandler
import org.anime_game_servers.gi_lua.script_lib.handler.activity.FungusFighterTrainingParams
import org.anime_game_servers.gi_lua.script_lib.handler.entites.MonsterFaceAvatarParameters
import org.anime_game_servers.gi_lua.script_lib.handler.parameter.KillByConfigIdParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.ChangeLevelTagParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.SealBattleParams
import org.anime_game_servers.gi_lua.utils.ScriptUtils
import org.anime_game_servers.lua.engine.LuaTable
import kotlin.reflect.KCallable

@LuaStatic
object ScriptLib {
    /**
     * Context free functions
     */
    private val scriptLogger = logger(
        ScriptLib::class.java.name
    )
    @JvmStatic
    var staticHandler: ScriptLibStaticHandler? = null

    @JvmStatic
    fun PrintLog(msg: String?) : Int {
        return staticHandler?.PrintLog(msg)?.let { 0 } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue()
    }
    @Deprecated("only for compat with modified Scripts", ReplaceWith("PrintLog(msg: String?)"))
    @JvmStatic
    fun PrintLog(context: LuaContextWrapper, msg: String?) : Int {
        return PrintLog(msg)
    }

    @JvmStatic
    fun GetEntityType(entityId: Int): Int {
        return staticHandler?.GetEntityType(entityId) ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue()
    }
    @Deprecated("only for compat with modified Scripts", ReplaceWith("GetEntityType(entityId: Int)"))
    @JvmStatic
    fun GetEntityType(context: LuaContextWrapper, entityId: Int): Int {
        return GetEntityType(entityId)
    }


    /**
     * Context independent functions
     */
    @JvmStatic
    fun PrintContextLog(context: LuaContextWrapper, msg: String) : Int {
        return staticHandler?.PrintContextLog(context.luaContext, msg)?.let { 0 }  ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue()
    }

    /**
     * GroupEventLuaContext functions
     */
    @JvmStatic
    fun PrintGroupWarning(context: LuaContextWrapper, msg: String?) {
        context.onGroupContext {
            onScriptLibHandler {
                PrintGroupWarning(this@onGroupContext, msg)
            }
        }
    }



    @JvmStatic
    // Some fields are guessed
    fun AutoMonsterTide(
        context: LuaContextWrapper,
        tideId: Int,
        groupId: Int,
        ordersConfigId: Array<Int?>?,
        tideCount: Int,
        sceneLimit: Int,
        param6: Int
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::AutoMonsterTide, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AutoMonsterTide(this@onGroupContext, tideId, groupId, ordersConfigId, tideCount, sceneLimit, param6)
            }
        }
    }

    @JvmStatic
    fun KillMonsterTide(context: LuaContextWrapper, groupId: Int, tideId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::KillMonsterTide, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                KillMonsterTide(this@onGroupContext, groupId, tideId)
            }
        }
    }


    @JvmStatic
    fun GetGroupMonsterCountByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::GetGroupMonsterCountByGroupId, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                GetGroupMonsterCountByGroupId(this@onGroupContext, groupId)
            }
        }
    }





    @JvmStatic
    fun GetRegionEntityCount(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val table = context.engine.getTable(rawTable)

                val regionId = table.getInt("region_eid")
                val entityType = table.getInt("entity_type")
                if (entityType < 0 || entityType >= EntityType.entries.size) {
                    scriptLogger.error { "[GetRegionEntityCount] Invalid entity type $entityType" }
                    return@onScriptLibHandler 0
                }

                val entityTypeEnum = EntityType.entries[entityType]
                GetRegionEntityCount(this@onGroupContext, regionId, entityTypeEnum)
            }
        }
    }

    @JvmStatic
    fun GetRegionConfigId(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val table = context.engine.getTable(rawTable)
                val regionEid = table.getInt("region_eid")
                GetRegionConfigId(this@onGroupContext, regionEid)
            }
        }
    }

    @JvmStatic
    fun TowerCountTimeStatus(context: LuaContextWrapper, isDone: Int, var2: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                TowerCountTimeStatus(this@onGroupContext, isDone, var2)
            }
        }
    }

    @JvmStatic
    fun GetGroupMonsterCount(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                GetGroupMonsterCount(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun SetMonsterBattleByGroup(context: LuaContextWrapper, configId: Int, groupId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupIdAndConfigId(ScriptLib::SetMonsterBattleByGroup, groupId, configId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                SetMonsterBattleByGroup(this@onGroupContext, configId, groupId)
            }
        }
    }



    @JvmStatic
    fun SetIsAllowUseSkill(context: LuaContextWrapper, canUse: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                SetIsAllowUseSkill(this@onGroupContext, canUse)
            }
        }
    }



    @JvmStatic
    fun TowerMirrorTeamSetUp(context: LuaContextWrapper, team: Int, var1: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                TowerMirrorTeamSetUp(this@onGroupContext, team, var1)
            }
        }
    }



    @JvmStatic
    fun CreateVehicle(context: LuaContextWrapper, uid: Int, gadgetId: Int, posTable: Any, rotTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::CreateVehicle, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                val luaPos = context.engine.getTable(posTable)
                val luaRot = context.engine.getTable(rotTable)
                CreateVehicle(this@onGroupContext, uid, gadgetId, ScriptUtils.luaToPos(luaPos), ScriptUtils.luaToPos(luaRot))
            }
        }
    }

    @JvmStatic
    fun CheckRemainGadgetCountByGroupId(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val table = context.engine.getTable(rawTable)
                CheckRemainGadgetCountByGroupId(this@onGroupContext, table)
            }
        }
    }

    @JvmStatic
    fun MarkPlayerAction(context: LuaContextWrapper, var1: Int, var2: Int, var3: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                MarkPlayerAction(this@onGroupContext, var1, var2, var3)
            }
        }
    }


    @JvmStatic
    fun GetSceneOwnerUid(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                GetSceneOwnerUid(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun ShowReminder(context: LuaContextWrapper, reminderId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                ShowReminder(this@onGroupContext, reminderId)
            }
        }
    }

    @JvmStatic
    fun CreateGroupTimerEvent(context: LuaContextWrapper, groupID: Int, source: String?, time: Double): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::CreateGroupTimerEvent, groupID)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                CreateGroupTimerEvent(this@onGroupContext, groupID, source, time)
            }
        }
    }

    @JvmStatic
    fun CancelGroupTimerEvent(context: LuaContextWrapper, groupID: Int, source: String?): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::CancelGroupTimerEvent, groupID)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                CancelGroupTimerEvent(this@onGroupContext, groupID, source)
            }
        }
    }

    @JvmStatic
    fun GetSceneUidList(context: LuaContextWrapper): Any {
        return context.onGroupContext {
            onScriptLibHandler {
                val list = GetSceneUidList(this@onGroupContext)
                val result = context.engine.createTable()

                for (i in list.indices) {
                    result.set((i + 1).toString(), list[i])
                }
                return@onScriptLibHandler result.getRawTable()
            }
        }
    }

    @JvmStatic
    fun GetSeaLampActivityPhase(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                GetSeaLampActivityPhase(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GadgetPlayUidOp(
        context: LuaContextWrapper,
        groupId: Int,
        gadgetCrucibleCfgId: Int,
        uidListRawTable: Any,
        var4: Int,
        var5: String,
        var6RawTable: Any
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupIdAndConfigId(ScriptLib::GadgetPlayUidOp, groupId, gadgetCrucibleCfgId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                val uidList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
                val var6Table = context.engine.getTable(var6RawTable)
                GadgetPlayUidOp(this@onGroupContext, groupId, gadgetCrucibleCfgId, uidList, var4, var5, var6Table)
            }
        }
    }

    @JvmStatic
    fun GetServerTime(context: LuaContextWrapper): Long {
        return context.onGroupContext {
            onScriptLibHandler {
                GetServerTime(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetServerTimeByWeek(context: LuaContextWrapper): Long {
        return context.onGroupContext {
            onScriptLibHandler {
                GetServerTimeByWeek(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetCurTriggerCount(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                GetCurTriggerCount(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetChannellerSlabLoopDungeonLimitTime(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                GetChannellerSlabLoopDungeonLimitTime(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun IsPlayerAllAvatarDie(context: LuaContextWrapper, uid: Int): Boolean {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::IsPlayerAllAvatarDie, uid)?.let {
                    return@onScriptLibHandler false
                }
                IsPlayerAllAvatarDie(this@onGroupContext, uid)
            }
        }
    }

    @JvmStatic
    fun sendShowCommonTipsToClient(
        context: LuaContextWrapper,
        title: String?,
        content: String?,
        closeTime: Int
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                sendShowCommonTipsToClient(this@onGroupContext, title, content, closeTime)
            }
        }
    }

    @JvmStatic
    fun sendCloseCommonTipsToClient(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                sendCloseCommonTipsToClient(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun updateBundleMarkShowStateByGroupId(context: LuaContextWrapper, groupId: Int, val2: Boolean): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::updateBundleMarkShowStateByGroupId, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                updateBundleMarkShowStateByGroupId(this@onGroupContext, groupId, val2)
            }
        }
    }

    @JvmStatic
    fun CreateBlossomChestByGroupId(context: LuaContextWrapper, groupId: Int, chestConfigId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupIdAndConfigId(ScriptLib::CreateBlossomChestByGroupId, groupId, chestConfigId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                CreateBlossomChestByGroupId(this@onGroupContext, groupId, chestConfigId)
            }
        }
    }

    @JvmStatic
    fun GetBlossomScheduleStateByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::GetBlossomScheduleStateByGroupId, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                GetBlossomScheduleStateByGroupId(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun SetBlossomScheduleStateByGroupId(context: LuaContextWrapper, groupId: Int, state: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                SetBlossomScheduleStateByGroupId(this@onGroupContext, groupId, state)
            }
        }
    }

    @JvmStatic
    fun RefreshBlossomGroup(context: LuaContextWrapper, rawTable: Any): Int {
        val configTable = context.engine.getTable(rawTable)
        return context.onGroupContext {
            onScriptLibHandler {
                RefreshBlossomGroup(this@onGroupContext, configTable)
            }
        }
    }

    @JvmStatic
    fun RefreshBlossomDropRewardByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::RefreshBlossomDropRewardByGroupId, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                RefreshBlossomDropRewardByGroupId(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun AddBlossomScheduleProgressByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::AddBlossomScheduleProgressByGroupId, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddBlossomScheduleProgressByGroupId(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun GetBlossomRefreshTypeByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::GetBlossomRefreshTypeByGroupId, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                GetBlossomRefreshTypeByGroupId(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun RefreshHuntingClueGroup(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                RefreshHuntingClueGroup(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetHuntingMonsterExtraSuiteIndexVec(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                GetHuntingMonsterExtraSuiteIndexVec(this@onGroupContext)
            }
        }
    }


    @JvmStatic
    fun FinishExpeditionChallenge(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                FinishExpeditionChallenge(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun ExpeditionChallengeEnterRegion(context: LuaContextWrapper, var1: Boolean): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                ExpeditionChallengeEnterRegion(this@onGroupContext, var1)
            }
        }
    }



    @JvmStatic
    fun InitTimeAxis(context: LuaContextWrapper, var1: String?, var2Table: Any, var3: Boolean): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val var2 = context.engine.getTable(var2Table)
                InitTimeAxis(this@onGroupContext, var1, var2, var3)
            }
        }
    }

    @JvmStatic
    fun EndTimeAxis(context: LuaContextWrapper, var1: String?): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                EndTimeAxis(this@onGroupContext, var1)
            }
        }
    }


    @JvmStatic
    fun StartHomeGallery(context: LuaContextWrapper, galleryId: Int, uid: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::StartHomeGallery, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                StartHomeGallery(this@onGroupContext, galleryId, uid)
            }
        }
    }

    @JvmStatic
    fun SetHandballGalleryBallPosAndRot(
        context: LuaContextWrapper,
        galleryId: Int,
        positionTable: Any,
        rotationTable: Any
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val position = context.engine.getTable(positionTable)
                val rotation = context.engine.getTable(rotationTable)
                SetHandballGalleryBallPosAndRot(this@onGroupContext, galleryId, position, rotation)
            }
        }
    }

    /**
     * This signalizes the server that it should unlock the float signal gadget with the specified id in the specified group
     * @param context group context in which this function is called
     * @param groupId group id of the group containing the float signal gadget that should be unlocked
     * @param signalGadgetCfgId the config id identifying the of the float signal gadget that should be unlocked
     * @return 0 on success, otherwise an error code
     */
    @JvmStatic
    fun UnlockFloatSignal(context: LuaContextWrapper, groupId: Int, signalGadgetCfgId: Int): Int {
        return context.onGroupContext {
            onSummerTimeScriptHandler {
                checkGroupIdAndConfigId(::UnlockFloatSignal, groupId, signalGadgetCfgId)?.let {
                    return@onSummerTimeScriptHandler it.getValue()
                }
                unlockFloatSignal(this@onGroupContext, groupId, signalGadgetCfgId)
            }
        }
    }

    @JvmStatic
    fun SendServerMessageByLuaKey(context: LuaContextWrapper, stringKey: String?, targetsTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val targets = context.engine.getTable(targetsTable)
                SendServerMessageByLuaKey(this@onGroupContext, stringKey, targets.getAsIntArray())
            }
        }
    }

    @JvmStatic
    fun TryReallocateEntityAuthority(context: LuaContextWrapper, uid: Int, endConfig: Int, var3: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::TryReallocateEntityAuthority, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                TryReallocateEntityAuthority(this@onGroupContext, uid, endConfig, var3)
            }
        }
    }

    @JvmStatic
    fun ForceRefreshAuthorityByConfigId(context: LuaContextWrapper, var1: Int, uid: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::ForceRefreshAuthorityByConfigId, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                ForceRefreshAuthorityByConfigId(this@onGroupContext, var1, uid)
            }
        }
    }

    @JvmStatic
    fun AddPlayerGroupVisionType(context: LuaContextWrapper, uidsTable: Any, visionTypesTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val uids = context.engine.getTable(uidsTable).getAsIntArray()
                val visionTypes = context.engine.getTable(visionTypesTable).getAsIntArray()
                AddPlayerGroupVisionType(this@onGroupContext, uids, visionTypes)
            }
        }
    }

    @JvmStatic
    fun DelPlayerGroupVisionType(context: LuaContextWrapper, uidsTable: Any, visionTypesTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val uids = context.engine.getTable(uidsTable).getAsIntArray()
                val visionTypes = context.engine.getTable(visionTypesTable).getAsIntArray()
                DelPlayerGroupVisionType(this@onGroupContext, uids, visionTypes)
            }
        }
    }

    @JvmStatic
    fun SetPlayerGroupVisionType(context: LuaContextWrapper, uidsTable: Any, visionTypesTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val uids = context.engine.getTable(uidsTable).getAsIntArray()
                val visionTypes = context.engine.getTable(visionTypesTable).getAsIntArray()
                SetPlayerGroupVisionType(this@onGroupContext, uids, visionTypes)
            }
        }
    }


    @JvmStatic
    fun MoveAvatarByPointArray(
        context: LuaContextWrapper,
        uid: Int,
        targetId: Int,
        var3Table: Any,
        var4: String?
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val var3 = context.engine.getTable(var3Table)
                MoveAvatarByPointArray(this@onGroupContext, uid, targetId, var3, var4)
            }
        }
    }

    @JvmStatic
    fun MovePlayerToPos(context: LuaContextWrapper, moveParamsTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val moveParams = context.engine.getTable(moveParamsTable)
                val targetsTable = moveParams.getTable("uid_list")
                val luaPos = moveParams.getTable("pos")
                val luaRot = moveParams.getTable("rot")
                val radius = moveParams.optInt("radius", -1)
                val isSkipUi = moveParams.optBoolean("is_skip_ui", false)


                if (targetsTable == null || targetsTable.getSize() == 0 || luaPos == null) {
                    scriptLogger.error { "[MovePlayerToPos] Invalid params, either missing uid_list or pos" }
                    return@onScriptLibHandler 1
                }

                val pos = ScriptUtils.luaToPos(luaPos)
                val rot = ScriptUtils.luaToPos(luaRot)
                val targets = targetsTable.getAsIntArray()

                MovePlayerToPos(this@onGroupContext, targets, pos, rot, radius, isSkipUi)
            }
        }
    }

    /**
     * Signalises that the server should transport the players with the specified uids to the specified position
     * @param context the Group Lua context
     * @param transportationParamsTable the table containing the parameters for the transportation. This includes
     * `uid_list` - the list of uids of the players to transport
     * `pos` - table with the position to transport the players to
     * `rot` - table with the rotation the players should be placed in
     * `radius` - the radius around the position the players should be placed in
     * `is_skip_ui` -
     * `scene_id` - the scene id to transport the players to, if not specified the current scene is used
     *
     * @return
     */
    @JvmStatic
    fun TransPlayerToPos(context: LuaContextWrapper, transportationParamsTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val transportationParams = context.engine.getTable(transportationParamsTable)
                val targetsTable = transportationParams.getTable("uid_list")
                val luaPos = transportationParams.getTable("pos")
                val luaRot = transportationParams.getTable("rot")
                val radius = transportationParams.optInt("radius", -1)
                val isSkipUi = transportationParams.optBoolean("is_skip_ui", false)
                val sceneId = transportationParams.optInt("scene_id", -1)


                if (targetsTable == null || targetsTable.getSize() == 0 || luaPos == null) {
                    scriptLogger.error { "[TransPlayerToPos] Invalid params, either missing uid_list or pos" }
                    return@onScriptLibHandler 1
                }

                val pos = ScriptUtils.luaToPos(luaPos)
                val rot = ScriptUtils.luaToPos(luaRot)
                val targets = targetsTable.getAsIntArray()

                TransPlayerToPos(this@onGroupContext, targets, pos, rot, radius, isSkipUi, sceneId)
            }
        }
    }

    @JvmStatic
    fun PlayCutScene(context: LuaContextWrapper, cutsceneId: Int, var2: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                PlayCutScene(this@onGroupContext, cutsceneId, var2)
            }
        }
    }

    @JvmStatic
    fun PlayCutSceneWithParam(context: LuaContextWrapper, cutsceneId: Int, var2: Int, var3Table: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val var3 = context.engine.getTable(var3Table)
                PlayCutSceneWithParam(this@onGroupContext, cutsceneId, var2, var3)
            }
        }
    }

    @JvmStatic
    fun ScenePlaySound(context: LuaContextWrapper, soundInfoTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val soundInfo = context.engine.getTable(soundInfoTable)
                ScenePlaySound(this@onGroupContext, soundInfo)
            }
        }
    }

    @JvmStatic
    fun BeginCameraSceneLook(context: LuaContextWrapper, sceneLookParamsTable: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val sceneLookParams = context.engine.getTable(sceneLookParamsTable)
                BeginCameraSceneLook(this@onGroupContext, sceneLookParams)
            }
        }
    }

    @JvmStatic
    fun SetPlayerEyePointStream(context: LuaContextWrapper, var1: Int, var2: Int, var3: Boolean): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                SetPlayerEyePointStream(this@onGroupContext, var1, var2, var3)
            }
        }
    }

    @JvmStatic
    fun ClearPlayerEyePoint(context: LuaContextWrapper, var1: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                ClearPlayerEyePoint(this@onGroupContext, var1)
            }
        }
    }

    @JvmStatic
    fun ShowReminderRadius(context: LuaContextWrapper, var1: Int, var2Table: Any, var3: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val var2 = context.engine.getTable(var2Table)
                ShowReminderRadius(this@onGroupContext, var1, var2, var3)
            }
        }
    }

    @JvmStatic
    fun ShowClientGuide(context: LuaContextWrapper, guideName: String?): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                ShowClientGuide(this@onGroupContext, guideName)
            }
        }
    }



    @JvmStatic
    fun SetWeatherAreaState(context: LuaContextWrapper, var1: Int, var2: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                SetWeatherAreaState(this@onGroupContext, var1, var2)
            }
        }
    }

    @JvmStatic
    fun EnterWeatherArea(context: LuaContextWrapper, weatherAreaId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                EnterWeatherArea(this@onGroupContext, weatherAreaId)
            }
        }
    }

    @JvmStatic
    fun CheckIsInMpMode(context: LuaContextWrapper): Boolean {
        return context.onGroupContext {
            onScriptLibHandler {
                CheckIsInMpMode(this@onGroupContext)
            }
        }
    }

    /**
     * TODO properly implement
     * var3 might contain the next point, sometimes is a single int, sometimes multiple ints as array
     * var4 has RouteType route_type, bool turn_mode
     */
    @JvmStatic
    fun SetPlatformPointArray(
        context: LuaContextWrapper,
        entityConfigId: Int,
        pointArrayId: Int,
        var3Table: Any,
        var4Table: Any
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val var3 = context.engine.getTable(var3Table)
                val var4 = context.engine.getTable(var4Table)
                SetPlatformPointArray(this@onGroupContext, entityConfigId, pointArrayId, var3, var4)
            }
        }
    }

    //TODO check
    @JvmStatic
    fun SetPlatformRouteId(context: LuaContextWrapper, entityConfigId: Int, routeId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkConfigId(ScriptLib::SetPlatformRouteId, entityConfigId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                SetPlatformRouteId(this@onGroupContext, entityConfigId, routeId)
            }
        }
    }

    //TODO check
    @JvmStatic
    fun StartPlatform(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkConfigId(ScriptLib::StartPlatform, configId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                StartPlatform(this@onGroupContext, configId)
            }
        }
    }

    //TODO check
    @JvmStatic
    fun StopPlatform(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkConfigId(ScriptLib::StopPlatform, configId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                StopPlatform(this@onGroupContext, configId)
            }
        }
    }

    @JvmStatic
    fun CreateChannellerSlabCampRewardGadget(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                CreateChannellerSlabCampRewardGadget(this@onGroupContext, configId)
            }
        }
    }

    @JvmStatic
    fun AssignPlayerShowTemplateReminder(context: LuaContextWrapper, var1: Int, var2Table: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val var2 = context.engine.getTable(var2Table)
                AssignPlayerShowTemplateReminder(this@onGroupContext, var1, var2)
            }
        }
    }

    @JvmStatic
    fun RevokePlayerShowTemplateReminder(context: LuaContextWrapper, var1: Int, var2Table: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val var2 = context.engine.getTable(var2Table)
                RevokePlayerShowTemplateReminder(this@onGroupContext, var1, var2)
            }
        }
    }



    @JvmStatic
    fun GetActivityOpenAndCloseTimeByScheduleId(context: LuaContextWrapper, scheduleId: Int): Any {
        return context.onGroupContext {
            onScriptLibHandler {
                val result = context.engine.createTable()
                val activityConfig = GetActivityOpenAndCloseTimeByScheduleId(this@onGroupContext, scheduleId)

                if (activityConfig != null) {
                    result.set(1, activityConfig.openTime.time)
                    result.set(2, activityConfig.closeTime.time)
                }

                result.getRawTable()
            }
        }
    }

    @JvmStatic
    fun GetGameHour(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                GetGameHour(this@onGroupContext)
            }
        }
    }

    /**
     * TODO implement
     * @param context
     * @param givingId The id if the giving element found in [GivingData]
     * @param groupId The groupdId of the group containing the gadget
     * @param gadgetCfgId The gadgets target configId
     * @return 0 if success, something else if failed
     */
    @JvmStatic
    fun ActiveGadgetItemGiving(context: LuaContextWrapper, givingId: Int, groupId: Int, gadgetCfgId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupIdAndConfigId(ScriptLib::ActiveGadgetItemGiving, groupId, gadgetCfgId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                ActiveGadgetItemGiving(this@onGroupContext, givingId, groupId, gadgetCfgId)
            }
        }
    }

    @JvmStatic
    fun AddChessBuildingPoints(
        context: LuaContextWrapper,
        groupId: Int,
        param2: Int,
        uid: Int,
        pointsToAdd: Int
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::AddChessBuildingPoints, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                checkUid(ScriptLib::AddChessBuildingPoints, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddChessBuildingPoints(this@onGroupContext, groupId, param2, uid, pointsToAdd)
            }
        }
    }

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2  probably the name of the data field
     * @param param3
     * @return
     */
    @JvmStatic
    fun AddExhibitionAccumulableData(context: LuaContextWrapper, uid: Int, param2: String?, param3: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::AddExhibitionAccumulableData, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddExhibitionAccumulableData(this@onGroupContext, uid, param2, param3)
            }
        }
    }

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2 probably the name of the data field
     * @param param3
     * @param param4Table contains the fields "play_type" is part of the enum [ExhibitionPlayType] and "gallery_id"
     * @return
     */
    @JvmStatic
    fun AddExhibitionAccumulableDataAfterSuccess(
        context: LuaContextWrapper,
        uid: Int,
        param2: String?,
        param3: Int,
        param4Table: Any
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::AddExhibitionAccumulableDataAfterSuccess, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                val param4 = context.engine.getTable(param4Table)
                val exhibitionTypeIndex = param4.optInt("play_type", -1)
                val galleryId = param4.optInt("gallery_id", -1)
                if (exhibitionTypeIndex < 0 || exhibitionTypeIndex >= ExhibitionPlayType.entries.size) {
                    scriptLogger.error { "[AddExhibitionAccumulableDataAfterSuccess] Invalid exhibition type $exhibitionTypeIndex" }
                    return@onScriptLibHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                if (galleryId == -1) {
                    scriptLogger.error { "[AddExhibitionAccumulableDataAfterSuccess] Invalid gallery id $galleryId" }
                    return@onScriptLibHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                val exhibitionTypeEnum = ExhibitionPlayType.entries[exhibitionTypeIndex]
                AddExhibitionAccumulableDataAfterSuccess(this@onGroupContext, uid, param2, param3, exhibitionTypeEnum, galleryId)
            }
        }
    }

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2  probably the name of the data field
     * @param param3
     * @return
     */
    @JvmStatic
    fun AddExhibitionReplaceableData(context: LuaContextWrapper, uid: Int, param2: String?, param3: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::AddExhibitionReplaceableData, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddExhibitionReplaceableData(this@onGroupContext, uid, param2, param3)
            }
        }
    }

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2 probably the name of the data field
     * @param param3
     * @param param4Table contains the fields "play_type" is part of the enum [ExhibitionPlayType] and "gallery_id"
     * @return
     */
    @JvmStatic
    fun AddExhibitionReplaceableDataAfterSuccess(
        context: LuaContextWrapper,
        uid: Int,
        param2: String?,
        param3: Int,
        param4Table: Any
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::AddExhibitionReplaceableDataAfterSuccess, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                val param4 = context.engine.getTable(param4Table)
                AddExhibitionReplaceableDataAfterSuccess(this@onGroupContext, uid, param2, param3, param4)
            }
        }
    }

    @JvmStatic
    fun AddGadgetPlayProgress(context: LuaContextWrapper, param1: Int, param2: Int, progressChange: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                AddGadgetPlayProgress(this@onGroupContext, param1, param2, progressChange)
            }
        }
    }

    @JvmStatic
    fun AddIrodoriChessBuildingPoints(context: LuaContextWrapper, groupId: Int, param2: Int, points: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::AddIrodoriChessBuildingPoints, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddIrodoriChessBuildingPoints(this@onGroupContext, groupId, param2, points)
            }
        }
    }

    @JvmStatic
    fun AddIrodoriChessTowerServerGlobalValue(
        context: LuaContextWrapper,
        groupId: Int,
        param2: Int,
        param3: Int,
        delta: Int
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::AddIrodoriChessTowerServerGlobalValue, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddIrodoriChessTowerServerGlobalValue(this@onGroupContext, groupId, param2, param3, delta)
            }
        }
    }

    @JvmStatic
    fun AddMechanicusBuildingPoints(
        context: LuaContextWrapper,
        groupId: Int,
        param2: Int,
        uid: Int,
        delta: Int
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::AddMechanicusBuildingPoints, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                checkUid(ScriptLib::AddMechanicusBuildingPoints, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddMechanicusBuildingPoints(this@onGroupContext, groupId, param2, uid, delta)
            }
        }
    }

    @JvmStatic
    fun AddRegionRecycleProgress(context: LuaContextWrapper, regionId: Int, delta: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                AddRegionRecycleProgress(this@onGroupContext, regionId, delta)
            }
        }
    }

    @JvmStatic
    fun AddRegionSearchProgress(context: LuaContextWrapper, regionId: Int, delta: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                AddRegionSearchProgress(this@onGroupContext, regionId, delta)
            }
        }
    }

    @JvmStatic
    fun AddRegionalPlayVarValue(context: LuaContextWrapper, uid: Int, regionId: Int, delta: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkUid(ScriptLib::AddRegionalPlayVarValue, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddRegionalPlayVarValue(this@onGroupContext, uid, regionId, delta)
            }
        }
    }

    @JvmStatic
    fun AddSceneMultiStagePlayUidValue(
        context: LuaContextWrapper,
        groupId: Int,
        param2: Int,
        param3: String?,
        uid: Int,
        param5: Int
    ): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::AddSceneMultiStagePlayUidValue, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                checkUid(ScriptLib::AddSceneMultiStagePlayUidValue, uid)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddSceneMultiStagePlayUidValue(this@onGroupContext, groupId, param2, param3, uid, param5)
            }
        }
    }

    @JvmStatic
    fun AddScenePlayBattleProgress(context: LuaContextWrapper, groupId: Int, progress: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                checkGroupId(ScriptLib::AddScenePlayBattleProgress, groupId)?.let {
                    return@onScriptLibHandler it.getValue()
                }
                AddScenePlayBattleProgress(this@onGroupContext, groupId, progress)
            }
        }
    }

    /**
     * TODO implement
     * @param context
     * @param param1Table contains the following fields: param_index:int, param_list:Table, param_uid_list:Table,
     * duration:int, target_uid_list:Table
     * @return
     */
    @JvmStatic
    fun AssignPlayerUidOpNotify(context: LuaContextWrapper, param1Table: Any): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                val param1 = context.engine.getTable(param1Table)
                AssignPlayerUidOpNotify(this@onGroupContext, param1)
            }
        }
    }

    @JvmStatic
    fun CreateTreasureMapSpotRewardGadget(context: LuaContextWrapper, gadgetCfgId: Int): Int {
        return context.onGroupContext {
            onScriptLibHandler {
                CreateTreasureMapSpotRewardGadget(this@onGroupContext, gadgetCfgId)
            }
        }
    }




    /* GroupEntityHandler */

    @JvmStatic
    fun KillEntityByConfigId(context: LuaContextWrapper, rawTable: Any): Int {
        val table = context.engine.getTable(rawTable)
        val configId = table.optInt("config_id", 0)
        val groupId = table.optInt("group_id", 0)
        checkGroupIdAndConfigId(ScriptLib::KillEntityByConfigId, groupId, configId)?.let {
            return it.getValue()
        }
        val entityTypeValue = table.optInt("entity_type", 0)
        if (entityTypeValue < 0 || entityTypeValue >= EntityType.entries.size) {
            scriptLogger.error { "[KillEntityByConfigId] Invalid entity type $entityTypeValue" }
            return ScriptLibErrors.INVALID_PARAMETER.getValue()
        }
        val entityType = EntityType.entries[entityTypeValue]
        val params = KillByConfigIdParams(configId, groupId, entityType)
        return context.onTypedContext(
            {
                onGroupEntityHandler {
                    killEntityByConfigId(this@onTypedContext, params)
                }
            },
            {
                onGadgetControllerHandler {
                    killEntityByConfigId(this@onTypedContext, params)
                }
            }
        ) {
            scriptLogger.error { "[KillEntityByConfigId] unknown context type " + this.javaClass.name }
        }
    }

    @JvmStatic
    fun RemoveEntityByConfigId(context: LuaContextWrapper, groupId: Int, entityTypeValue: Int, configId: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                checkGroupId(ScriptLib::RemoveEntityByConfigId, groupId)?.let {
                    return@onGroupEntityHandler it.getValue()
                }
                val entityType = EntityType.entries[entityTypeValue]
                removeEntityByConfigId(this@onGroupContext, groupId, entityType, configId)
            }
        }
    }

    @JvmStatic
    fun KillGroupEntity(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                val table = context.engine.getTable(rawTable)
                val groupId = table.optInt("group_id", -1)
                val killPolicyId = table.optInt("kill_policy", -1)
                checkGroupId(ScriptLib::KillGroupEntity, groupId)?.let {
                    return@onGroupEntityHandler it.getValue()
                }
                if (killPolicyId == -1) {
                    return@onGroupEntityHandler killByCfgIds(this@onGroupContext, groupId, table)
                }
                return@onGroupEntityHandler killByGroupPolicy(this@onGroupContext, groupId, killPolicyId)
            }
        }
    }

    private fun killByGroupPolicy(context: GroupEventLuaContext, groupId: Int, killPolicyId: Int): Int {
        scriptLogger.debug { "[KillGroupEntity] kill by group policy" }
        if (killPolicyId >= GroupKillPolicy.entries.size) {
            scriptLogger.error { "[KillGroupEntity] kill_policy out of bounds" }
            return ScriptLibErrors.INVALID_PARAMETER.getValue()
        }
        val policy = GroupKillPolicy.entries[killPolicyId]
        return context.onGroupEntityHandler {
            killGroupEntityByPolicy(context, groupId, policy)
        }
    }

    private fun killByCfgIds(context: GroupEventLuaContext, groupId: Int, luaTable: LuaTable): Int {
        scriptLogger.debug { "[KillGroupEntity] kill by cfg ids" }
        val monsterList = luaTable.getTable("monsters")
        val gadgetList = luaTable.getTable("gadgets")
        val monsters = monsterList?.getAsIntArray() ?: IntArray(0)
        val gadgets = gadgetList?.getAsIntArray() ?: IntArray(0)

        return context.onGroupEntityHandler {
            killGroupEntityByCfgIds(context, groupId, monsters, gadgets)
        }
    }


    @JvmStatic
    fun DelAllSubEntityByOriginOwnerConfigId(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                checkConfigId(ScriptLib::DelAllSubEntityByOriginOwnerConfigId, configId)?.let {
                    return@onGroupEntityHandler it.getValue()
                }
                delAllSubEntityByOriginOwnerConfigId(this@onGroupContext, configId)
            }
        }
    }


    @JvmStatic
    fun GetEntityIdByConfigId(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                checkConfigId(ScriptLib::GetEntityIdByConfigId, configId)?.let {
                    return@onGroupEntityHandler it.getValue()
                }
                getEntityIdByConfigId(this@onGroupContext, configId)
            }
        }
    }

    @JvmStatic
    fun GetTeamEntityIdByUid(context: LuaContextWrapper, uid: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                checkUid(ScriptLib::GetTeamEntityIdByUid, uid)?.let {
                    return@onGroupEntityHandler it.getValue()
                }
                getTeamEntityIdByUid(this@onGroupContext, uid)
            }
        }
    }

    @JvmStatic
    fun GetAvatarEntityIdByUid(context: LuaContextWrapper, uid: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                checkUid(ScriptLib::GetAvatarEntityIdByUid, uid)?.let {
                    return@onGroupEntityHandler it.getValue()
                }
                getAvatarEntityIdByUid(this@onGroupContext, uid)
            }
        }
    }

    @JvmStatic
    fun GetConfigIdByEntityId(context: LuaContextWrapper, entityID: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                getConfigIdByEntityId(this@onGroupContext, entityID)
            }
        }
    }

    @JvmStatic
    fun GetTeamUidByEntityId(context: LuaContextWrapper, entityID: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                getTeamUidByEntityId(this@onGroupContext, entityID)
            }
        }
    }

    @JvmStatic
    fun GetUidByTeamEntityId(context: LuaContextWrapper, entityID: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                getUidByTeamEntityId(this@onGroupContext, entityID)
            }
        }
    }


    @JvmStatic
    fun GetPosByEntityId(context: LuaContextWrapper, entityId: Int): Any {
        return context.onGroupContext {
            onGroupEntityHandler {
                val pos = getPosByEntityId(this@onGroupContext, entityId)
                ScriptUtils.posToLua(pos, context.engine).getRawTable()
            }
        }
    }

    @JvmStatic
    fun GetRotationByEntityId(context: LuaContextWrapper, entityId: Int): Any {
        return context.onGroupContext {
            onGroupEntityHandler {
                val rot = getRotationByEntityId(this@onGroupContext, entityId)
                ScriptUtils.posToLua(rot, context.engine).getRawTable()
            }
        }
    }

    /*                    */
    /* GroupGadgetHandler */
    /*                    */

    @JvmStatic
    fun SetWorktopOptionsByGroupId(context: LuaContextWrapper, groupId: Int, configId: Int, optionsTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkGroupIdAndConfigId(::SetWorktopOptionsByGroupId, groupId, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                val options = context.engine.getTable(optionsTable)
                setWorktopOptionsByGroupId(this@onGroupContext, groupId, configId, options)
            }
        }
    }

    @JvmStatic
    fun SetWorktopOptions(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val table = context.engine.getTable(rawTable)
                setWorktopOptions(this@onGroupContext, table)
            }
        }
    }

    @JvmStatic
    fun DelWorktopOptionByGroupId(context: LuaContextWrapper, groupId: Int, configId: Int, option: Int): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkGroupIdAndConfigId(::DelWorktopOptionByGroupId, groupId, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                delWorktopOptionByGroupId(this@onGroupContext, groupId, configId, option)
            }
        }
    }

    @JvmStatic
    fun DelWorktopOption(context: LuaContextWrapper, var1: Int): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                delWorktopOption(this@onGroupContext, var1)
            }
        }
    }

    @JvmStatic
    fun CreateGadget(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val table = context.engine.getTable(rawTable)
                createGadget(this@onGroupContext, table)
            }
        }
    }

    /**
     * Spawn a gadget from the caller group at the specified position
     * @param configId The config id of the gadget in the calling group
     * @param posTable The position to spawn the gadget at
     * @param rotTable The rotation of the gadget when spawned
     */
    @JvmStatic
    fun CreateGadgetByConfigIdByPos(context: LuaContextWrapper, configId: Int, posTable: Any, rotTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val luaPos = context.engine.getTable(posTable)
                val luaRot = context.engine.getTable(rotTable)
                createGadgetByConfigIdByPos(this@onGroupContext, configId, ScriptUtils.luaToPos(luaPos), ScriptUtils.luaToPos(luaRot))
            }
        }
    }

    /**
     * TODO parse the table before passing it to the handler
     * Spawns a gadget based on the caller groups gadget with cfg id matching the specified id. It also applies additional parameters based on the parameters
     * @param creationParamsTable parameters to spawn a gadget with
     */
    @JvmStatic
    fun CreateGadgetByParamTable(context: LuaContextWrapper, creationParamsTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val table = context.engine.getTable(creationParamsTable)
                createGadgetByParamTable(this@onGroupContext, table)
            }
        }
    }

    /**
     * Change the state of a gadget in the current group
     * @param context The context of the group event
     * @param configId config id of a gadget in the current caller group
     * @param gadgetState target state for the gadget
     */
    @JvmStatic
    fun SetGadgetStateByConfigId(context: LuaContextWrapper, configId: Int, gadgetState: Int): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkConfigId(::SetGadgetStateByConfigId, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                setGadgetStateByConfigId(this@onGroupContext, configId, gadgetState)
            }
        }
    }


    /**
     * Change the state of a gadget in the defined group
     * @param context The context of the group event
     * @param groupId The group containing the target gadget or the caller group if 0
     * @param configId config id of a gadget in the target group
     * @param gadgetState target state for the gadget
     */
    @JvmStatic
    fun SetGroupGadgetStateByConfigId(
        context: LuaContextWrapper,
        groupId: Int,
        configId: Int,
        gadgetState: Int
    ): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkGroupIdAndConfigId(::SetGroupGadgetStateByConfigId, groupId, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                setGroupGadgetStateByConfigId(this@onGroupContext, groupId, configId, gadgetState)
            }
        }
    }

    /**
     * Returns the state of a gadget based on the group id and config id
     * @param context The context of the group event
     * @param groupId group to search for the gadget in, 0 for the caller group.
     * @param configId config id of the gadget in the group.
     */
    @JvmStatic
    fun GetGadgetStateByConfigId(context: LuaContextWrapper, groupId: Int, configId: Int): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkGroupIdAndConfigId(::GetGadgetStateByConfigId, groupId, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                getGadgetStateByConfigId(this@onGroupContext, groupId, configId)
            }
        }
    }


    /**
     * Change the state if a gadget in the current group, based in the parametersTable
     * @param context The context of the group event
     * @param parametersTable The parameter table, contains the following fields: config_id:int, state:int
     */
    @JvmStatic
    fun ChangeGroupGadget(context: LuaContextWrapper, parametersTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val table = context.engine.getTable(parametersTable)
                val configId = table.optInt("config_id", -1)
                val state = table.optInt("state", -1)
                checkConfigId(::ChangeGroupGadget, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                if (state < 0) {
                    scriptLogger.error { "[ChangeGroupGadget] Invalid state ($state)" }
                    return@onGroupGadgetHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                changeGroupGadget(this@onGroupContext, configId, state)
            }
        }
    }

    @JvmStatic
    fun SetGadgetTalkByConfigId(context: LuaContextWrapper, groupId: Int, configId: Int, talkId: Int): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkGroupIdAndConfigId(::SetGadgetTalkByConfigId, groupId, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                setGadgetTalkByConfigId(this@onGroupContext, groupId, configId, talkId)
            }
        }
    }

    @JvmStatic
    fun SetGadgetHp(context: LuaContextWrapper, groupId: Int, configId: Int, hpPercent: Int): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkGroupIdAndConfigId(::SetGadgetHp, groupId, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                setGadgetHp(this@onGroupContext, groupId, configId, hpPercent)
            }
        }
    }

    /**
     * Retrieves and returns the gadget id of a gadget entity based on the entity id.
     * @param context The context of the group event
     * @param entityId The entity id of the gadget requested.
     */
    @JvmStatic
    fun GetGadgetIdByEntityId(context: LuaContextWrapper, entityId: Int): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                if (entityId <= 0) {
                    scriptLogger.error { "[GetGadgetIdByEntityId] Invalid or missing entityId $entityId" }
                    return@onGroupGadgetHandler ScriptLibErrors.INVALID_PARAMETER.getValue()
                }
                getGadgetIdByEntityId(this@onGroupContext, entityId)
            }
        }
    }

    /**
     * // TODO identify unknown parameters and exact behaviour
     * Executes a lua function on a gadgets lua controller.
     * This seems to be used in only the Crucible activity and might also trigger OnClientExecuteReq
     * @param groupId group to find the gadget in
     * @param gadgetCfgId cfg id of the gadget in the group to execute lua in
     * @param activityType seems to be an activity type
     * @param var4 TODO
     * @param val5 TODO
     */
    @JvmStatic
    fun ExecuteGadgetLua(
        context: LuaContextWrapper,
        groupId: Int,
        gadgetCfgId: Int,
        activityType: Int,
        var4: Int,
        val5: Int
    ): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkGroupIdAndConfigId(::ExecuteGadgetLua, groupId, gadgetCfgId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                executeGadgetLua(this@onGroupContext, groupId, gadgetCfgId, activityType, var4, val5)
            }
        }
    }

    /**
     * Returns the config id of the gadget with the eid (gadget_eid)
     * @param context The context of the group event
     * @param paramsTable The parameter table, contains only `gadget_eid`, which contains the entity id of the gadget requested.
     */
    @JvmStatic
    fun GetGadgetConfigId(context: LuaContextWrapper, paramsTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val params = context.engine.getTable(paramsTable)
                val gadgetEid = params.optInt("gadget_eid", -1)
                if (gadgetEid <= 0) {
                    scriptLogger.error { "[GetGadgetConfigId] Invalid or missing gadget_eid $gadgetEid" }
                    return@onGroupGadgetHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                getGadgetConfigId(this@onGroupContext, gadgetEid)
            }
        }
    }

    /**
     * Returns the hp in percent of a gadget based on the group id and config id
     * @param context The context of the group event
     * @param groupId group to search for the gadget entity in.
     * @param configId config id of the gadget in the group.
     */
    @JvmStatic
    fun GetGadgetHpPercent(context: LuaContextWrapper, groupId: Int, configId: Int): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkGroupIdAndConfigId(::GetGadgetHpPercent, groupId, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                getGadgetHpPercent(this@onGroupContext, groupId, configId)
            }
        }
    }

    /* GroupMonsterHandler */

    @JvmStatic
    fun CreateMonsterFaceAvatar(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                val table = context.engine.getTable(rawTable)
                val entityId = table.optInt("entity_id", -1)
                val monsters = table.getTable("monsters")?.getAsIntArray()?.toList()
                val angle = table.optInt("angle", -1)
                val ranges = table.getTable("ranges")?.let {
                    val list = it.getAsIntArray()
                    if(list.size != 2){
                        scriptLogger.error { "[CreateMonsterFaceAvatar] Invalid ranges size ${it.getSize()}" }
                        return@onGroupMonsterHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                    }
                    Pair(list[0], list[1])
                }
                if(monsters == null || ranges == null || angle == -1 || entityId == -1){
                    scriptLogger.error { "[CreateMonsterFaceAvatar] Invalid or missing monsters, ranges, angle or entityId" }
                    return@onGroupMonsterHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                createMonsterFaceAvatar(this@onGroupContext, MonsterFaceAvatarParameters(entityId, monsters, ranges, angle))
            }
        }
    }

    @JvmStatic
    fun GetMonsterIdByEntityId(context: LuaContextWrapper, entityId: Int): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                getMonsterIdByEntityId(this@onGroupContext, entityId)
            }
        }
    }

    @JvmStatic
    fun GetMonsterConfigId(context: LuaContextWrapper, entityId: Int): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                getMonsterConfigId(this@onGroupContext, entityId)
            }
        }
    }

    @JvmStatic
    fun GetMonsterHpPercent(context: LuaContextWrapper, groupId: Int, configId: Int): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                checkGroupIdAndConfigId(::GetMonsterHpPercent, groupId, configId)?.let {
                    return@onGroupMonsterHandler it.getValue()
                }
                getMonsterHpPercent(this@onGroupContext, groupId, configId)
            }
        }
    }

    @JvmStatic
    fun GetMonsterAffixListByConfigId(context: LuaContextWrapper, groupId: Int, configId: Int): IntArray {
        return context.onGroupContext {
            onGroupMonsterHandler {
                checkGroupIdAndConfigId(::GetMonsterAffixListByConfigId, groupId, configId)?.let {
                    return@onGroupMonsterHandler intArrayOf(it.getValue())
                }
                getMonsterAffixListByConfigId(this@onGroupContext, groupId, configId).toIntArray()
            }
        }
    }

    @JvmStatic
    fun LockMonsterHp(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                checkConfigId(::LockMonsterHp, configId)?.let {
                    return@onGroupMonsterHandler it.getValue()
                }
                lockMonsterHp(this@onGroupContext, configId)
            }
        }
    }

    @JvmStatic
    fun UnlockMonsterHp(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                checkConfigId(::UnlockMonsterHp, configId)?.let {
                    return@onGroupMonsterHandler it.getValue()
                }
                unlockMonsterHp(this@onGroupContext, configId)
            }
        }
    }

    @JvmStatic
    fun SetMonsterHp(context: LuaContextWrapper, groupId: Int, configId: Int, hpPercent: Int): Int  {
        return context.onGroupContext {
            onGroupMonsterHandler {
                checkGroupIdAndConfigId(::setMonsterHp, groupId, configId)?.let {
                    return@onGroupMonsterHandler it.getValue()
                }
                setMonsterHp(this@onGroupContext, groupId, configId, hpPercent)
            }
        }
    }

    @JvmStatic
    fun SetMonsterAIByGroup(context: LuaContextWrapper, aiId: Int, configId: Int, groupId: Int): Int  {
        return context.onGroupContext {
            onGroupMonsterHandler {
                checkGroupIdAndConfigId(::SetMonsterAIByGroup, groupId, configId)?.let {
                    return@onGroupMonsterHandler it.getValue()
                }
                setMonsterAIByGroup(this@onGroupContext, aiId, groupId, configId)
            }
        }
    }

    @JvmStatic
    fun CreateMonster(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                val table = context.engine.getTable(rawTable)
                createMonster(this@onGroupContext, table)
            }
        }
    }

    @JvmStatic
    fun CreateMonsterWithGlobalValue(context: LuaContextWrapper, configId: Int, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                val table = context.engine.getTable(rawTable)
                val keys = table.getKeys()
                val paramMap = keys.mapNotNull { key ->
                    key?.let {
                        key to table.getInt(key)
                    }
                }.toMap()
                createMonsterWithGlobalValue(this@onGroupContext, configId, paramMap)
            }
        }
    }

    @JvmStatic
    fun CreateMonsterByConfigIdByPos(
        context: LuaContextWrapper,
        configId: Int,
        rawPosTable: Any,
        rawRotTable: Any
    ): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                val posTable = context.engine.getTable(rawPosTable)
                val rotTable = context.engine.getTable(rawRotTable)
                val pos = ScriptUtils.luaToPos(posTable)
                    ?: return@onGroupMonsterHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                val rot = ScriptUtils.luaToPos(rotTable)
                    ?: return@onGroupMonsterHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()

                createMonsterByConfigIdByPos(this@onGroupContext, configId, pos, rot)
            }
        }
    }

    @JvmStatic
    fun CreateMonstersFromMonsterPool(context: LuaContextWrapper, poolName: String): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                createMonstersFromMonsterPool(this@onGroupContext, poolName)
            }
        }
    }


    /*                      */
    /* AbilityScriptHandler */
    /*                      */


    @JvmStatic
    fun AddEntityGlobalFloatValueByConfigId(
        context: LuaContextWrapper,
        cfgIdTable: Any,
        floatValueKey: String,
        value: Int
    ): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                val cfgIdList = context.engine.getTable(cfgIdTable).getAsIntArray().toList()
                addEntityGlobalFloatValueByConfigId(this@onGroupContext, cfgIdList, floatValueKey, value)
            }
        }
    }

    @JvmStatic
    fun AddTeamEntityGlobalFloatValue(
        context: LuaContextWrapper,
        sceneUidListTable: Any,
        floatValueKey: String,
        value: Int
    ): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                val sceneUidList = context.engine.getTable(sceneUidListTable).getAsIntArray().toList()
                addTeamEntityGlobalFloatValue(this@onGroupContext, sceneUidList, floatValueKey, value)
            }
        }
    }




    /* AbilityScriptHandler */

    /**
     * Returns a float global value from the gadgets ability definitions.
     * @param context The context of the group event
     * @param groupId group to search for the gadget entity in.
     * @param configId config id of the gadget in the group.
     * @param abilitySGVName name of the abilities svg value to get the float value from.
     */
    @JvmStatic
    fun GetGadgetAbilityFloatValue(
        context: LuaContextWrapper,
        groupId: Int,
        configId: Int,
        floatValueKey: String
    ): Float {
        return context.onGroupContext {
            onGroupAbilityHandler {
                checkGroupIdAndConfigId(::GetGadgetAbilityFloatValue, groupId, configId)?.let {
                    return@onGroupAbilityHandler it.getValue().toFloat()
                }
                checkFloatValueKey(::GetGadgetAbilityFloatValue, floatValueKey)?.let {
                    return@onGroupAbilityHandler it.getValue().toFloat()
                }
                getGadgetAbilityFloatValue(this@onGroupContext, groupId, configId, floatValueKey)
            }
        }
    }
    @JvmStatic
    fun GetMonsterAbilityFloatValue(
        context: LuaContextWrapper,
        groupId: Int,
        configId: Int,
        floatValueKey: String
    ): Float {
        return context.onGroupContext {
            onGroupAbilityHandler {
                checkGroupIdAndConfigId(::GetMonsterAbilityFloatValue, groupId, configId)?.let {
                    return@onGroupAbilityHandler it.getValue().toFloat()
                }
                checkFloatValueKey(::GetMonsterAbilityFloatValue, floatValueKey)?.let {
                    return@onGroupAbilityHandler it.getValue().toFloat()
                }
                getMonsterAbilityFloatValue(this@onGroupContext, groupId, configId, floatValueKey)
            }
        }
    }
    @JvmStatic
    fun GetTeamAbilityFloatValue(
        context: LuaContextWrapper,
        uid: Int,
        floatValueKey: String
    ): Float {
        return context.onGroupContext {
            onGroupAbilityHandler {
                checkUid(::GetTeamAbilityFloatValue, uid)?.let {
                    return@onGroupAbilityHandler it.getValue().toFloat()
                }
                checkFloatValueKey(::GetTeamAbilityFloatValue, floatValueKey)?.let {
                    return@onGroupAbilityHandler it.getValue().toFloat()
                }
                getTeamAbilityFloatValue(this@onGroupContext, uid, floatValueKey)
            }
        }
    }

    @JvmStatic
    fun SetTeamEntityGlobalFloatValue(
        context: LuaContextWrapper,
        sceneUidListTable: Any,
        floatValueKey: String,
        value: Float
    ): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                val sceneUidList = context.engine.getTable(sceneUidListTable).getAsIntArray().toList()
                setTeamEntityGlobalFloatValue(this@onGroupContext, sceneUidList, floatValueKey, value)
            }
        }
    }

    @JvmStatic
    fun SetTeamServerGlobalValue(context: LuaContextWrapper, sceneUid: Int, sgvName: String, value: Int): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                setTeamServerGlobalValue(this@onGroupContext, sceneUid, sgvName, value)
            }
        }
    }

    @JvmStatic
    fun AddTeamServerGlobalValue(context: LuaContextWrapper, ownerId: Int, sgvName: String, value: Int): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                addTeamServerGlobalValue(this@onGroupContext, ownerId, sgvName, value)
            }
        }
    }

    @JvmStatic
    fun GetTeamServerGlobalValue(context: LuaContextWrapper, ownerId: Int, sgvName: String, value: Int): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                getTeamServerGlobalValue(this@onGroupContext, ownerId, sgvName, value)
            }
        }
    }

    @JvmStatic
    fun SetEntityServerGlobalValueByConfigId(
        context: LuaContextWrapper,
        cfgId: Int,
        sgvName: String,
        value: Int
    ): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                checkConfigId(::SetEntityServerGlobalValueByConfigId, cfgId)?.let {
                    return@onGroupAbilityHandler it.getValue()
                }
                setEntityServerGlobalValueByConfigId(this@onGroupContext, cfgId, sgvName, value)
            }
        }
    }

    @JvmStatic
    fun SetEntityServerGlobalValueByEntityId(
        context: LuaContextWrapper,
        entityId: Int,
        sgvName: String,
        value: Int
    ): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                setEntityServerGlobalValueByEntityId(this@onGroupContext, entityId, sgvName, value)
            }
        }
    }

    @JvmStatic
    fun SetGroupLogicStateValue(context: LuaContextWrapper, sgvName: String, value: Int): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                setGroupLogicStateValue(this@onGroupContext, sgvName, value)
            }
        }
    }

    /* QuestScriptHandler */

    @JvmStatic
    fun AddQuestProgress(context: LuaContextWrapper, eventNotifyName: String): Int {
        return context.onGroupContext {
            onQuestHandler {
                addQuestProgress(this@onGroupContext, eventNotifyName)
            }
        }
    }

    @JvmStatic
    fun GetHostQuestState(context: LuaContextWrapper, questId: Int): Int {
        return context.onGroupContext {
            onQuestHandler {
                getHostQuestState(this@onGroupContext, questId).getValue()
            }
        }
    }

    @JvmStatic
    fun GetQuestState(context: LuaContextWrapper, entityId: Int, questId: Int): Int {
        return context.onGroupContext {
            onQuestHandler {
                getQuestState(this@onGroupContext, entityId, questId).getValue()
            }
        }
    }

    @JvmStatic
    fun GetQuestStateByUid(context: LuaContextWrapper, uid: Int, questId: Int): Int {
        return context.onGroupContext {
            onQuestHandler {
                getQuestStateByUid(this@onGroupContext, uid, questId).getValue()
            }
        }
    }


    /* ChallengeScriptHandler */

    @JvmStatic
    fun ActiveChallenge(
        context: LuaContextWrapper,
        challengeIndex: Int,
        challengeId: Int,
        timeLimitOrGroupId: Int,
        groupId: Int,
        objectiveKills: Int,
        param5: Int
    ): Int {
        return context.onGroupContext {
            onChallengeHandler {
                activeChallenge(this@onGroupContext, challengeIndex, challengeId, timeLimitOrGroupId, groupId, objectiveKills, param5)
            }
        }
    }

    @JvmStatic
    fun StartChallenge(
        context: LuaContextWrapper,
        challengeIndex: Int,
        challengeId: Int,
        challengeParams: Any
    ): Int {
        return context.onGroupContext {
            onChallengeHandler {
                val challengeParamTable = context.engine.getTable(challengeParams)
                startChallenge(this@onGroupContext, challengeIndex, challengeId, challengeParamTable)
            }
        }
    }

    @JvmStatic
    fun StopChallenge(context: LuaContextWrapper, challengeIndex: Int, result: Int): Int {
        return context.onGroupContext {
            onChallengeHandler {
                stopChallenge(this@onGroupContext, challengeIndex, result)
            }
        }
    }

    @JvmStatic
    fun PauseChallenge(context: LuaContextWrapper, challengeIndex: Int): Int {
        return context.onGroupContext {
            onChallengeHandler {
                pauseChallenge(this@onGroupContext, challengeIndex)
            }
        }
    }

    @JvmStatic
    fun GetChallengeTransaction(context: LuaContextWrapper, challengeId: Int): Int {
        return context.onGroupContext {
            onChallengeHandler {
                getChallengeTransaction(this@onGroupContext, challengeId)
            }
        }
    }

    @JvmStatic
    fun IsChallengeStartedByChallengeId(context: LuaContextWrapper, challengeId: Int): Boolean {
        return context.onGroupContext {
            onChallengeHandler {
                isChallengeStartedByChallengeId(this@onGroupContext, challengeId)
            }
        }
    }

    @JvmStatic
    fun IsChallengeStartedByChallengeIndex(context: LuaContextWrapper, groupId: Int, challengeIndex: Int): Boolean {
        return context.onGroupContext {
            onChallengeHandler {
                checkGroupId(::IsChallengeStartedByChallengeIndex, groupId)?.let {
                    return@onChallengeHandler false
                }
                isChallengeStartedByChallengeIndex(this@onGroupContext, groupId, challengeIndex)
            }
        }
    }

    /**
     * Adds or removed time from the challenge
     * TODO verify and implement
     * @param context
     * @param challengeId The active target challenges id
     * @param duration The duration to add or remove
     * @return 0 if success, 1 if no challenge is active, 2 if the challenge id doesn't match the active challenge,
     * 3 if modifying the duration failed
     */
    @JvmStatic
    fun AddChallengeDuration(context: LuaContextWrapper, challengeId: Int, duration: Int): Int {
        return context.onGroupContext {
            onChallengeHandler {
                addChallengeDuration(this@onGroupContext, challengeId, duration)
            }
        }
    }
    @JvmStatic
    fun SetChallengeDuration(context: LuaContextWrapper, challengeId: Int, time: Int): Int {
        return context.onGroupContext {
            onChallengeHandler {
                setChallengeDuration(this@onGroupContext, challengeId, time)
            }
        }
    }

    @JvmStatic
    fun CreateFatherChallenge(
        context: LuaContextWrapper,
        challengeIndex: Int,
        challengeId: Int,
        timeLimit: Int,
        conditionTable: Any
    ): Int {
        return context.onGroupContext {
            onChallengeHandler {
                val conditionLuaTable = context.engine.getTable(conditionTable)
                createFatherChallenge(this@onGroupContext, challengeIndex, challengeId, timeLimit, conditionLuaTable)
            }
        }
    }

    @JvmStatic
    fun StartFatherChallenge(context: LuaContextWrapper, challengeIndex: Int): Int {
        return context.onGroupContext {
            onChallengeHandler {
                startFatherChallenge(this@onGroupContext, challengeIndex)
            }
        }
    }

    @JvmStatic
    fun EndFatherChallenge(context: LuaContextWrapper, fatherChallengeIndex: Int): Int {
        return context.onGroupContext {
            onChallengeHandler {
                endFatherChallenge(this@onGroupContext, fatherChallengeIndex)
            }
        }
    }

    @JvmStatic
    fun ModifyFatherChallengeProperty(
        context: LuaContextWrapper,
        challengeId: Int,
        propertyTypeIndex: Int,
        value: Int
    ): Int {
        return context.onGroupContext {
            onChallengeHandler {
                val propertyType = FatherChallengeProperty.entries[propertyTypeIndex]
                modifyFatherChallengeProperty(this@onGroupContext, challengeId, propertyType, value)
            }
        }
    }

    @JvmStatic
    fun SetChallengeEventMark(context: LuaContextWrapper, challengeId: Int, markType: Int): Int {
        return context.onGroupContext {
            onChallengeHandler {
                if (markType < 0 || markType >= ChallengeEventMarkType.entries.size) {
                    scriptLogger.error { "[SetChallengeEventMark] Invalid mark type $markType" }
                    return@onChallengeHandler 1
                }
                val markTypeEnum = ChallengeEventMarkType.entries[markType]
                setChallengeEventMark(this@onGroupContext, challengeId, markTypeEnum)
            }
        }
    }

    @JvmStatic
    fun AttachChildChallenge(
        context: LuaContextWrapper, fatherChallengeIndex: Int, childChallengeIndex: Int,
        childChallengeId: Int, var4Table: Any, var5Table: Any, var6Table: Any
    ): Int {
        return context.onGroupContext {
            onChallengeHandler {
                val conditionArray = context.engine.getTable(var4Table)
                val var5 = context.engine.getTable(var5Table)
                val conditionTable = context.engine.getTable(var6Table)
                attachChildChallenge(this@onGroupContext, fatherChallengeIndex, childChallengeIndex, childChallengeId, conditionArray, var5, conditionTable)
            }
        }
    }


    /* DeathZoneScriptHandler */

    @JvmStatic
    fun ChangeDeathZone(context: LuaContextWrapper, deathZoneId: Int, rawParamsTable: Any): Int {
        return context.onGroupContext {
            onDeathZoneHandler {
                val conditionArray = context.engine.getTable(rawParamsTable)
                val isOpen = conditionArray.optBoolean("is_open", false)
                changeDeathZone(this@onGroupContext, deathZoneId, isOpen)
            }
        }
    }

    @JvmStatic
    fun GetDeathZoneStatus(context: LuaContextWrapper, deathZoneId: Int): Int {
        return context.onGroupContext {
            onDeathZoneHandler {
                getDeathZoneStatus(this@onGroupContext, deathZoneId)
            }
        }
    }


    /* DungeonScriptHandler */

    @JvmStatic
    fun CauseDungeonFail(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onDungeonHandler {
                causeDungeonFail(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun CauseDungeonSuccess(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onDungeonHandler {
                causeDungeonSuccess(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun EnterPersistentDungeon(context: LuaContextWrapper, dungeonId: Int, var2: Int, posTable: Any, rotTable: Any): Int {
        return context.onGroupContext {
            onDungeonHandler {
                val luaPos = context.engine.getTable(posTable)
                val luaRot = context.engine.getTable(rotTable)

                enterPersistentDungeon(this@onGroupContext, dungeonId, var2, ScriptUtils.luaToPos(luaPos), ScriptUtils.luaToPos(luaRot))
            }
        }
    }

    /**
     * Activates a dungeon checkpoint.
     * @param context a group event lua context
     * @param pointId the scene point id of the dungeon checkpoint
     * @return 0 if successful, 1 if dungeon manager is null, 2 if dungeon manager failed to activate the checkpoint
     */
    @JvmStatic
    fun ActivateDungeonCheckPoint(context: LuaContextWrapper, pointId: Int): Int {
        return context.onGroupContext {
            onDungeonHandler {
                activateDungeonCheckPoint(this@onGroupContext, pointId)
            }
        }
    }

    @JvmStatic
    fun GetDungeonTeamPlayerNum(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onDungeonHandler {
                getDungeonTeamPlayerNum(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetDungeonTransaction(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onDungeonHandler {
                getDungeonTransaction(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetOpeningDungeonListByRosterId(context: LuaContextWrapper, rosterId: Int): IntArray {
        return context.onGroupContext {
            onDungeonHandler {
                getOpeningDungeonListByRosterId(this@onGroupContext, rosterId).toIntArray()
            }
        }
    }


    /* GroupManagerScriptHandler */

    @JvmStatic
    fun GoToGroupSuite(context: LuaContextWrapper, groupId: Int, suite: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndSuiteId(::GoToGroupSuite, groupId, suite)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                goToGroupSuite(this@onGroupContext, groupId, suite)
            }
        }
    }

    @JvmStatic
    fun GoToFlowSuite(context: LuaContextWrapper, groupId: Int, suite: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndSuiteId(::GoToFlowSuite, groupId, suite)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                goToFlowSuite(this@onGroupContext, groupId, suite)
            }
        }
    }

    @JvmStatic
    fun SetFlowSuite(context: LuaContextWrapper, groupId: Int, suite: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndSuiteId(::SetFlowSuite, groupId, suite)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                setFlowSuite(this@onGroupContext, groupId, suite)
            }
        }
    }

    @JvmStatic
    fun AddExtraGroupSuite(context: LuaContextWrapper, groupId: Int, suite: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndSuiteId(ScriptLib::AddExtraGroupSuite, groupId, suite)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                addExtraGroupSuite(this@onGroupContext, groupId, suite)
            }
        }
    }

    @JvmStatic
    fun RemoveExtraGroupSuite(context: LuaContextWrapper, groupId: Int, suite: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndSuiteId(ScriptLib::RemoveExtraGroupSuite, groupId, suite)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                removeExtraGroupSuite(this@onGroupContext, groupId, suite)
            }
        }
    }

    @JvmStatic
    fun KillExtraGroupSuite(context: LuaContextWrapper, groupId: Int, suite: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndSuiteId(ScriptLib::KillExtraGroupSuite, groupId, suite)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                killExtraGroupSuite(this@onGroupContext, groupId, suite)
            }
        }
    }

    @JvmStatic
    fun AddExtraFlowSuite(context: LuaContextWrapper, groupId: Int, suiteId: Int, flowSuitePolicy: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndSuiteId(ScriptLib::AddExtraFlowSuite, groupId, suiteId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                if (flowSuitePolicy < 0 || flowSuitePolicy >= FlowSuiteOperatePolicy.entries.size) {
                    scriptLogger.error { "[AddExtraFlowSuite] Invalid flow suite policy $flowSuitePolicy" }
                    return@onGroupManagementHandler 1
                }
                val flowSuitePolicyEnum = FlowSuiteOperatePolicy.entries[flowSuitePolicy]
                addExtraFlowSuite(this@onGroupContext, groupId, suiteId, flowSuitePolicyEnum)
            }
        }
    }

    @JvmStatic
    fun RemoveExtraFlowSuite(context: LuaContextWrapper, groupId: Int, suiteId: Int, flowSuitePolicy: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndSuiteId(ScriptLib::RemoveExtraFlowSuite, groupId, suiteId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                if (flowSuitePolicy < 0 || flowSuitePolicy >= FlowSuiteOperatePolicy.entries.size) {
                    scriptLogger.error { "[RemoveExtraFlowSuite] Invalid flow suite policy $flowSuitePolicy" }
                    return@onGroupManagementHandler 1
                }
                val flowSuitePolicyEnum = FlowSuiteOperatePolicy.entries[flowSuitePolicy]
                removeExtraFlowSuite(this@onGroupContext, groupId, suiteId, flowSuitePolicyEnum)
            }
        }
    }

    @JvmStatic
    fun KillExtraFlowSuite(context: LuaContextWrapper, groupId: Int, suiteId: Int, flowSuitePolicy: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndSuiteId(ScriptLib::KillExtraFlowSuite, groupId, suiteId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                if (flowSuitePolicy < 0 || flowSuitePolicy >= FlowSuiteOperatePolicy.entries.size) {
                    scriptLogger.error { "[KillExtraFlowSuite] Invalid flow suite policy $flowSuitePolicy" }
                    return@onGroupManagementHandler 1
                }
                val flowSuitePolicyEnum = FlowSuiteOperatePolicy.entries[flowSuitePolicy]
                killExtraFlowSuite(this@onGroupContext, groupId, suiteId, flowSuitePolicyEnum)
            }
        }
    }

    /**
     * Set the actions and triggers to designated group
     */
    @JvmStatic
    fun RefreshGroup(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                val table = context.engine.getTable(rawTable)
                refreshGroup(this@onGroupContext, table)
            }
        }
    }

    @JvmStatic
    fun GetGroupSuite(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(ScriptLib::GetGroupSuite, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                getGroupSuite(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun SetGroupReplaceable(context: LuaContextWrapper, groupId: Int, value: Boolean): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(ScriptLib::SetGroupReplaceable, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                setGroupReplaceable(this@onGroupContext, groupId, value)
            }
        }
    }

    @JvmStatic
    fun ActivateGroupLinkBundle(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::ActivateGroupLinkBundle, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                activateGroupLinkBundle(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun ActivateGroupLinkBundleByBundleId(context: LuaContextWrapper, bundleId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                activateGroupLinkBundleByBundleId(this@onGroupContext, bundleId)
            }
        }
    }

    @JvmStatic
    fun DeactivateGroupLinkBundle(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::DeactivateGroupLinkBundle, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                deactivateGroupLinkBundle(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun DeactivateGroupLinkBundleByBundleId(context: LuaContextWrapper, bundleId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                deactivateGroupLinkBundleByBundleId(this@onGroupContext, bundleId)
            }
        }
    }

    @JvmStatic
    fun FinishGroupLinkBundle(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::FinishGroupLinkBundle, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                finishGroupLinkBundle(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun CreateGroupVariable(context: LuaContextWrapper, varName: String, value: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                createGroupVariable(this@onGroupContext, varName, value)
            }
        }
    }

    @JvmStatic
    fun GetGroupVariableValue(context: LuaContextWrapper, varName: String): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                getGroupVariableValue(this@onGroupContext, varName)
            }
        }
    }

    @JvmStatic
    fun GetGroupVariableValueByGroup(context: LuaContextWrapper, name: String, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::GetGroupVariableValueByGroup, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                getGroupVariableValueByGroup(this@onGroupContext, name, groupId)
            }
        }
    }

    @JvmStatic
    fun SetGroupVariableValue(context: LuaContextWrapper, varName: String, value: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                setGroupVariableValue(this@onGroupContext, varName, value)
            }
        }
    }

    @JvmStatic
    fun SetGroupVariableValueByGroup(context: LuaContextWrapper, key: String, value: Int, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::SetGroupVariableValueByGroup, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                setGroupVariableValueByGroup(this@onGroupContext, key, value, groupId)
            }
        }
    }

    @JvmStatic
    fun ChangeGroupVariableValue(context: LuaContextWrapper, varName: String, value: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                changeGroupVariableValue(this@onGroupContext, varName, value)
            }
        }
    }

    @JvmStatic
    fun ChangeGroupVariableValueByGroup(context: LuaContextWrapper, name: String, value: Int, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::ChangeGroupVariableValueByGroup, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                changeGroupVariableValueByGroup(this@onGroupContext, name, value, groupId)
            }
        }
    }

    @JvmStatic
    fun SetGroupTempValue(context: LuaContextWrapper, name: String, value: Int, var3Table: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                val var3 = context.engine.getTable(var3Table)
                setGroupTempValue(this@onGroupContext, name, value, var3)
            }
        }
    }

    @JvmStatic
    fun GetGroupTempValue(context: LuaContextWrapper, name: String, var2Table: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                val var2 = context.engine.getTable(var2Table)
                getGroupTempValue(this@onGroupContext, name, var2)
            }
        }
    }

    @JvmStatic
    fun ChangeGroupTempValue(context: LuaContextWrapper, name: String, diff: Int, var3Table: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                val var3 = context.engine.getTable(var3Table)
                changeGroupTempValue(this@onGroupContext, name, diff, var3)
            }
        }
    }

    @JvmStatic
    fun CreateGroupTrigger(context: LuaContextWrapper, name: String): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                createGroupTrigger(this@onGroupContext, name)
            }
        }
    }

    @JvmStatic
    fun SetGroupDead(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::SetGroupDead, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                setGroupDead(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun IsGroupRegisteredInCurScene(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::IsGroupRegisteredInCurScene, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                isGroupRegisteredInCurScene(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun UnfreezeGroupLimit(context: LuaContextWrapper, forceId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                unfreezeGroupLimit(this@onGroupContext, forceId)
            }
        }
    }

    /**
     * TODO better parameter handling and verify active handling
     * Calls a lua function in the specified group if the group is active. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    @JvmStatic
    fun ExecuteActiveGroupLua(
        context: LuaContextWrapper,
        groupId: Int,
        functionName: String?,
        callParamsTable: Any
    ): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(ScriptLib::ExecuteActiveGroupLua, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                val callParams = context.engine.getTable(callParamsTable)
                executeActiveGroupLua(this@onGroupContext, groupId, functionName, callParams)
            }
        }
    }

    /**
     * TODO better parameter handling
     * Calls a lua function in the specified group. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    @JvmStatic
    fun ExecuteGroupLua(context: LuaContextWrapper, groupId: Int, functionName: String?, callParamsTable: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(ScriptLib::ExecuteGroupLua, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                val callParams = context.engine.getTable(callParamsTable)
                executeGroupLua(this@onGroupContext, groupId, functionName, callParams)
            }
        }
    }



    /* SceneStateScriptHandler */

    @JvmStatic
    fun UnhideScenePoint(context: LuaContextWrapper, scenePointId: Int): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                unhideScenePoint(this@onGroupContext, scenePointId)
            }
        }
    }

    @JvmStatic
    fun UnlockScenePoint(context: LuaContextWrapper, scenePointId: Int): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                unlockScenePoint(this@onGroupContext, scenePointId)
            }
        }
    }

    @JvmStatic
    fun UnlockForce(context: LuaContextWrapper, force: Int): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                unlockForce(this@onGroupContext, force)
            }
        }
    }

    @JvmStatic
    fun LockForce(context: LuaContextWrapper, force: Int): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                lockForce(this@onGroupContext, force)
            }
        }
    }

    @JvmStatic
    fun AddSceneTag(context: LuaContextWrapper, sceneId: Int, sceneTagId: Int): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                addSceneTag(this@onGroupContext, sceneId, sceneTagId)
            }
        }
    }

    @JvmStatic
    fun DelSceneTag(context: LuaContextWrapper, sceneId: Int, sceneTagId: Int): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                delSceneTag(this@onGroupContext, sceneId, sceneTagId)
            }
        }
    }

    @JvmStatic
    fun CheckSceneTag(context: LuaContextWrapper, sceneId: Int, sceneTagId: Int): Boolean {
        return context.onGroupContext {
            onSceneStateHandler {
                checkSceneTag(this@onGroupContext, sceneId, sceneTagId)
            }
        }
    }


    @JvmStatic
    fun ChangeToTargetLevelTag(context: LuaContextWrapper, var1: Int): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                changeToTargetLevelTag(this@onGroupContext, var1)
            }
        }
    }

    @JvmStatic
    fun ChangeToTargetLevelTagWithParamTable(context: LuaContextWrapper, var1: Int, changeLevelTagParamsTable: Any): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                val paramsTable = context.engine.getTable(changeLevelTagParamsTable)
                val posTable = paramsTable.getTable("pos")
                val rotTable = paramsTable.getTable("rot")
                val radius = paramsTable.optInt("radius", -1)
                val params = ChangeLevelTagParams(ScriptUtils.luaToPos(posTable), ScriptUtils.luaToPos(rotTable), radius)
                changeToTargetLevelTagWithParamTable(this@onGroupContext, var1, params)
            }
        }
    }

    @JvmStatic
    fun GetCurrentLevelTagVec(context: LuaContextWrapper, levelTagGroupId: Int): IntArray {
        return context.onGroupContext {
            onSceneStateHandler {
                getCurrentLevelTagVec(this@onGroupContext, levelTagGroupId).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetLevelTagNameById(context: LuaContextWrapper, levelTagId: Int): String {
        return context.onGroupContext {
            onSceneStateHandler {
                getLevelTagNameById(this@onGroupContext, levelTagId)
            }
        }
    }


    @JvmStatic
    fun IsLevelTagChangeInCD(context: LuaContextWrapper, levelTagGroupId: Int): Boolean {
        return context.onGroupContext {
            onSceneStateHandler {
                isLevelTagChangeInCD(this@onGroupContext, levelTagGroupId)
            }
        }
    }


    /* SealBattleScriptHandler */

    @JvmStatic
    fun StartSealBattle(context: LuaContextWrapper, gadgetId: Int, battleParamsTable: Any): Int {
        return context.onGroupContext {
            onSealBattleHandler {
                val battleParams = context.engine.getTable(battleParamsTable)
                val handlerParams = SealBattleParams.fromSealBattleType(battleParams) ?: run {
                    val battleType = battleParams.optInt("battle_type", -1)
                    scriptLogger.error { "[StartSealBattle] Invalid battle type $battleType" }
                    return@onSealBattleHandler -1
                }
                startSealBattle(this@onGroupContext, gadgetId, handlerParams)
            }
        }
    }


    /* GalleryScriptHandler*/

    @JvmStatic
    fun StartGallery(context: LuaContextWrapper, galleryId: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                startGallery(this@onGroupContext, galleryId)
            }
        }
    }

    @JvmStatic
    fun StopGallery(context: LuaContextWrapper, galleryId: Int, var2: Boolean): Int {
        return context.onGroupContext {
            onGalleryHandler {
                stopGallery(this@onGroupContext, galleryId, var2)
            }
        }
    }

    @JvmStatic
    fun StopGalleryByReason(context: LuaContextWrapper, galleryId: Int, stopReasonId: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                stopGalleryByReason(this@onGroupContext, galleryId, stopReasonId)
            }
        }
    }

    @JvmStatic
    fun UpdatePlayerGalleryScore(context: LuaContextWrapper, galleryId: Int, var2Table: Any): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val var2 = context.engine.getTable(var2Table)
                updatePlayerGalleryScore(this@onGroupContext, galleryId, var2)
            }
        }
    }

    @JvmStatic
    fun GetGalleryTransaction(context: LuaContextWrapper, galleryId: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                getGalleryTransaction(this@onGroupContext, galleryId)
            }
        }
    }

    @JvmStatic
    fun GetGalleryUidList(context: LuaContextWrapper, galleryId: Int): IntArray {
        return context.onGroupContext {
            onGalleryHandler {
                getGalleryUidList(this@onGroupContext, galleryId).toIntArray()
            }
        }
    }

    @JvmStatic
    fun IsGalleryStart(context: LuaContextWrapper, galleryId: Int): Boolean {
        return context.onGroupContext {
            onGalleryHandler {
                isGalleryStart(this@onGroupContext, galleryId)
            }
        }
    }

    @JvmStatic
    fun SetGalleryRevivePoint(context: LuaContextWrapper, galleryId: Int, groupId: Int, pointId: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                setGalleryRevivePoint(this@onGroupContext, galleryId, groupId, pointId)
            }
        }
    }

    @JvmStatic
    fun SetPlayerStartGallery(context: LuaContextWrapper, galleryId: Int, uidListRawTable: Any): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val uiList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
                setPlayerStartGallery(this@onGroupContext, galleryId, uiList)
            }
        }
    }

    @JvmStatic
    fun AttachGalleryAbilityGroup(context: LuaContextWrapper, uidListRawTable: Any, galleryId: Int, var3: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val uiList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
                attachGalleryAbilityGroup(this@onGroupContext, uiList, galleryId, var3)
            }
        }
    }

    @JvmStatic
    fun AttachGalleryTeamAbilityGroup(
        context: LuaContextWrapper,
        uidListRawTable: Any,
        galleryId: Int,
        var3: Int
    ): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val uiList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
                attachGalleryTeamAbilityGroup(this@onGroupContext, uiList, galleryId, var3)
            }
        }
    }

    @JvmStatic
    fun DelGalleryAbilityGroup(context: LuaContextWrapper, uidListRawTable: Any, galleryId: Int, var3: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val uiList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
                delGalleryAbilityGroup(this@onGroupContext, uiList, galleryId, var3)
            }
        }
    }


    @JvmStatic
    fun InitGalleryProgressScore(
        context: LuaContextWrapper, name: String?, galleryId: Int, progressTable: Any,
        scoreUiTypeIndex: Int, scoreTypeIndex: Int
    ): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val progress = context.engine.getTable(progressTable)

                checkGalleryScoreUiTypeIndexName(::InitGalleryProgressScore, scoreUiTypeIndex)?.let {
                    return@onGalleryHandler it.getValue()
                }
                val uiScoreType = GalleryProgressScoreUIType.entries[scoreUiTypeIndex]

                checkGalleryScoreTypeIndexName(::InitGalleryProgressScore, scoreTypeIndex)?.let {
                    return@onGalleryHandler it.getValue()
                }
                val scoreType = GalleryProgressScoreType.entries[scoreTypeIndex]

                checkGalleryProgressName(::InitGalleryProgressScore, name)?.let {
                    return@onGalleryHandler it.getValue()
                }

                initGalleryProgressScore(this@onGroupContext, name!!, galleryId, progress, uiScoreType, scoreType)
            }
        }
    }

    @JvmStatic
    fun InitGalleryProgressWithScore(
        context: LuaContextWrapper, name: String?, galleryId: Int, progressTable: Any,
        maxProgress: Int, scoreUiTypeIndex: Int, scoreTypeIndex: Int
    ): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val progress = context.engine.getTable(progressTable)

                checkGalleryScoreUiTypeIndexName(::InitGalleryProgressWithScore, scoreUiTypeIndex)?.let {
                    return@onGalleryHandler it.getValue()
                }
                val uiScoreType = GalleryProgressScoreUIType.entries[scoreUiTypeIndex]

                checkGalleryScoreTypeIndexName(::InitGalleryProgressWithScore, scoreTypeIndex)?.let {
                    return@onGalleryHandler it.getValue()
                }
                val scoreType = GalleryProgressScoreType.entries[scoreTypeIndex]

                checkGalleryProgressName(::InitGalleryProgressWithScore, name)?.let {
                    return@onGalleryHandler it.getValue()
                }

                initGalleryProgressWithScore(this@onGroupContext, name!!, galleryId, progress, maxProgress, uiScoreType, scoreType)
            }
        }
    }

    @JvmStatic
    fun AddGalleryProgressScore(context: LuaContextWrapper, name: String?, galleryId: Int, score: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                checkGalleryProgressName(::AddGalleryProgressScore, name)?.let {
                    return@onGalleryHandler it.getValue()
                }
                addGalleryProgressScore(this@onGroupContext, name!!, galleryId, score)
            }
        }
    }

    @JvmStatic
    fun GetGalleryProgressScore(context: LuaContextWrapper, name: String?, galleryId: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                checkGalleryProgressName(::GetGalleryProgressScore, name)?.let {
                    return@onGalleryHandler it.getValue()
                }
                getGalleryProgressScore(this@onGroupContext, name!!, galleryId)
            }
        }
    }


    /* FleurFairScriptHandler */

    @JvmStatic
    fun AddFleurFairMultistagePlayBuffEnergy(
        context: LuaContextWrapper,
        groupId: Int,
        param2: Int,
        uid: Int,
        bonusId: Int
    ): Int {
        return context.onGroupContext {
            onFleurFairScriptHandler {
                checkGroupId(::AddFleurFairMultistagePlayBuffEnergy, groupId)?.let {
                    return@onFleurFairScriptHandler it.getValue()
                }
                checkUid(::AddFleurFairMultistagePlayBuffEnergy, uid)?.let {
                    return@onFleurFairScriptHandler it.getValue()
                }
                addFleurFairMultistagePlayBuffEnergy(this@onGroupContext, groupId, param2, uid, bonusId)
            }
        }
    }

    @JvmStatic
    fun FinishFleurFairGalleryStageByUid(context: LuaContextWrapper, groupId: Int, var2: Int, uid: Int, var4: Boolean): Int {
        return context.onGroupContext {
            onFleurFairScriptHandler {
                checkGroupId(::FinishFleurFairGalleryStageByUid, groupId)?.let {
                    return@onFleurFairScriptHandler it.getValue()
                }
                checkUid(::FinishFleurFairGalleryStageByUid, uid)?.let {
                    return@onFleurFairScriptHandler it.getValue()
                }
                finishFleurFairGalleryStageByUid(this@onGroupContext, groupId, var2, uid, var4)
            }
        }
    }

    @JvmStatic
    fun GetFleurFairDungeonSectionId(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onFleurFairScriptHandler {
                getFleurFairDungeonSectionId(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetFleurFairMultistagePlayBuffEnergy(context: LuaContextWrapper, groupId: Int, var2: Int, uid:Int): Int {
        return context.onGroupContext {
            onFleurFairScriptHandler {
                checkGroupId(::GetFleurFairMultistagePlayBuffEnergy, groupId)?.let {
                    return@onFleurFairScriptHandler it.getValue()
                }
                checkUid(::GetFleurFairMultistagePlayBuffEnergy, uid)?.let {
                    return@onFleurFairScriptHandler it.getValue()
                }
                getFleurFairMultistagePlayBuffEnergy(this@onGroupContext, groupId, var2, uid)
            }
        }
    }

    @JvmStatic
    fun GetFleurFairMultistagePlayGalleryIdVec(context: LuaContextWrapper, groupId: Int, var2: Int): IntArray {
        return context.onGroupContext {
            onFleurFairScriptHandler {
                checkGroupId(::GetFleurFairMultistagePlayGalleryIdVec, groupId)?.let {
                    return@onFleurFairScriptHandler intArrayOf(it.getValue())
                }
                getFleurFairMultistagePlayGalleryIdVec(this@onGroupContext, groupId, var2).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetFleurFairMultistagePlayGalleryTempValue(context: LuaContextWrapper, groupId: Int, var2: Int, tmpValueKey: String): Int {
        return context.onGroupContext {
            onFleurFairScriptHandler {
                checkGroupId(::GetFleurFairMultistagePlayGalleryTempValue, groupId)?.let {
                    return@onFleurFairScriptHandler it.getValue()
                }
                getFleurFairMultistagePlayGalleryTempValue(this@onGroupContext, groupId, var2, tmpValueKey)
            }
        }
    }


    /* LanternRiteScriptHandler */

    @JvmStatic
    fun GetLanternRiteValue(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onLanternRiteScriptHandler {
                getLanternRiteValue(this@onGroupContext)
            }
        }
    }
    @JvmStatic
    fun SetLanternRiteValue(context: LuaContextWrapper, value: Int): Int {
        return context.onGroupContext {
            onLanternRiteScriptHandler {
                setLanternRiteValue(this@onGroupContext, value)
            }
        }
    }


    /* LunaRiteScriptHandler */

    @JvmStatic
    fun GetLunaRiteSacrificeNum(context: LuaContextWrapper, areaId: Int): Int {
        return context.onGroupContext {
            onLunaRiteScriptHandler {
                getLunaRiteSacrificeNum(this@onGroupContext, areaId)
            }
        }
    }


    /* AsterScriptHandler */
    @JvmStatic
    fun CreateAsterMidGeneralRewardGadget(context: LuaContextWrapper, rawParamsTable: Any): Int {
        return context.onGroupContext {
            onAsterScriptHandler {
                val paramsTable = context.engine.getTable(rawParamsTable)
                val configId = paramsTable.optInt("config_id", -1)
                val difficultyId = paramsTable.optInt("difficulty_id", -1)
                checkConfigId(::CreateAsterMidGeneralRewardGadget, configId, isTableParam = true)?.let {
                    return@onAsterScriptHandler it.getValue()
                }
                if (difficultyId == -1) {
                    scriptLogger.error { "[CreateAsterMidGeneralRewardGadget] Invalid difficulty id $difficultyId" }
                    return@onAsterScriptHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                createAsterMidGeneralRewardGadget(this@onGroupContext, configId, difficultyId)
            }
        }
    }



    /* CharAmusementScriptHandler */

    @JvmStatic
    fun CharAmusementMultistagePlaySwitchTeam(context: LuaContextWrapper, groupId: Int, var2: Int, stageIndex: Int): Int {
        return context.onGroupContext {
            onCharAmusementScriptHandler {
                checkGroupId(::CharAmusementMultistagePlaySwitchTeam, groupId)?.let {
                    return@onCharAmusementScriptHandler it.getValue()
                }
                charAmusementMultistagePlaySwitchTeam(this@onGroupContext, groupId, var2, stageIndex)
            }
        }
    }

    @JvmStatic
    fun CharAmusementUpdateScore(context: LuaContextWrapper, groupId: Int, var2: Int, scoreChange:Int): Int {
        return context.onGroupContext {
            onCharAmusementScriptHandler {
                checkGroupId(::CharAmusementUpdateScore, groupId)?.let {
                    return@onCharAmusementScriptHandler it.getValue()
                }
                charAmusementUpdateScore(this@onGroupContext, groupId, var2, scoreChange)
            }
        }
    }

    @JvmStatic
    fun GetCharAmusementGalleryTarget(context: LuaContextWrapper, galleryId: Int, isMultiplayer: Boolean): Int {
        return context.onGroupContext {
            onCharAmusementScriptHandler {
                getCharAmusementGalleryTarget(this@onGroupContext, galleryId, isMultiplayer)
            }
        }
    }

    @JvmStatic
    fun GetCharAmusementMultistagePlayGalleryIdVec(context: LuaContextWrapper, groupId: Int, var2: Int): IntArray {
        return context.onGroupContext {
            onCharAmusementScriptHandler {
                checkGroupId(::GetCharAmusementMultistagePlayGalleryIdVec, groupId)?.let {
                    return@onCharAmusementScriptHandler intArrayOf(it.getValue())
                }
                getCharAmusementMultistagePlayGalleryIdVec(this@onGroupContext, groupId, var2).toIntArray()
            }
        }
    }


    /* EffigyChallengeScriptHandler */

    @JvmStatic
    fun CreateEffigyChallengeMonster(context: LuaContextWrapper, var1: Int, var2Table: Any): Int {
        return context.onGroupContext {
            onEffigyScriptHandler {
                val var2 = context.engine.getTable(var2Table)
                createEffigyChallengeMonster(this@onGroupContext, var1, var2)
            }
        }
    }

    @JvmStatic
    fun GetEffigyChallengeMonsterLevel(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onEffigyScriptHandler {
                getEffigyChallengeMonsterLevel(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetEffigyChallengeLimitTime(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onEffigyScriptHandler {
                getEffigyChallengeLimitTime(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetEffigyChallengeV2DungeonDifficulty(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onEffigyScriptHandler {
                getEffigyChallengeV2DungeonDifficulty(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun IsEffigyChallengeConditionSelected(context: LuaContextWrapper, conditionId: Int): Boolean {
        return context.onGroupContext {
            onEffigyScriptHandler {
                isEffigyChallengeConditionSelected(this@onGroupContext, conditionId)
            }
        }
    }


    /* FungusFighertScriptHandler */

    @JvmStatic
    fun SetCurFungusFighterTrainingParams(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onFungusFighterScriptHandler {
                val paramsTable = context.engine.getTable(rawTable)
                val randIndex = paramsTable.optInt("randIndex", -1)
                val monsterPoolList = paramsTable.getTable("monsterPoolList")?.getAsIntArray()?.toList()

                if(randIndex == -1 || monsterPoolList == null) {
                    scriptLogger.error { "SetCurFungusFighterTrainingParams: Invalid parameters" }
                    return@onFungusFighterScriptHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }

                setCurFungusFighterTrainingParams(this@onGroupContext, FungusFighterTrainingParams(randIndex, monsterPoolList))
            }
        }
    }

    @JvmStatic
    fun GetCurFungusFighterPlotConfigIdList(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onFungusFighterScriptHandler {
                getCurFungusFighterPlotConfigIdList(this@onGroupContext).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetCurFungusFighterTrainingParams(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onFungusFighterScriptHandler {
                getCurFungusFighterTrainingParams(this@onGroupContext).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetCurFungusFighterTrainingValidBackupFungusIdList(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onFungusFighterScriptHandler {
                getCurFungusFighterTrainingValidBackupFungusIdList(this@onGroupContext).toIntArray()
            }
        }
    }

    @JvmStatic
    fun IsFungusCaptured(context: LuaContextWrapper, uid:Int, fungusMonsterId:Int): Int {
        return context.onGroupContext {
            onFungusFighterScriptHandler {
                checkUid(::IsFungusCaptured, uid)?.let {
                    return@onFungusFighterScriptHandler it.getValue()
                }
                isFungusCaptured(this@onGroupContext, uid, fungusMonsterId)
            }
        }
    }

    @JvmStatic
    fun VintageFinishGroupByPresentId(context: LuaContextWrapper, presentId: Int): Int {
        return context.onGroupContext {
            onVintageScriptHandler {
                vintageFinishGroupByPresentId(this@onGroupContext, presentId)
            }
        }
    }


    /* HideAndSeekScriptHandler*/


    @JvmStatic
    fun GetHideAndSeekPlayIndex(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onHideAndSeekScriptHandler {
                getHideAndSeekPlayIndex(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekHunter(context: LuaContextWrapper, index: Int): Int {
        return context.onGroupContext {
            onHideAndSeekScriptHandler {
                getHideAndSeekHunter(this@onGroupContext, index)
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekPreyUidList(context: LuaContextWrapper, index: Int): IntArray {
        return context.onGroupContext {
            onHideAndSeekScriptHandler {
                getHideAndSeekPreyUidList(this@onGroupContext, index).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekMap(context: LuaContextWrapper, index: Int): Int {
        return context.onGroupContext {
            onHideAndSeekScriptHandler {
                getHideAndSeekMap(this@onGroupContext, index)
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekPlayGalleryId(context: LuaContextWrapper, index: Int): Int {
        return context.onGroupContext {
            onHideAndSeekScriptHandler {
                getHideAndSeekPlayGalleryId(this@onGroupContext, index)
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekPlayerSkillList(context: LuaContextWrapper, index: Int, uid: Int): IntArray {
        return context.onGroupContext {
            onHideAndSeekScriptHandler {
                checkUid(::GetHideAndSeekPlayerSkillList, uid)?.let {
                    return@onHideAndSeekScriptHandler intArrayOf(it.getValue())
                }
                getHideAndSeekPlayerSkillList(this@onGroupContext, index, uid).toIntArray()
            }
        }
    }


    /* TreasureSeelieScriptHandler */

    @JvmStatic
    fun TreasureSeelieCollectOrbsNotify(context: LuaContextWrapper, lightLevel: Int, var2: Int): Int {
        return context.onGroupContext {
            onTreasureSeelieScriptHandler {
                treasureSeelieCollectOrbsNotify(this@onGroupContext, lightLevel, var2)
            }
        }
    }

    @JvmStatic
    fun GetTreasureSeelieDayByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onTreasureSeelieScriptHandler {
                checkGroupId(::GetTreasureSeelieDayByGroupId, groupId)?.let {
                    return@onTreasureSeelieScriptHandler it.getValue()
                }
                getTreasureSeelieDayByGroupId(this@onGroupContext, groupId)
            }
        }
    }


    /* WinterCampScriptHandler */

    @JvmStatic
    fun WinterCampGetBattleGroupBundleId(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onWinterCampScriptHandler {
                winterCampGetBattleGroupBundleId(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun WinterCampGetExploreGroupBundleId(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onWinterCampScriptHandler {
                winterCampGetExploreGroupBundleId(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun WinterCampSnowDriftInteract(context: LuaContextWrapper, cfgId: Int): Int {
        return context.onGroupContext {
            onWinterCampScriptHandler {
                checkConfigId(::WinterCampSnowDriftInteract, cfgId)?.let {
                    return@onWinterCampScriptHandler it.getValue()
                }
                winterCampSnowDriftInteract(this@onGroupContext, cfgId)
            }
        }
    }


    /*                                 */
    /* GadgetControllerHandler methods */
    /*                                 */

    /**
     * Methods used in EntityControllers/using ControllerLuaContext
     */
    @JvmStatic
    fun SetGadgetState(context: LuaContextWrapper, gadgetState: Int): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                setGadgetState(this@onControllerContext, gadgetState)
            }
        }
    }

    @JvmStatic
    fun GetGadgetState(context:LuaContextWrapper): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                getGadgetState(this@onControllerContext)
            }
        }
    }

    @JvmStatic
    fun GetGadgetArguments(context:LuaContextWrapper): IntArray? {
        // TODO check because of IntArray return type
        return context.onControllerContext {
            onGadgetControllerHandler {
                getGadgetArguments(this@onControllerContext)
            }
        }
    }

    @JvmStatic
    fun ResetGadgetState(context: LuaContextWrapper, gadgetState: Int): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                resetGadgetState(this@onControllerContext, gadgetState)
            }
        }
    }

    @JvmStatic
    fun SetGearStartValue(context: LuaContextWrapper, startValue: Int): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                setGearStartValue(this@onControllerContext, startValue)
            }
        }
    }

    @JvmStatic
    fun GetGearStartValue(context: LuaContextWrapper): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                getGearStartValue(this@onControllerContext)
            }
        }
    }

    @JvmStatic
    fun SetGearStopValue(context: LuaContextWrapper, startValue: Int): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                setGearStopValue(this@onControllerContext, startValue)
            }
        }
    }

    @JvmStatic
    fun GetGearStopValue(context: LuaContextWrapper): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                getGearStopValue(this@onControllerContext)
            }
        }
    }

    @JvmStatic
    fun GetGadgetStateBeginTime(context: LuaContextWrapper): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                getGadgetStateBeginTime(this@onControllerContext)
            }
        }
    }

    @JvmStatic
    fun GetContextGadgetConfigId(context: LuaContextWrapper): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                getContextGadgetConfigId(this@onControllerContext)
            }
        }
    }

    @JvmStatic
    fun GetContextGroupId(context: LuaContextWrapper): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                getContextGroupId(this@onControllerContext)
            }
        }
    }

    @JvmStatic
    fun SetGadgetEnableInteract(context: LuaContextWrapper, groupId: Int, configId: Int, enable: Boolean): Int {
        checkGroupIdAndConfigId(::SetGadgetEnableInteract, groupId, configId)?.let {
            return it.getValue()
        }
        return context.onTypedContext({
            onGroupGadgetHandler {
                setGadgetEnableInteract(this@onTypedContext, groupId, configId, enable)
            }
        }, {
            onGadgetControllerHandler {
                setGadgetEnableInteract(this@onTypedContext, groupId, configId, enable)
            }
        })
    }

    @JvmStatic
    fun DropSubfield(context: LuaContextWrapper, paramsTable: Any): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                val params = context.engine.getTable(paramsTable)
                dropSubfield(this@onControllerContext, params)
            }
        }
    }

    @JvmStatic
    fun GetGatherConfigIdList(context: LuaContextWrapper): IntArray? {
        return context.onControllerContext {
            onGadgetControllerHandler {
                getGatherConfigIdList(this@onControllerContext)
            }
        }
    }


    /* helpers to verify values */
    private fun checkUid(caller: KCallable<*>, uid: Int): ScriptLibErrors? {
        if (uid <= 0) {
            scriptLogger.error { "[${caller.name}] Invalid uid ($uid)" }
            return ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }

    private fun checkConfigId(caller: KCallable<*>, configId: Int, isTableParam: Boolean = false): ScriptLibErrors? {
        if (configId <= 0) { // TODO 0 exists, verify if always or only in specific cases
            scriptLogger.error { "[$caller] Invalid configId ($configId)" }
            return if(isTableParam) ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT else ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }

    private fun checkGroupId(caller: KCallable<*>, groupId: Int): ScriptLibErrors? {
        if (groupId < 0) {
            scriptLogger.error { "[$caller] Invalid groupId ($groupId)" }
            return ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }

    private fun checkGroupIdAndConfigId(caller: KCallable<*>, groupId: Int, configId: Int): ScriptLibErrors? {
        if (groupId < 0 || configId <= 0) {
            scriptLogger.error { "[$caller] Invalid groupId ($groupId) or configId ($configId)" }
            return ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }

    private fun checkGroupIdAndSuiteId(caller: KCallable<*>, groupId: Int, suiteId: Int): ScriptLibErrors? {
        if (groupId < 0 || suiteId < 0) {
            scriptLogger.error { "[$caller] Invalid groupId ($groupId) or suiteId ($suiteId)" }
            return ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }

    private fun checkGalleryScoreUiTypeIndexName(caller: KCallable<*>, scoreUiTypeIndex: Int): ScriptLibErrors? {
        if (scoreUiTypeIndex < 0 || scoreUiTypeIndex >= GalleryProgressScoreUIType.entries.size) {
            scriptLogger.error { "[$caller] Invalid GalleryProgressScoreUIType ($scoreUiTypeIndex)" }
            return ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }

    private fun checkGalleryScoreTypeIndexName(caller: KCallable<*>, scoreTypeIndex: Int): ScriptLibErrors? {
        if (scoreTypeIndex < 0 || scoreTypeIndex >= GalleryProgressScoreType.entries.size) {
            scriptLogger.error { "[$caller] Invalid GalleryProgressScoreType ($scoreTypeIndex)" }
            return ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }

    private fun checkGalleryProgressName(caller: KCallable<*>, name: String?): ScriptLibErrors? {
        if (name.isNullOrEmpty()) {
            scriptLogger.error { "[$caller] Invalid gallery name $name" }
            return ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }

    private fun checkFloatValueKey(caller: KCallable<*>, floatValueKey: String): ScriptLibErrors? {
        if (floatValueKey.isEmpty()) {
            scriptLogger.error { "[$caller] Invalid ability Float Value name ($floatValueKey)" }
            return ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }
}
