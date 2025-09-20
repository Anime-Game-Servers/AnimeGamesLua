@file:Suppress("unused", "FunctionName")

package org.anime_game_servers.gi_lua.script_lib

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.core.base.annotations.lua.LuaStatic
import org.anime_game_servers.gi_lua.models.PositionImpl
import org.anime_game_servers.gi_lua.models.constants.*
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreType
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreUIType
import org.anime_game_servers.gi_lua.script_lib.handler.ScriptLibStaticHandler
import org.anime_game_servers.gi_lua.script_lib.handler.activity.ChessPreviewInfo
import org.anime_game_servers.gi_lua.script_lib.handler.activity.CrystalLinkTeamSetupParams
import org.anime_game_servers.gi_lua.script_lib.handler.activity.FungusFighterTrainingParams
import org.anime_game_servers.gi_lua.script_lib.handler.activity.MechanicusChallengeState
import org.anime_game_servers.gi_lua.script_lib.handler.entites.CreateGadgetParameters
import org.anime_game_servers.gi_lua.script_lib.handler.entites.CreateMonsterParameters
import org.anime_game_servers.gi_lua.script_lib.handler.entites.MonsterFaceAvatarParameters
import org.anime_game_servers.gi_lua.script_lib.handler.entites.RemainGadgetCountParameters
import org.anime_game_servers.gi_lua.script_lib.handler.gadget.SetPlatformPointArrayParams
import org.anime_game_servers.gi_lua.script_lib.handler.other.AssignPlayerShowTemplateReminderParams
import org.anime_game_servers.gi_lua.script_lib.handler.other.AssignPlayerUidOpNotifyParams
import org.anime_game_servers.gi_lua.script_lib.handler.other.ScenePlaySoundParams
import org.anime_game_servers.gi_lua.script_lib.handler.parameter.KillByConfigIdParams
import org.anime_game_servers.gi_lua.script_lib.handler.player.ExhibitionPlayTarget
import org.anime_game_servers.gi_lua.script_lib.handler.scene.AttachChildChallengePointConfig
import org.anime_game_servers.gi_lua.script_lib.handler.scene.BeginCameraSceneLookParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.BeginCameraSceneLookTemplateParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.ChangeLevelTagParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.CreateFatherChallengeParameters
import org.anime_game_servers.gi_lua.script_lib.handler.scene.EnvAnimalType
import org.anime_game_servers.gi_lua.script_lib.handler.scene.InitSceneMultistagePlayParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.ModifyClimatePolygonParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.PoolMonsterTideConfig
import org.anime_game_servers.gi_lua.script_lib.handler.scene.PrestartScenePlayBattleParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.RefreshBlossomGroupParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.RefreshGroupParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.SealBattleParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.SetSceneMultiStagePlayValuesParams
import org.anime_game_servers.gi_lua.script_lib.handler.scene.StartSceneMultiStagePlayStageParams
import org.anime_game_servers.gi_lua.utils.ScriptUtils.toLuaTable
import org.anime_game_servers.gi_lua.utils.ScriptUtils.toVector
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
        return staticHandler?.printLog(msg)?.let { 0 } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue()
    }
    @Deprecated("only for compat with modified Scripts", ReplaceWith("PrintLog(msg: String?)"))
    @JvmStatic
    fun PrintLog(context: LuaContextWrapper, msg: String?) : Int {
        return PrintLog(msg)
    }

    @JvmStatic
    fun GetEntityType(entityId: Int): Int {
        return staticHandler?.getEntityType(entityId) ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue()
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
        return staticHandler?.printContextLog(context.luaContext, msg)?.let { 0 }  ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue()
    }

    /* LoggingScriptHandler */

    /**
     * GroupEventLuaContext functions
     */
    @JvmStatic
    fun PrintGroupWarning(context: LuaContextWrapper, msg: String) {
        context.onGroupContext {
            onLoggingHandler {
                printGroupWarning(this@onGroupContext, msg)
            }
        }
    }

    @JvmStatic
    fun MarkGroupLuaAction(context: LuaContextWrapper, action: String, transaction: String, log: Any): Int {
        return context.onGroupContext {
            onLoggingHandler {
                val logTable = context.engine.getTable(log)
                markGroupLuaAction(this@onGroupContext, action, transaction, logTable)
            }
        }
    }

    @JvmStatic
    fun MarkPlayerAction(context: LuaContextWrapper, var1: Int, var2: Int, var3: Int): Int {
        return context.onGroupContext {
            onLoggingHandler {
                markPlayerAction(this@onGroupContext, var1, var2, var3)
            }
        }
    }


    /* GroupEntityHandler */

    @JvmStatic
    fun KillEntityByConfigId(context: LuaContextWrapper, rawTable: Any): Int {
        val table = context.engine.getTable(rawTable)
        val configId = table.optInt("config_id", 0)
        val groupId = table.optInt("group_id", 0)
        checkGroupIdAndConfigId(::KillEntityByConfigId, groupId, configId)?.let {
            return it.getValue()
        }

        val entityTypeValue = table.optInt("entity_type", 0)
        checkEnumIndex<EntityType>(::KillEntityByConfigId, entityTypeValue)?.let {
            return it.getValue()
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
                checkGroupId(::RemoveEntityByConfigId, groupId)?.let {
                    return@onGroupEntityHandler it.getValue()
                }
                checkEnumIndex<EntityType>(::RemoveEntityByConfigId, entityTypeValue)?.let {
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
                checkGroupId(::KillGroupEntity, groupId)?.let {
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
        checkEnumIndex<GroupKillPolicy>(::KillGroupEntity, killPolicyId)?.let {
            return it.getValue()
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
        val monsters = monsterList?.getAsIntArray()?.toList() ?: emptyList()
        val gadgets = gadgetList?.getAsIntArray()?.toList() ?: emptyList()

        return context.onGroupEntityHandler {
            killGroupEntityByCfgIds(context, groupId, monsters, gadgets)
        }
    }


    @JvmStatic
    fun DelAllSubEntityByOriginOwnerConfigId(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                checkConfigId(::DelAllSubEntityByOriginOwnerConfigId, configId)?.let {
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
                checkConfigId(::GetEntityIdByConfigId, configId)?.let {
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
                checkUid(::GetTeamEntityIdByUid, uid)?.let {
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
                checkUid(::GetAvatarEntityIdByUid, uid)?.let {
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
                getPosByEntityId(this@onGroupContext, entityId)
                    .toLuaTable(context.engine)
                    .getRawTable()
            }
        }
    }

    @JvmStatic
    fun GetRotationByEntityId(context: LuaContextWrapper, entityId: Int): Any {
        return context.onGroupContext {
            onGroupEntityHandler {
                getRotationByEntityId(this@onGroupContext, entityId)
                    .toLuaTable(context.engine)
                    .getRawTable()
            }
        }
    }

    @JvmStatic
    fun GetSurroundUidList(context: LuaContextWrapper, configId: Int, radius: Int): IntArray? {
        return context.onGroupContext {
            onGroupEntityHandler {
                checkConfigId(::GetSurroundUidList, configId)?.let {
                    return@onGroupEntityHandler intArrayOf(it.getValue())
                }
                getSurroundUidList(this@onGroupContext, configId, radius)?.toIntArray()
            }
        }
    }

    @JvmStatic
    fun TryReallocateEntityAuthority(context: LuaContextWrapper, uid: Int, configId: Int, regionConfigId: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                checkUid(::TryReallocateEntityAuthority, uid)?.let {
                    return@onGroupEntityHandler it.getValue()
                }
                tryReallocateEntityAuthority(this@onGroupContext, uid, configId, regionConfigId)
            }
        }
    }

    @JvmStatic
    fun ForceRefreshAuthorityByConfigId(context: LuaContextWrapper, var1: Int, uid: Int): Int {
        return context.onGroupContext {
            onGroupEntityHandler {
                checkUid(::ForceRefreshAuthorityByConfigId, uid)?.let {
                    return@onGroupEntityHandler it.getValue()
                }
                forceRefreshAuthorityByConfigId(this@onGroupContext, var1, uid)
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
                val optionsTable = context.engine.getTable(optionsTable)
                val options = optionsTable.getAsIntArray().toList()
                setWorktopOptionsByGroupId(this@onGroupContext, groupId, configId, options)
            }
        }
    }

    @JvmStatic
    fun SetWorktopOptions(context: LuaContextWrapper, rawOptionsTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val optionsTable = context.engine.getTable(rawOptionsTable)
                val options = optionsTable.getAsIntArray().toList()
                setWorktopOptions(this@onGroupContext, options)
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
                val configId = table.optInt("config_id", 0)
                checkConfigId(::CreateGadget, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                createGadget(this@onGroupContext, configId)
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
                val pos = context.engine.getTable(posTable).toVector()
                val rot = context.engine.getTable(rotTable).toVector()
                createGadgetByConfigIdByPos(this@onGroupContext, configId, pos, rot)
            }
        }
    }

    /**
     * Spawns a gadget based on the caller groups gadget with cfg id matching the specified id. It also applies additional parameters based on the parameters
     * @param creationParamsTable parameters to spawn a gadget with
     */
    @JvmStatic
    fun CreateGadgetByParamTable(context: LuaContextWrapper, creationParamsTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val table = context.engine.getTable(creationParamsTable)
                val creationParams = CreateGadgetParameters.fromLuaTable(table) ?: run {
                    scriptLogger.error { "[CreateGadgetByParamTable] Invalid parameters table" }
                    return@onGroupGadgetHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                createGadgetByParamTable(this@onGroupContext, creationParams)
            }
        }
    }

    @JvmStatic
    fun CreateGadgetWave(
        context: LuaContextWrapper,
        areaId: Int,
        suitId: Int,
        offset: Int,
        boxSizeTable: Any,
        gadgetSizeTable: Any
    ): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val boxSize = context.engine.getTable(boxSizeTable).toVector()
                val gadgetSize = context.engine.getTable(gadgetSizeTable).toVector()
                createGadgetWave(
                    this@onGroupContext,
                    areaId,
                    suitId,
                    offset,
                    boxSize,
                    gadgetSize
                )
            }
        }
    }

    @JvmStatic
    fun CreateGadgetWithGlobalValue(context: LuaContextWrapper, configId: Int, rawSgvTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkConfigId(::CreateGadgetWithGlobalValue, configId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                val sgvTable = context.engine.getTable(rawSgvTable)
                val keys = sgvTable.getKeys()
                val sgvMap = keys.associateWith { key -> sgvTable.getInt(key) }
                createGadgetWithGlobalValue(this@onGroupContext, configId, sgvMap)
            }
        }
    }
    @JvmStatic
    fun CheckRemainGadgetCountByGroupId(context: LuaContextWrapper, rawParametersTable: Any): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                val parametersTable = context.engine.getTable(rawParametersTable)
                val parameters = RemainGadgetCountParameters.fromLuaTable(parametersTable) ?: run {
                    scriptLogger.error { "[CheckRemainGadgetCountByGroupId] Invalid parameters: $rawParametersTable" }
                    return@onGroupGadgetHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                checkRemainGadgetCountByGroupId(this@onGroupContext, parameters)
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
        checkConfigId(::SetGadgetStateByConfigId, configId)?.let {
            return it.getValue()
        }
        return context.onTypedContext({
            onGroupGadgetHandler {
                setGadgetStateByConfigId(this@onTypedContext, configId, gadgetState)
            }
        }, {
            onGadgetControllerHandler {
                setGadgetStateByConfigId(this@onTypedContext, configId, gadgetState)
            }
        })
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
     * Executes the OnClientExecuteReq function on a gadgets lua controller.
     * This seems to be used in only the Crucible activity
     * @param groupId group to find the gadget in
     * @param gadgetCfgId cfg id of the gadget in the group to execute lua in or 0 to get from context
     */
    @JvmStatic
    fun ExecuteGadgetLua(
        context: LuaContextWrapper,
        groupId: Int,
        gadgetCfgId: Int,
        param1: Int,
        param2: Int,
        param3: Int
    ): Int {
        return context.onGroupContext {
            onGroupGadgetHandler {
                checkGroupId(::ExecuteGadgetLua, groupId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                checkConfigIdOptional(::ExecuteGadgetLua, gadgetCfgId)?.let {
                    return@onGroupGadgetHandler it.getValue()
                }
                executeGadgetLua(this@onGroupContext, groupId, gadgetCfgId, param1, param2, param3)
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
                val params = MonsterFaceAvatarParameters.fromLuaTable(table) ?: run {
                    scriptLogger.error { "[CreateMonsterFaceAvatar] Invalid parameters: $rawTable" }
                    return@onGroupMonsterHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                createMonsterFaceAvatar(this@onGroupContext, params)
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
    fun CreateMonster(context: LuaContextWrapper, rawParametersTable: Any): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                val parametersTable = context.engine.getTable(rawParametersTable)
                val parameters = CreateMonsterParameters.fromLuaTable(parametersTable) ?: run {
                    scriptLogger.error { "[CreateMonster] Invalid parameters table $parametersTable" }
                    return@onGroupMonsterHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                createMonster(this@onGroupContext, parameters)
            }
        }
    }

    @JvmStatic
    fun CreateMonsterWithGlobalValue(context: LuaContextWrapper, configId: Int, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupMonsterHandler {
                val table = context.engine.getTable(rawTable)
                val keys = table.getKeys()
                val paramMap = keys.associateWith { key -> table.getInt(key) }
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
                val pos = context.engine.getTable(rawPosTable).toVector()
                val rot = context.engine.getTable(rawRotTable).toVector()

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



    /* GroupRegionScriptHandler */

    @JvmStatic
    fun GetRegionEntityCount(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupRegionHandler {
                val table = context.engine.getTable(rawTable)

                val regionId = table.getInt("region_eid")
                val entityType = table.getInt("entity_type")
                checkEnumIndex<EntityType>(::GetRegionEntityCount, entityType)?.let {
                    return@onGroupRegionHandler 0 // TODO was this number intentional?
                }

                val entityTypeEnum = EntityType.entries[entityType]
                getRegionEntityCount(this@onGroupContext, regionId, entityTypeEnum)
            }
        }
    }

    @JvmStatic
    fun GetRegionConfigId(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onGroupRegionHandler {
                val table = context.engine.getTable(rawTable)
                val regionEid = table.getInt("region_eid")
                getRegionConfigId(this@onGroupContext, regionEid)
            }
        }
    }

    @JvmStatic
    fun IsInRegion(context: LuaContextWrapper, uid: Int, regionId: Int): Boolean {
        return context.onGroupContext {
            onGroupRegionHandler {
                checkUid(::IsInRegion, uid)?.let {
                    return@onGroupRegionHandler false
                }
                isInRegion(this@onGroupContext, uid, regionId)
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
     * @param floatValueKey name of the abilities svg value to get the float value from.
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
                checkGroupId(::GetGadgetAbilityFloatValue, groupId)?.let {
                    return@onGroupAbilityHandler it.getValue().toFloat()
                }
                checkConfigIdOptional(::GetGadgetAbilityFloatValue, configId)?.let {
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


    @JvmStatic
    fun GetGroupLogicStateValue(context: LuaContextWrapper, sgvName: String): Int {
        return context.onGroupContext {
            onGroupAbilityHandler {
                getGroupLogicStateValue(this@onGroupContext, sgvName)
            }
        }
    }


    /*                 */
    /* Player Handlers */
    /*                 */


    /* Gadget giving handler */

    /**
     * @param givingId The id if the giving element found in [GivingData]
     * @param groupId The groupId of the group containing the gadget
     * @param gadgetCfgId The gadgets target configId
     * @return 0 if success, something else if failed
     */
    @JvmStatic
    fun ActiveGadgetItemGiving(context: LuaContextWrapper, givingId: Int, groupId: Int, gadgetCfgId: Int): Int {
        return context.onGroupContext {
            onGadgetGivingHandler {
                checkGroupIdAndConfigId(::ActiveGadgetItemGiving, groupId, gadgetCfgId)?.let {
                    return@onGadgetGivingHandler it.getValue()
                }
                activeGadgetItemGiving(this@onGroupContext, givingId, groupId, gadgetCfgId)
            }
        }
    }

    @JvmStatic
    fun GetGivingItemList(context: LuaContextWrapper, givingId: Int): IntArray? {
        return context.onGroupContext {
            onGadgetGivingHandler {
                getGivingItemList(this@onGroupContext, givingId)?.toIntArray()
            }
        }
    }


    /* GadgetPlayScriptHandler Group and Controller context */


    @JvmStatic
    fun GetGadgetPlayProgress(context: LuaContextWrapper, groupId: Int, configId: Int): Int {
        return context.onControllerContext {
            onGadgetPlayControllerHandler {
                checkGroupId(::GetGadgetPlayProgress, groupId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                checkConfigIdOptional(::GetGadgetPlayProgress, configId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                getGadgetPlayProgress(this@onControllerContext, groupId, configId)
            }
        }
    }

    @JvmStatic
    fun GetGadgetPlayStageBeginProgress(context: LuaContextWrapper, groupId: Int, configId: Int): Int {
        return context.onControllerContext {
            onGadgetPlayControllerHandler {
                checkGroupId(::GetGadgetPlayStageBeginProgress, groupId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                checkConfigIdOptional(::GetGadgetPlayStageBeginProgress, configId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                getGadgetPlayStageBeginProgress(this@onControllerContext, groupId, configId)
            }
        }
    }

    @JvmStatic
    fun GetGadgetPlayUidValue(
        context: LuaContextWrapper,
        groupId: Int,
        configId: Int,
        uid: Int,
        name: String
    ): Int {
        return context.onControllerContext {
            onGadgetPlayControllerHandler {
                checkUid(::GetGadgetPlayUidValue, uid)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                checkGroupId(::GetGadgetPlayUidValue, groupId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                checkConfigIdOptional(::GetGadgetPlayUidValue, configId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                getGadgetPlayUidValue(this@onControllerContext, groupId, configId, uid, name)
            }
        }
    }

    @JvmStatic
    fun AddGadgetPlayProgress(context: LuaContextWrapper, groupId: Int, configId: Int, progressChange: Int): Int {
        return context.onControllerContext {
            onGadgetPlayControllerHandler {
                checkGroupId(::AddGadgetPlayProgress, groupId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                checkConfigIdOptional(::AddGadgetPlayProgress, configId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                addGadgetPlayProgress(this@onControllerContext, groupId, configId, progressChange)
            }
        }
    }

    /* special overload with signature used by ElemForce_Base.lua since cb3 */
    @JvmStatic
    fun GadgetPlayUidOp(
        context: LuaContextWrapper,
        groupId: Int,
        gadgetCrucibleCfgId: Int,
        uidListRawTable: Any,
        op: Int,
        param: Int,
    ): Int {
        val uidList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
        return GadgetPlayUidOpInternal(context, groupId, gadgetCrucibleCfgId, uidList, op, "", listOf(param))
    }

    @JvmStatic
    fun GadgetPlayUidOp(
        context: LuaContextWrapper,
        groupId: Int,
        gadgetCrucibleCfgId: Int,
        uidListRawTable: Any,
        op: Int,
        paramString: String,
        paramListRawTable: Any
    ): Int {
        val uidList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
        val paramList = context.engine.getTable(paramListRawTable).getAsIntArray().toList()

        return GadgetPlayUidOpInternal(context, groupId, gadgetCrucibleCfgId, uidList, op, paramString, paramList)
    }

    @JvmStatic
    private fun GadgetPlayUidOpInternal(
        context: LuaContextWrapper,
        groupId: Int,
        gadgetCrucibleCfgId: Int,
        uidList: List<Int>,
        op: Int,
        paramString: String,
        paramList: List<Int>
    ): Int {
        checkGroupId(::GadgetPlayUidOpInternal, groupId)?.let {
            return it.getValue()
        }
        checkConfigIdOptional(::GadgetPlayUidOpInternal, gadgetCrucibleCfgId)?.let {
            return it.getValue()
        }

        return context.onTypedContext( {
            onGadgetPlayHandler {
                gadgetPlayUidOp(this@onTypedContext, groupId, gadgetCrucibleCfgId, uidList, op, paramString, paramList)
            }
        }, {
            onGadgetPlayControllerHandler {
                gadgetPlayUidOp(this@onTypedContext, groupId, gadgetCrucibleCfgId, uidList, op, paramString, paramList)
            }
        })
    }

    @JvmStatic
    fun SetGadgetPlayUidValue(
        context: LuaContextWrapper,
        groupId: Int,
        configId: Int,
        uid: Int,
        key: String,
        value: Int
    ): Int {
        return context.onControllerContext {
            onGadgetPlayControllerHandler {
                checkUid(::SetGadgetPlayUidValue, uid)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                checkGroupId(::SetGadgetPlayUidValue, groupId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                checkConfigIdOptional(::SetGadgetPlayUidValue, configId)?.let {
                    return@onGadgetPlayControllerHandler it.getValue()
                }
                setGadgetPlayUidValue(this@onControllerContext, groupId, configId, uid, key, value)
            }
        }
    }

    @JvmStatic
    fun StartGadgetPlay(context: LuaContextWrapper, groupId: Int, configId: Int): Int {
        return context.onGroupContext {
            onGadgetPlayHandler {
                checkGroupId(::StartGadgetPlay, groupId)?.let {
                    return@onGadgetPlayHandler it.getValue()
                }
                checkConfigIdOptional(::StartGadgetPlay, configId)?.let {
                    return@onGadgetPlayHandler it.getValue()
                }
                startGadgetPlay(this@onGroupContext, groupId, configId)
            }
        }
    }


    /* PlatformScriptHandler */

    @JvmStatic
    fun GetPlatformArrayInfoByPointId(context: LuaContextWrapper, arrayId: Int, pointId: Int): Int {
        return context.onGroupContext {
            onPlatformHandler {
                getPlatformArrayInfoByPointId(this@onGroupContext, arrayId, pointId)
                // TODO actually return it as vararg containing the 3 values
                0
            }
        }
    }

    @JvmStatic
    fun GetPlatformPointArray(context: LuaContextWrapper, configId: Int): IntArray {
        return context.onGroupContext {
            onPlatformHandler {
                checkConfigId(::GetPlatformPointArray, configId)?.let {
                    return@onPlatformHandler intArrayOf(it.getValue())
                }
                getPlatformPointArray(this@onGroupContext, configId).toIntArray()
            }
        }
    }
    @JvmStatic
    fun SetPlatformRouteIndexToNext(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onPlatformHandler {
                checkConfigId(::SetPlatformRouteIndexToNext, configId)?.let {
                    return@onPlatformHandler it.getValue()
                }
                setPlatformRouteIndexToNext(this@onGroupContext, configId)
            }
        }
    }

    /**
     * @param rawPointListTable contains the next points in a table
     * @param rawParamsTable has RouteType route_type, bool turn_mode
     */
    @JvmStatic
    fun SetPlatformPointArray(
        context: LuaContextWrapper,
        entityConfigId: Int,
        pointArrayId: Int,
        rawPointListTable: Any,
        rawParamsTable: Any
    ): Int {
        return context.onGroupContext {
            onPlatformHandler {
                val pointList = context.engine.getTable(rawPointListTable).getAsIntArray().toList()
                val paramsTable = context.engine.getTable(rawParamsTable)
                val params = SetPlatformPointArrayParams.fromLuaTable(paramsTable) ?: run {
                    scriptLogger.error { "[SetPlatformPointArray] Invalid parameters table $paramsTable" }
                    return@onPlatformHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                setPlatformPointArray(this@onGroupContext, entityConfigId, pointArrayId, pointList, params)
            }
        }
    }

    @JvmStatic
    fun SetPlatformRouteId(context: LuaContextWrapper, entityConfigId: Int, routeId: Int): Int {
        return context.onGroupContext {
            onPlatformHandler {
                checkConfigId(::SetPlatformRouteId, entityConfigId)?.let {
                    return@onPlatformHandler it.getValue()
                }
                setPlatformRouteId(this@onGroupContext, entityConfigId, routeId)
            }
        }
    }

    @JvmStatic
    fun StartPlatform(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onPlatformHandler {
                checkConfigId(::StartPlatform, configId)?.let {
                    return@onPlatformHandler it.getValue()
                }
                startPlatform(this@onGroupContext, configId)
            }
        }
    }

    @JvmStatic
    fun StopPlatform(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onPlatformHandler {
                checkConfigId(::StopPlatform, configId)?.let {
                    return@onPlatformHandler it.getValue()
                }
                stopPlatform(this@onGroupContext, configId)
            }
        }
    }

    /* VehicleScriptHandler*/
    @JvmStatic
    fun CreateVehicle(context: LuaContextWrapper, uid: Int, gadgetId: Int, posTable: Any, rotTable: Any): Int {
        return context.onGroupContext {
            onVehicleHandler {
                checkUid(::CreateVehicle, uid)?.let {
                    return@onVehicleHandler it.getValue()
                }
                val luaPos = context.engine.getTable(posTable).toVector()
                val luaRot = context.engine.getTable(rotTable).toVector()
                createVehicle(this@onGroupContext, uid, gadgetId, luaPos, luaRot)
            }
        }
    }


    /*                 */
    /* Player Handlers */
    /*                 */


    /* ExhibitionScriptHandler */

    /**
     * @param context
     * @param uid
     * @param dataKey key under which the data is stored
     * @param value value to add
     * @return
     */
    @JvmStatic
    fun AddExhibitionAccumulableData(context: LuaContextWrapper, uid: Int, dataKey: String, value: Int): Int {
        return context.onGroupContext {
            onExhibitionHandler {
                checkUid(::AddExhibitionAccumulableData, uid)?.let {
                    return@onExhibitionHandler it.getValue()
                }
                addExhibitionAccumulableData(this@onGroupContext, uid, dataKey, value)
            }
        }
    }

    /**
     * @param context
     * @param uid
     * @param dataKey key under which the data is stored
     * @param value value to add
     * @param rawTargetPlayInfoTable contains the fields "play_type" is part of the enum [ExhibitionPlayType] and "gallery_id",
     *     These define the play that should be finished successfully before adding the data
     * @return
     */
    @JvmStatic
    fun AddExhibitionAccumulableDataAfterSuccess(
        context: LuaContextWrapper,
        uid: Int,
        dataKey: String,
        value: Int,
        rawTargetPlayInfoTable: Any
    ): Int {
        return context.onGroupContext {
            onExhibitionHandler {
                checkUid(::AddExhibitionAccumulableDataAfterSuccess, uid)?.let {
                    return@onExhibitionHandler it.getValue()
                }
                val targetPlayInfoTable = context.engine.getTable(rawTargetPlayInfoTable)
                val targetPlayInfo = ExhibitionPlayTarget.fromLuaTable(targetPlayInfoTable) ?:
                    return@onExhibitionHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()

                addExhibitionAccumulableDataAfterSuccess(this@onGroupContext, uid, dataKey, value, targetPlayInfo)
            }
        }
    }


    @JvmStatic
    fun GetExhibitionAccumulableData(context: LuaContextWrapper, uid: Int, exhibitionId: Int): Int {
        return context.onGroupContext {
            onExhibitionHandler {
                checkUid(::GetExhibitionAccumulableData, uid)?.let {
                    return@onExhibitionHandler it.getValue()
                }
                getExhibitionAccumulableData(this@onGroupContext, uid, exhibitionId)
            }
        }
    }

    /**
     * @param context
     * @param uid
     * @param dataKey key under which the data is stored
     * @param value value to add
     * @return
     */
    @JvmStatic
    fun AddExhibitionReplaceableData(context: LuaContextWrapper, uid: Int, dataKey: String, value: Int): Int {
        return context.onGroupContext {
            onExhibitionHandler {
                checkUid(::AddExhibitionReplaceableData, uid)?.let {
                    return@onExhibitionHandler it.getValue()
                }
                addExhibitionReplaceableData(this@onGroupContext, uid, dataKey, value)
            }
        }
    }

    /**
     * @param context
     * @param uid
     * @param dataKey key under which the data is stored
     * @param value value to add
     * @param rawTargetPlayInfoTable contains the fields "play_type" is part of the enum [ExhibitionPlayType] and "gallery_id",
     *     These define the play that should be finished successfully before adding the data
     * @return
     */
    @JvmStatic
    fun AddExhibitionReplaceableDataAfterSuccess(
        context: LuaContextWrapper,
        uid: Int,
        dataKey: String,
        value: Int,
        rawTargetPlayInfoTable: Any
    ): Int {
        return context.onGroupContext {
            onExhibitionHandler {
                checkUid(::AddExhibitionReplaceableDataAfterSuccess, uid)?.let {
                    return@onExhibitionHandler it.getValue()
                }
                val targetPlayInfoTable = context.engine.getTable(rawTargetPlayInfoTable)
                val targetPlayInfo = ExhibitionPlayTarget.fromLuaTable(targetPlayInfoTable) ?:
                return@onExhibitionHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                addExhibitionReplaceableDataAfterSuccess(this@onGroupContext, uid, dataKey, value, targetPlayInfo)
            }
        }
    }

    @JvmStatic
    fun ClearExhibitionReplaceableData(context: LuaContextWrapper, uid: Int, dataKey: String): Int {
        return context.onGroupContext {
            onExhibitionHandler {
                checkUid(::ClearExhibitionReplaceableData, uid)?.let {
                    return@onExhibitionHandler it.getValue()
                }
                clearExhibitionReplaceableData(this@onGroupContext, uid, dataKey)
            }
        }
    }

    @JvmStatic
    fun GetExhibitionReplaceableData(context: LuaContextWrapper, uid: Int, exhibitionId: Int): Int {
        return context.onGroupContext {
            onExhibitionHandler {
                checkUid(::GetExhibitionReplaceableData, uid)?.let {
                    return@onExhibitionHandler it.getValue()
                }
                getExhibitionReplaceableData(this@onGroupContext, uid, exhibitionId)
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


    /*                */
    /* Scene Handlers */
    /*                */

    /* BlossomScriptHandler */


    @JvmStatic
    fun CreateBlossomChestByGroupId(context: LuaContextWrapper, groupId: Int, chestConfigId: Int): Int {
        return context.onGroupContext {
            onBlossomHandler {
                checkGroupIdAndConfigId(::CreateBlossomChestByGroupId, groupId, chestConfigId)?.let {
                    return@onBlossomHandler it.getValue()
                }
                createBlossomChestByGroupId(this@onGroupContext, groupId, chestConfigId)
            }
        }
    }

    @JvmStatic
    fun GetBlossomScheduleStateByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onBlossomHandler {
                checkGroupId(::GetBlossomScheduleStateByGroupId, groupId)?.let {
                    return@onBlossomHandler it.getValue()
                }
                getBlossomScheduleStateByGroupId(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun SetBlossomScheduleStateByGroupId(context: LuaContextWrapper, groupId: Int, state: Int): Int {
        return context.onGroupContext {
            onBlossomHandler {
                setBlossomScheduleStateByGroupId(this@onGroupContext, groupId, state)
            }
        }
    }

    @JvmStatic
    fun RefreshBlossomGroup(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onBlossomHandler {
                val configTable = context.engine.getTable(rawTable)
                val refreshConfig = RefreshBlossomGroupParams.fromLuaTable(configTable) ?: run {
                    scriptLogger.error { "[RefreshBlossomGroup] Invalid or missing refresh config in table $rawTable" }
                    return@onBlossomHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                checkGroupIdAndSuiteId(:: RefreshBlossomGroup, refreshConfig.groupId, refreshConfig.suiteId)?.let {
                    return@onBlossomHandler it.getValue()
                }
                refreshBlossomGroup(this@onGroupContext, refreshConfig)
            }
        }
    }

    @JvmStatic
    fun RefreshBlossomDropRewardByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onBlossomHandler {
                checkGroupId(::RefreshBlossomDropRewardByGroupId, groupId)?.let {
                    return@onBlossomHandler it.getValue()
                }
                refreshBlossomDropRewardByGroupId(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun AddBlossomScheduleProgressByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onBlossomHandler {
                checkGroupId(::AddBlossomScheduleProgressByGroupId, groupId)?.let {
                    return@onBlossomHandler it.getValue()
                }
                addBlossomScheduleProgressByGroupId(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun GetBlossomRefreshTypeByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onBlossomHandler {
                checkGroupId(::GetBlossomRefreshTypeByGroupId, groupId)?.let {
                    return@onBlossomHandler it.getValue()
                }
                getBlossomRefreshTypeByGroupId(this@onGroupContext, groupId)
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
                val challengeParamTable = context.engine.getTable(challengeParams).getAsIntArray().toList()
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
        rawParametersTable: Any
    ): Int {
        return context.onGroupContext {
            onChallengeHandler {
                val parametersTable = context.engine.getTable(rawParametersTable)
                val conditions = CreateFatherChallengeParameters.fromLuaTable(parametersTable) ?: run {
                    scriptLogger.error { "[CreateFatherChallenge] Invalid or missing conditions in table $rawParametersTable" }
                    return@onChallengeHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                createFatherChallenge(this@onGroupContext, challengeIndex, challengeId, timeLimit, conditions)
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
                checkEnumIndex<FatherChallengeProperty>(::ModifyFatherChallengeProperty, propertyTypeIndex)?.let {
                    return@onChallengeHandler it.getValue()
                }
                val propertyType = FatherChallengeProperty.entries[propertyTypeIndex]
                modifyFatherChallengeProperty(this@onGroupContext, challengeId, propertyType, value)
            }
        }
    }

    @JvmStatic
    fun SetChallengeEventMark(context: LuaContextWrapper, challengeId: Int, markType: Int): Int {
        return context.onGroupContext {
            onChallengeHandler {
                checkEnumIndex<ChallengeEventMarkType>(::SetChallengeEventMark, markType)?.let {
                    return@onChallengeHandler it.getValue()
                }
                val markTypeEnum = ChallengeEventMarkType.entries[markType]
                setChallengeEventMark(this@onGroupContext, challengeId, markTypeEnum)
            }
        }
    }

    @JvmStatic
    fun AttachChildChallenge(
        context: LuaContextWrapper, fatherChallengeIndex: Int, childChallengeIndex: Int,
        childChallengeId: Int, rawParametersTable: Any, rawUidListTable: Any, rawPointConfigTable: Any
    ): Int {
        return context.onGroupContext {
            onChallengeHandler {
                val parameterList = context.engine.getTable(rawParametersTable).getAsIntArray().toList()
                val uidList = context.engine.getTable(rawUidListTable).getAsIntArray().toList()
                val pointConfigTable = context.engine.getTable(rawPointConfigTable)
                val pointConfig = AttachChildChallengePointConfig.fromLuaTable(pointConfigTable) ?: run {
                    scriptLogger.error { "[AttachChildChallenge] Invalid or missing point config in table $rawPointConfigTable" }
                    return@onChallengeHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                attachChildChallenge(this@onGroupContext, fatherChallengeIndex, childChallengeIndex, childChallengeId,
                    parameterList, uidList, pointConfig)
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
    fun EnterPersistentDungeon(context: LuaContextWrapper, dungeonId: Int, uid: Int, posTable: Any, rotTable: Any): Int {
        return context.onGroupContext {
            onDungeonHandler {
                val pos = context.engine.getTable(posTable).toVector()
                val rot = context.engine.getTable(rotTable).toVector()

                enterPersistentDungeon(this@onGroupContext, dungeonId, uid, pos, rot)
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


    /* EnvAnimaScriptHandler */

    @JvmStatic
    fun SwitchSceneEnvAnimal(context: LuaContextWrapper, stateId: Int): Int {
        return context.onGroupContext {
            onEnvAnimalHandler {
                checkEnumIndex<EnvAnimalType>(::SwitchSceneEnvAnimal, stateId)?.let {
                    return@onEnvAnimalHandler it.getValue()
                }
                val state = EnvAnimalType.entries[stateId]
                switchSceneEnvAnimal(this@onGroupContext, state)
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
    fun StopGallery(context: LuaContextWrapper, galleryId: Int, isFailed: Boolean): Int {
        return context.onGroupContext {
            onGalleryHandler {
                stopGallery(this@onGroupContext, galleryId, isFailed)
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
    fun UpdatePlayerGalleryScore(context: LuaContextWrapper, galleryId: Int, rawParamsTable: Any): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val paramsTable = context.engine.getTable(rawParamsTable)
                updatePlayerGalleryScore(this@onGroupContext, galleryId, paramsTable)
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
    fun AttachGalleryAbilityGroup(context: LuaContextWrapper, uidListRawTable: Any, galleryId: Int, abilityGroupIndex: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val uiList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
                attachGalleryAbilityGroup(this@onGroupContext, uiList, galleryId, abilityGroupIndex)
            }
        }
    }

    @JvmStatic
    fun AttachGalleryTeamAbilityGroup(
        context: LuaContextWrapper,
        uidListRawTable: Any,
        galleryId: Int,
        abilityGroupIndex: Int
    ): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val uiList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
                attachGalleryTeamAbilityGroup(this@onGroupContext, uiList, galleryId, abilityGroupIndex)
            }
        }
    }

    @JvmStatic
    fun DelGalleryAbilityGroup(context: LuaContextWrapper, uidListRawTable: Any, galleryId: Int, abilityGroupIndex: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val uiList = context.engine.getTable(uidListRawTable).getAsIntArray().toList()
                delGalleryAbilityGroup(this@onGroupContext, uiList, galleryId, abilityGroupIndex)
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
                val progress = context.engine.getTable(progressTable).getAsIntArray().toList()

                checkEnumIndex<GalleryProgressScoreUIType>(::InitGalleryProgressScore, scoreUiTypeIndex)?.let {
                    return@onGalleryHandler it.getValue()
                }
                val uiScoreType = GalleryProgressScoreUIType.entries[scoreUiTypeIndex]

                checkEnumIndex<GalleryProgressScoreType>(::InitGalleryProgressScore, scoreTypeIndex)?.let {
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
                val progress = context.engine.getTable(progressTable).getAsIntArray().toList()

                checkEnumIndex<GalleryProgressScoreUIType>(::InitGalleryProgressWithScore, scoreUiTypeIndex)?.let {
                    return@onGalleryHandler it.getValue()
                }
                val uiScoreType = GalleryProgressScoreUIType.entries[scoreUiTypeIndex]

                checkEnumIndex<GalleryProgressScoreType>(::InitGalleryProgressWithScore, scoreTypeIndex)?.let {
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

    @JvmStatic
    fun StartHomeGallery(context: LuaContextWrapper, galleryId: Int, uid: Int): Int {
        return context.onGroupContext {
            onGalleryHandler {
                checkUid(::StartHomeGallery, uid)?.let {
                    return@onGalleryHandler it.getValue()
                }
                startHomeGallery(this@onGroupContext, galleryId, uid)
            }
        }
    }

    @JvmStatic
    fun UpdateStakeHomePlayRecord(context: LuaContextWrapper, uids: Any): Int {
        return context.onGroupContext {
            onGalleryHandler {
                val uidList = context.engine.getTable(uids).getAsIntArray().toList()
                updateStakeHomePlayRecord(this@onGroupContext, uidList)
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
            onGalleryHandler {
                val position = context.engine.getTable(positionTable).toVector()
                val rotation = context.engine.getTable(rotationTable).toVector()

                setHandballGalleryBallPosAndRot(this@onGroupContext, galleryId, position, rotation)
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
                checkGroupIdAndSuiteId(::AddExtraGroupSuite, groupId, suite)?.let {
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
                checkGroupIdAndSuiteId(::RemoveExtraGroupSuite, groupId, suite)?.let {
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
                checkGroupIdAndSuiteId(::KillExtraGroupSuite, groupId, suite)?.let {
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
                checkGroupIdAndSuiteId(::AddExtraFlowSuite, groupId, suiteId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                checkEnumIndex<FlowSuiteOperatePolicy>(::AddExtraFlowSuite, flowSuitePolicy)?.let {
                    return@onGroupManagementHandler it.getValue()
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
                checkGroupIdAndSuiteId(::RemoveExtraFlowSuite, groupId, suiteId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                checkEnumIndex<FlowSuiteOperatePolicy>(::RemoveExtraFlowSuite, flowSuitePolicy)?.let {
                    return@onGroupManagementHandler it.getValue()
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
                checkGroupIdAndSuiteId(::KillExtraFlowSuite, groupId, suiteId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                checkEnumIndex<FlowSuiteOperatePolicy>(::KillExtraFlowSuite, flowSuitePolicy)?.let {
                    return@onGroupManagementHandler it.getValue()
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
    fun RefreshGroup(context: LuaContextWrapper, rawParamsTable: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                val paramsTable = context.engine.getTable(rawParamsTable)
                val params = RefreshGroupParams.fromLuaTable(paramsTable) ?: run {
                    scriptLogger.error { "[RefreshGroup] Invalid params $paramsTable" }
                    return@onGroupManagementHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                refreshGroup(this@onGroupContext, params)
            }
        }
    }

    @JvmStatic
    fun GetGroupSuite(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::GetGroupSuite, groupId)?.let {
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
                checkGroupId(::SetGroupReplaceable, groupId)?.let {
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
    fun updateBundleMarkShowStateByGroupId(context: LuaContextWrapper, groupId: Int, val2: Boolean): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(ScriptLib::updateBundleMarkShowStateByGroupId, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                updateBundleMarkShowStateByGroupId(this@onGroupContext, groupId, val2)
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
    fun SetGroupTempValue(context: LuaContextWrapper, name: String, value: Int, rawParamsTable: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                val paramsTable = context.engine.getTable(rawParamsTable)
                val groupId = paramsTable.optInt("group_id", 0)
                setGroupTempValue(this@onGroupContext, name, value, groupId)
            }
        }
    }

    @JvmStatic
    fun GetGroupTempValue(context: LuaContextWrapper, name: String, rawParamsTable: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                val paramsTable = context.engine.getTable(rawParamsTable)
                val groupId = paramsTable.optInt("group_id", 0)
                getGroupTempValue(this@onGroupContext, name, groupId)
            }
        }
    }

    @JvmStatic
    fun ChangeGroupTempValue(context: LuaContextWrapper, name: String, diff: Int, rawParamsTable: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                val paramsTable = context.engine.getTable(rawParamsTable)
                val groupId = paramsTable.optInt("group_id", 0)
                changeGroupTempValue(this@onGroupContext, name, diff, groupId)
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
    fun GetCurTriggerCount(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                getCurTriggerCount(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetGroupAliveMonsterList(context: LuaContextWrapper, groupId: Int): IntArray? {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::GetGroupAliveMonsterList, groupId)?.let {
                    return@onGroupManagementHandler intArrayOf(it.getValue())
                }
                getGroupAliveMonsterList(this@onGroupContext, groupId)?.toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetGroupMonsterCountByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::GetGroupMonsterCountByGroupId, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                getGroupMonsterCountByGroupId(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun GetGroupMonsterCount(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                getGroupMonsterCount(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun CheckIsInGroup(context: LuaContextWrapper, groupId: Int, configId: Int): Boolean {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupIdAndConfigId(::CheckIsInGroup, groupId, configId)?.let {
                    return@onGroupManagementHandler it.getValue() > 0
                }
                checkIsInGroup(this@onGroupContext, groupId, configId)
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
        functionName: String,
        callParamsTable: Any
    ): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::ExecuteActiveGroupLua, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                val callParams = context.engine.getTable(callParamsTable).getAsIntArray().toList()
                executeActiveGroupLua(this@onGroupContext, groupId, functionName, callParams)
            }
        }
    }

    /**
     * Calls a lua function in the specified group. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    @JvmStatic
    fun ExecuteGroupLua(context: LuaContextWrapper, groupId: Int, functionName: String, callParamsTable: Any): Int {
        return context.onGroupContext {
            onGroupManagementHandler {
                checkGroupId(::ExecuteGroupLua, groupId)?.let {
                    return@onGroupManagementHandler it.getValue()
                }
                val callParams = context.engine.getTable(callParamsTable).getAsIntArray().toList()
                executeGroupLua(this@onGroupContext, groupId, functionName, callParams)
            }
        }
    }


    /* HuntingScriptHandler */

    @JvmStatic
    fun RefreshHuntingClueGroup(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onHuntingHandler {
                refreshHuntingClueGroup(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetHuntingMonsterExtraSuiteIndexVec(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onHuntingHandler {
                getHuntingMonsterExtraSuiteIndexVec(this@onGroupContext)
            }
        }
    }


    /* MonsterTideScriptHandler*/

    @JvmStatic
    fun ClearPoolMonsterTide(context: LuaContextWrapper, groupId: Int, tideNum: Int): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::ClearPoolMonsterTide, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                clearPoolMonsterTide(this@onGroupContext, groupId, tideNum)
            }
        }
    }

    @JvmStatic
    fun ContinueAutoMonster(context: LuaContextWrapper, groupId: Int, tideNum: Int): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::ContinueAutoMonster, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                continueAutoMonster(this@onGroupContext, groupId, tideNum)
            }
        }
    }

    @JvmStatic
    fun EndMonsterTide(context: LuaContextWrapper, groupId: Int, tideIndex: Int, endType: Int): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::EndMonsterTide, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                endMonsterTide(this@onGroupContext, groupId, tideIndex, endType)
            }
        }
    }

    @JvmStatic
    fun EndPoolMonsterTide(context: LuaContextWrapper, groupId: Int, index: Int): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::EndPoolMonsterTide, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                endPoolMonsterTide(this@onGroupContext, groupId, index)
            }
        }
    }

    @JvmStatic
    fun PauseAutoMonsterTide(context: LuaContextWrapper, groupId: Int, monsterTideIndex: Int): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::PauseAutoMonsterTide, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                pauseAutoMonsterTide(this@onGroupContext, groupId, monsterTideIndex)
            }
        }
    }

    @JvmStatic
    fun PauseAutoPoolMonsterTide(context: LuaContextWrapper, groupId: Int, tideStage: Int): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::PauseAutoPoolMonsterTide, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                pauseAutoPoolMonsterTide(this@onGroupContext, groupId, tideStage)
            }
        }
    }

    @JvmStatic
    fun ResumeAutoPoolMonsterTide(context: LuaContextWrapper, groupId: Int, tideStage: Int): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::ResumeAutoPoolMonsterTide, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                resumeAutoPoolMonsterTide(this@onGroupContext, groupId, tideStage)
            }
        }
    }

    @JvmStatic
    // Some fields are guessed
    fun AutoMonsterTide(
        context: LuaContextWrapper,
        tideId: Int,
        groupId: Int,
        ordersConfigId: Array<Int>,
        tideCount: Int,
        sceneLimit: Int,
        param6: Int
    ): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::AutoMonsterTide, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                autoMonsterTide(this@onGroupContext, tideId, groupId, ordersConfigId.toList(), tideCount, sceneLimit, param6)
            }
        }
    }

    @JvmStatic
    fun KillMonsterTide(context: LuaContextWrapper, groupId: Int, tideId: Int): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::KillMonsterTide, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                killMonsterTide(this@onGroupContext, groupId, tideId)
            }
        }
    }

    @JvmStatic
    fun AutoPoolMonsterTide(
        context: LuaContextWrapper,
        index: Int,
        groupId: Int,
        monsterPools: Any,
        routeId: Int,
        routePoints: Any,
        monsterAffixes: Any,
        monsterPoolParam: Any
    ): Int {
        return context.onGroupContext {
            onMonsterTideHandler {
                checkGroupId(::AutoPoolMonsterTide, groupId)?.let {
                    return@onMonsterTideHandler it.getValue()
                }
                val monsterPoolParamTable = context.engine.getTable(monsterPoolParam)
                val monsterPoolParams = PoolMonsterTideConfig.fromLuaTable(monsterPoolParamTable) ?: run {
                    scriptLogger.error { "AutoPoolMonsterTide: Invalid monsterPoolParamTable" }
                    return@onMonsterTideHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                val monsterPoolList = context.engine.getTable(monsterPools).getAsIntArray().toList()
                val routePointsList = context.engine.getTable(routePoints).getAsIntArray().toList()
                val monsterAffixList = context.engine.getTable(monsterAffixes).getAsIntArray().toList()
                autoPoolMonsterTide(
                    this@onGroupContext,
                    index,
                    groupId,
                    monsterPoolList,
                    routeId,
                    routePointsList,
                    monsterAffixList,
                    monsterPoolParams
                )
            }
        }
    }


    /* RandTaskScriptHandler */

    @JvmStatic
    fun FinishRandTask(context: LuaContextWrapper, optionId: Int, isSuccess: Boolean): Int {
        return context.onGroupContext {
            onRandTaskHandler {
                finishRandTask(this@onGroupContext, optionId, isSuccess)
            }
        }
    }


    /* SceneGadgetChainScriptHandler */

    @JvmStatic
    fun GetChainLevel(context: LuaContextWrapper, uid: Int, chainId: Int): Int {
        return context.onGroupContext {
            onSceneGadgetChainHandler {
                checkUid(::GetChainLevel, uid)?.let {
                    return@onSceneGadgetChainHandler it.getValue()
                }
                getChainLevel(this@onGroupContext, uid, chainId)
            }
        }
    }
    @JvmStatic
    fun SetChainLevel(context: LuaContextWrapper, chainId: Int, level: Int, isNotify: Boolean): Int {
        return context.onGroupContext {
            onSceneGadgetChainHandler {
                setChainLevel(this@onGroupContext, chainId, level, isNotify)
            }
        }
    }


    /* ScenePlayScriptHandler */

    @JvmStatic
    fun AddScenePlayBattleProgress(context: LuaContextWrapper, groupId: Int, progress: Int): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupId(::AddScenePlayBattleProgress, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                addScenePlayBattleProgress(this@onGroupContext, groupId, progress)
            }
        }
    }

    @JvmStatic
    fun CreateScenePlayGeneralRewardGadget(context: LuaContextWrapper, groupId: Int, configId: Int): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupIdAndConfigId(::CreateScenePlayGeneralRewardGadget, groupId, configId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                createScenePlayGeneralRewardGadget(this@onGroupContext, groupId, configId)
            }
        }
    }

    @JvmStatic
    fun FailScenePlayBattle(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupId(::FailScenePlayBattle, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                failScenePlayBattle(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun GetScenePlayBattleHostUid(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupId(::GetScenePlayBattleHostUid, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                getScenePlayBattleHostUid(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun GetScenePlayBattleType(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupId(::GetScenePlayBattleType, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                getScenePlayBattleType(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun GetScenePlayBattleUidValue(context: LuaContextWrapper, groupId: Int, uid: Int, key: String): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkUid(::GetScenePlayBattleUidValue, uid)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                checkGroupId(::GetScenePlayBattleUidValue, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                getScenePlayBattleUidValue(this@onGroupContext, groupId, uid, key)
            }
        }
    }

    @JvmStatic
    fun PrestartScenePlayBattle(context: LuaContextWrapper, rawPlayParam: Any): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                val playParamTable = context.engine.getTable(rawPlayParam)
                val playParam = PrestartScenePlayBattleParams.fromLuaTable(playParamTable) ?: run {
                    scriptLogger.error { "PrestartScenePlayBattle: Invalid playParamTable" }
                    return@onScenePlayHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                prestartScenePlayBattle(this@onGroupContext, playParam)
            }
        }
    }

    @JvmStatic
    fun ScenePlayBattleUidOp(
        context: LuaContextWrapper,
        groupId: Int,
        configId: Int,
        uids: Any,
        buffType: Int,
        paramString: String,
        params: Any,
        paramTargets: Any,
        index: Int,
        duration: Int
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupIdAndConfigId(::ScenePlayBattleUidOp, groupId, configId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                val uidList = context.engine.getTable(uids).getAsIntArray().toList()
                val paramList = context.engine.getTable(params).getAsIntArray().toList()
                val paramTargetList = context.engine.getTable(paramTargets).getAsIntArray().toList()
                scenePlayBattleUidOp(
                    this@onGroupContext,
                    groupId,
                    configId,
                    uidList,
                    buffType,
                    paramString,
                    paramList,
                    paramTargetList,
                    index,
                    duration
                )
            }
        }
    }


    @JvmStatic
    fun SetScenePlayBattlePlayTeamEntityGadgetId(context: LuaContextWrapper, groupId: Int, gadgetId: Int): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupId(::SetScenePlayBattlePlayTeamEntityGadgetId, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                setScenePlayBattlePlayTeamEntityGadgetId(this@onGroupContext, groupId, gadgetId)
            }
        }
    }

    @JvmStatic
    fun SetScenePlayBattleUidValue(
        context: LuaContextWrapper,
        groupId: Int,
        uid: Int,
        key: String,
        value: Int
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkUid(::SetScenePlayBattleUidValue, uid)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                checkGroupId(::SetScenePlayBattleUidValue, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                setScenePlayBattleUidValue(this@onGroupContext, groupId, uid, key, value)
            }
        }
    }

    @JvmStatic
    fun AddSceneMultiStagePlayUidValue(
        context: LuaContextWrapper,
        groupId: Int,
        param2: Int,
        param3: String,
        uid: Int,
        param5: Int
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupId(::AddSceneMultiStagePlayUidValue, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                checkUid(::AddSceneMultiStagePlayUidValue, uid)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                addSceneMultiStagePlayUidValue(this@onGroupContext, groupId, param2, param3, uid, param5)
            }
        }
    }

    @JvmStatic
    fun SetSceneMultiStagePlayUidValue(
        context: LuaContextWrapper,
        groupId: Int,
        index: Int,
        tag: String,
        value: Int
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupId(::SetSceneMultiStagePlayUidValue, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                setSceneMultiStagePlayUidValue(this@onGroupContext, groupId, index, tag, value)
            }
        }
    }

    @JvmStatic
    fun SetSceneMultiStagePlayValue(
        context: LuaContextWrapper,
        index: Int,
        tag: String,
        value: Int,
        isNotify: Boolean
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                setSceneMultiStagePlayValue(this@onGroupContext, index, tag, value, isNotify)
            }
        }
    }

    @JvmStatic
    fun SetSceneMultiStagePlayValues(
        context: LuaContextWrapper,
        index: Int,
        rawParams: Any,
        isNotify: Boolean
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                val paramTable = context.engine.getTable(rawParams)
                val params = SetSceneMultiStagePlayValuesParams.fromLuaTable(paramTable) ?: run {
                    scriptLogger.error { "SetSceneMultiStagePlayValues: Invalid paramTable" }
                    return@onScenePlayHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                setSceneMultiStagePlayValues(this@onGroupContext, index, params, isNotify)
            }
        }
    }

    @JvmStatic
    fun StartSceneMultiStagePlayStage(
        context: LuaContextWrapper,
        index: Int,
        time: Int,
        key: String,
        rawParams: Any
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                val paramTable = context.engine.getTable(rawParams)
                val params = StartSceneMultiStagePlayStageParams.fromLuaTable(paramTable) ?: run {
                    scriptLogger.error { "StartSceneMultiStagePlayStage: Invalid paramTable" }
                    return@onScenePlayHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                startSceneMultiStagePlayStage(this@onGroupContext, index, time, key, params)
            }
        }
    }

    @JvmStatic
    fun EndSceneMultiStagePlay(context: LuaContextWrapper, playIndex: Int, isSuccess: Boolean): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                endSceneMultiStagePlay(this@onGroupContext, playIndex, isSuccess)
            }
        }
    }

    @JvmStatic
    fun EndSceneMultiStagePlayStage(
        context: LuaContextWrapper,
        playIndex: Int,
        stageName: String,
        isSuccess: Boolean
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                endSceneMultiStagePlayStage(this@onGroupContext, playIndex, stageName, isSuccess)
            }
        }
    }

    @JvmStatic
    fun GetSceneMultiStagePlayUidValue(
        context: LuaContextWrapper,
        groupId: Int,
        index: Int,
        name: String,
        uid: Int
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkUid(::GetSceneMultiStagePlayUidValue, uid)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                checkGroupId(::GetSceneMultiStagePlayUidValue, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                getSceneMultiStagePlayUidValue(this@onGroupContext, groupId, index, name, uid)
            }
        }
    }

    @JvmStatic
    fun InitSceneMultistagePlay(
        context: LuaContextWrapper,
        index: Int,
        playType: MultistagePlayType,
        rawParams: Any,
        uids: Any
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                val paramTable = context.engine.getTable(rawParams)
                val params = InitSceneMultistagePlayParams.fromLuaTable(paramTable) ?: run {
                    scriptLogger.error { "InitSceneMultistagePlay: Invalid paramTable" }
                    return@onScenePlayHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                val uidList = context.engine.getTable(uids).getAsIntArray().toList()
                initSceneMultistagePlay(this@onGroupContext, index, playType, params, uidList)
            }
        }
    }

    @JvmStatic
    fun CreateFoundation(
        context: LuaContextWrapper,
        uids: Any,
        configId: Int,
        groupId: Int,
        index: Int
    ): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupIdAndConfigId(::CreateFoundation, groupId, configId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                val uidList = context.engine.getTable(uids).getAsIntArray().toList()
                createFoundation(this@onGroupContext, uidList, configId, groupId, index)
            }
        }
    }

    @JvmStatic
    fun CreateFoundations(context: LuaContextWrapper, foundationParam: Any, groupId: Int, index: Int): Int {
        return context.onGroupContext {
            onScenePlayHandler {
                checkGroupId(::CreateFoundations, groupId)?.let {
                    return@onScenePlayHandler it.getValue()
                }
                val foundationParamTable = context.engine.getTable(foundationParam)
                val keys = foundationParamTable.getKeys().map { it.toInt() }
                val foundationParamsMap = keys.associateWith { foundationParamTable.getInt(it) }
                createFoundations(this@onGroupContext, foundationParamsMap, groupId, index)
            }
        }
    }


    /* ScenePlayerScriptHandler*/

    @JvmStatic
    fun SetIsAllowUseSkill(context: LuaContextWrapper, canUse: Int): Int {
        return context.onGroupContext {
            onScenePlayerHandler {
                setIsAllowUseSkill(this@onGroupContext, canUse)
            }
        }
    }


    @JvmStatic
    fun IsPlayerAllAvatarDie(context: LuaContextWrapper, uid: Int): Boolean {
        return context.onGroupContext {
            onScenePlayerHandler {
                checkUid(::IsPlayerAllAvatarDie, uid)?.let {
                    return@onScenePlayerHandler false
                }
                isPlayerAllAvatarDie(this@onGroupContext, uid)
            }
        }
    }

    @JvmStatic
    fun BeginCameraSceneLookWithTemplate(context: LuaContextWrapper, var1: Int, camParam: Any): Int {
        return context.onGroupContext {
            onScenePlayerHandler {
                val camParams = context.engine.getTable(camParam).let {
                    BeginCameraSceneLookTemplateParams.fromLuaTable(it)
                }?: run {
                    return@onScenePlayerHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                beginCameraSceneLookWithTemplate(this@onGroupContext, var1, camParams)
            }
        }
    }

    @JvmStatic
    fun EnterCurve(context: LuaContextWrapper, uid: Int, curveId: Int, pointId: Int, curveTypeId: Int): Int {
        return context.onGroupContext {
            onScenePlayerHandler {
                checkUid(::EnterCurve, uid)?.let {
                    return@onScenePlayerHandler it.getValue()
                }

                checkEnumIndex<CurveType>(::EnterCurve, curveTypeId )?.let {
                    return@onScenePlayerHandler it.getValue()
                }
                val curveType = CurveType.entries[curveTypeId]

                enterCurve(this@onGroupContext, uid, curveId, pointId, curveType)
            }
        }
    }

    @JvmStatic
    fun GetPlayerVehicleType(context: LuaContextWrapper, uid: Int): Int {
        return context.onGroupContext {
            onScenePlayerHandler {
                checkUid(::GetPlayerVehicleType, uid)?.let {
                    return@onScenePlayerHandler it.getValue()
                }
                getPlayerVehicleType(this@onGroupContext, uid).ordinal
            }
        }
    }

    @JvmStatic
    fun IsPlayerTransmittable(context: LuaContextWrapper, uid: Int): Boolean {
        return context.onGroupContext {
            onScenePlayerHandler {
                checkUid(::IsPlayerTransmittable, uid)?.let {
                    return@onScenePlayerHandler it.getValue() > 0
                }
                isPlayerTransmittable(this@onGroupContext, uid)
            }
        }
    }

    @JvmStatic
    fun IsWidgetEquipped(context: LuaContextWrapper, uid: Int, widgetId: Int): Boolean {
        return context.onGroupContext {
            onScenePlayerHandler {
                checkUid(::IsWidgetEquipped, uid)?.let {
                    return@onScenePlayerHandler it.getValue() > 0
                }
                isWidgetEquipped(this@onGroupContext, uid, widgetId)
            }
        }
    }
    @JvmStatic
    fun SetWidgetClientDetectorCoolDown(context: LuaContextWrapper, widgetId: Int, isSuccess: Boolean): Int {
        return context.onGroupContext {
            onScenePlayerHandler {
                setWidgetClientDetectorCoolDown(this@onGroupContext, widgetId, isSuccess)
            }
        }
    }

    @JvmStatic
    fun MovePlayerToPos(context: LuaContextWrapper, moveParamsTable: Any): Int {
        return context.onGroupContext {
            onScenePlayerHandler {
                val moveParams = context.engine.getTable(moveParamsTable)
                val targets = moveParams.getTable("uid_list")?.getAsIntArray()?.toList()
                val pos = moveParams.getTable("pos")?.toVector()
                val rot = moveParams.getTable("rot")?.toVector() ?: PositionImpl()
                val radius = moveParams.optInt("radius", -1)
                val isSkipUi = moveParams.optBoolean("is_skip_ui", false)


                if (targets == null || targets.isEmpty() || pos == null) {
                    scriptLogger.error { "[MovePlayerToPos] Invalid params, either missing uid_list or pos" }
                    return@onScenePlayerHandler 1
                }

                movePlayerToPos(this@onGroupContext, targets, pos, rot, radius, isSkipUi)
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
            onScenePlayerHandler {
                val transportationParams = context.engine.getTable(transportationParamsTable)
                val targets = transportationParams.getTable("uid_list")?.getAsIntArray()?.toList()
                val pos = transportationParams.getTable("pos")?.toVector()
                val rot = transportationParams.getTable("rot")?.toVector() ?: PositionImpl()
                val radius = transportationParams.optInt("radius", -1)
                val isSkipUi = transportationParams.optBoolean("is_skip_ui", false)
                val sceneId = transportationParams.optInt("scene_id", -1)


                if (targets == null || targets.isEmpty() || pos == null) {
                    scriptLogger.error { "[TransPlayerToPos] Invalid params, either missing uid_list or pos" }
                    return@onScenePlayerHandler 1
                }

                transPlayerToPos(this@onGroupContext, targets, pos, rot, radius, isSkipUi, sceneId)
            }
        }
    }

    @JvmStatic
    fun PlayCutScene(context: LuaContextWrapper, cutsceneId: Int, waitTime: Int): Int {
        return context.onGroupContext {
            onScenePlayerHandler {
                playCutScene(this@onGroupContext, cutsceneId, waitTime)
            }
        }
    }

    @JvmStatic
    fun PlayCutSceneWithParam(context: LuaContextWrapper, cutsceneId: Int, waitTime: Int, rawParamListTable: Any): Int {
        return context.onGroupContext {
            onScenePlayerHandler {
                val paramListTable = context.engine.getTable(rawParamListTable)
                val paramList = mutableListOf<List<Double>>()
                for(i in 1..paramListTable.getSize()){
                    paramListTable.getTable(i)?.getAsDoubleArray()?.toList()?.let {
                        paramList+= it
                    } ?: run {
                        scriptLogger.error { "[PlayCutSceneWithParam] failed to get for index $i" }
                    }
                }
                if(paramList.isEmpty()){
                    scriptLogger.error { "[PlayCutSceneWithParam] Invalid params, paramList can't be empty" }
                    return@onScenePlayerHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                playCutSceneWithParam(this@onGroupContext, cutsceneId, waitTime, paramList)
            }
        }
    }

    @JvmStatic
    fun BeginCameraSceneLook(context: LuaContextWrapper, sceneLookParamsTable: Any): Int {
        return context.onGroupContext {
            onScenePlayerHandler {
                val camParams = context.engine.getTable(sceneLookParamsTable).let {
                    BeginCameraSceneLookParams.fromLuaTable(it)
                }?: run {
                    return@onScenePlayerHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                beginCameraSceneLook(this@onGroupContext, camParams)
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
    fun ChangeToTargetLevelTag(context: LuaContextWrapper, levelTagId: Int): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                changeToTargetLevelTag(this@onGroupContext, levelTagId)
            }
        }
    }

    @JvmStatic
    fun ChangeToTargetLevelTagWithParamTable(context: LuaContextWrapper, levelTagId: Int, changeLevelTagParamsTable: Any): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                val paramsTable = context.engine.getTable(changeLevelTagParamsTable)
                val params = ChangeLevelTagParams.fromLuaTable(paramsTable) ?: run {
                    scriptLogger.error { "[ChangeToTargetLevelTagWithParamTable]: Invalid paramTable" }
                    return@onSceneStateHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                changeToTargetLevelTagWithParamTable(this@onGroupContext, levelTagId, params)
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

    @JvmStatic
    fun GetSceneOwnerUid(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onSceneStateHandler {
                getSceneOwnerUid(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetSceneUidList(context: LuaContextWrapper): Any {
        return context.onGroupContext {
            onSceneStateHandler {
                val list = getSceneUidList(this@onGroupContext)
                val result = context.engine.createTable()

                for (i in list.indices) {
                    result[(i + 1).toString()] = list[i]
                }
                result.getRawTable()
            }
        }
    }

    @JvmStatic
    fun CheckIsInMpMode(context: LuaContextWrapper): Boolean {
        return context.onGroupContext {
            onSceneStateHandler {
                checkIsInMpMode(this@onGroupContext)
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


    /* TimersScriptHandler */

    @JvmStatic
    fun CreateGroupTimerEvent(context: LuaContextWrapper, groupID: Int, source: String, time: Double): Int {
        return context.onGroupContext {
            onTimersHandler {
                checkGroupId(::CreateGroupTimerEvent, groupID)?.let {
                    return@onTimersHandler it.getValue()
                }
                createGroupTimerEvent(this@onGroupContext, groupID, source, time)
            }
        }
    }

    @JvmStatic
    fun CancelGroupTimerEvent(context: LuaContextWrapper, groupID: Int, source: String): Int {
        return context.onGroupContext {
            onTimersHandler {
                checkGroupId(::CancelGroupTimerEvent, groupID)?.let {
                    return@onTimersHandler it.getValue()
                }
                cancelGroupTimerEvent(this@onGroupContext, groupID, source)
            }
        }
    }

    @JvmStatic
    fun PauseTimeAxis(context: LuaContextWrapper, timeAxisKey: String): Int {
        return context.onGroupContext {
            onTimersHandler {
                pauseTimeAxis(this@onGroupContext, timeAxisKey)
            }
        }
    }

    @JvmStatic
    fun ContinueTimeAxis(context: LuaContextWrapper, timeAxisKey: String): Int {
        return context.onGroupContext {
            onTimersHandler {
                continueTimeAxis(this@onGroupContext, timeAxisKey)
            }
        }
    }

    @JvmStatic
    fun InitTimeAxis(context: LuaContextWrapper, timeAxisKey: String, timersTable: Any, loop: Boolean): Int {
        return context.onGroupContext {
            onTimersHandler {
                val timers = context.engine.getTable(timersTable).getAsFloatArray().toList()
                initTimeAxis(this@onGroupContext, timeAxisKey, timers, loop)
            }
        }
    }

    @JvmStatic
    fun EndTimeAxis(context: LuaContextWrapper, timeAxisKey: String): Int {
        return context.onGroupContext {
            onTimersHandler {
                endTimeAxis(this@onGroupContext, timeAxisKey)
            }
        }
    }

    @JvmStatic
    fun EndAllTimeAxis(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onTimersHandler {
                endAllTimeAxis(this@onGroupContext)
            }
        }
    }


    /* WeatherScriptHandler */
    @JvmStatic
    fun SetWeatherAreaState(context: LuaContextWrapper, var1: Int, var2: Int): Int {
        return context.onGroupContext {
            onWeatherHandler {
                setWeatherAreaState(this@onGroupContext, var1, var2 != 0)
            }
        }
    }

    @JvmStatic
    fun EnterWeatherArea(context: LuaContextWrapper, weatherAreaId: Int): Int {
        return context.onGroupContext {
            onWeatherHandler {
                enterWeatherArea(this@onGroupContext, weatherAreaId)
            }
        }
    }
    @JvmStatic
    fun ModifyClimatePolygonParamTable(context: LuaContextWrapper, one: Int, climate: Any): Int {
        return context.onGroupContext {
            onWeatherHandler {
                val climateTable = context.engine.getTable(climate).let {
                    ModifyClimatePolygonParams.fromLuaTable(it)
                }?: run {
                    return@onWeatherHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                modifyClimatePolygonParamTable(this@onGroupContext, one, climateTable)
            }
        }
    }



    /*                */
    /* Other Handlers */
    /*                */

    /* AranaraScriptHandler */

    @JvmStatic
    fun ReceiveAllAranaraCollectionByType(context: LuaContextWrapper, groupId: Int, type: Int): Int {
        return context.onGroupContext {
            onAranaraHandler {
                checkGroupId(::RecieveAllAranaraCollectionByType, groupId)?.let {
                    return@onAranaraHandler it.getValue()
                }
                receiveAllAranaraCollectionByType(this@onGroupContext, groupId, type)
            }
        }
    }
    /* compat function with typo*/
    @Suppress("SpellCheckingInspection")
    @JvmStatic
    fun RecieveAllAranaraCollectionByType(context: LuaContextWrapper, groupId: Int, type: Int): Int {
        return ReceiveAllAranaraCollectionByType(context, groupId, type)
    }

    @JvmStatic
    fun GetAranaraCollectableCountByTypeAndState(context: LuaContextWrapper, type: Int, state: Int): Int {
        return context.onGroupContext {
            onAranaraHandler {
                getAranaraCollectableCountByTypeAndState(this@onGroupContext, type, state)
            }
        }
    }


    /* OfferingScriptHandler */

    @JvmStatic
    fun GetOfferingLevel(context: LuaContextWrapper, offeringId: Int): Int {
        return context.onGroupContext {
            onOfferingHandler {
                getOfferingLevel(this@onGroupContext, offeringId)
            }
        }
    }


    /* RegionalPlayScriptHandler */

    @JvmStatic
    fun GetRegionalPlayVarValue(context: LuaContextWrapper, uid: Int, type: Int): Int {
        return context.onGroupContext {
            onRegionalPlayHandler {
                getRegionalPlayVarValue(this@onGroupContext, uid, type)
            }
        }
    }

    @JvmStatic
    fun AddRegionalPlayVarValue(context: LuaContextWrapper, uid: Int, regionId: Int, delta: Int): Int {
        return context.onGroupContext {
            onRegionalPlayHandler {
                checkUid(::AddRegionalPlayVarValue, uid)?.let {
                    return@onRegionalPlayHandler it.getValue()
                }
                addRegionalPlayVarValue(this@onGroupContext, uid, regionId, delta)
            }
        }
    }


    /* TimeScriptHandler */

    @JvmStatic
    fun GetServerTime(context: LuaContextWrapper): Long {
        return context.onGroupContext {
            onTimeHandler {
                getServerTime(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetServerTimeByWeek(context: LuaContextWrapper): Long {
        return context.onGroupContext {
            onTimeHandler {
                getServerTimeByWeek(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetGameHour(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onTimeHandler {
                getGameHour(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetGameTimePassed(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onTimeHandler {
                getGameTimePassed(this@onGroupContext).let {
                    intArrayOf(it.hour, it.minutes)
                }
            }
        }
    }

    @JvmStatic
    fun SkipTeyvatTime(context: LuaContextWrapper, time: Int, rate: Int): Int {
        return context.onGroupContext {
            onTimeHandler {
                skipTeyvatTime(this@onGroupContext, time, rate)
            }
        }
    }

    @JvmStatic
    fun GetSceneTimeSeconds(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onTimeHandler {
                getSceneTimeSeconds(this@onGroupContext)
            }
        }
    }


    /* TowerScriptHandler */

    @JvmStatic
    fun TowerCountTimeStatus(context: LuaContextWrapper, isDone: Int): Int {
        return context.onGroupContext {
            onTowerHandler {
                towerCountTimeStatus(this@onGroupContext, isDone)
            }
        }
    }

    @JvmStatic
    fun TowerMirrorTeamSetUp(context: LuaContextWrapper, team: Int): Int {
        return context.onGroupContext {
            onTowerHandler {
                towerMirrorTeamSetUp(this@onGroupContext, team)
            }
        }
    }


    /* VisionScriptHandler */

    @JvmStatic
    fun AddPlayerGroupVisionType(context: LuaContextWrapper, uidsTable: Any, visionTypesTable: Any): Int {
        return context.onGroupContext {
            onVisionHandler {
                val uids = context.engine.getTable(uidsTable).getAsIntArray().toList()
                val visionTypes = context.engine.getTable(visionTypesTable).getAsIntArray().toList()
                addPlayerGroupVisionType(this@onGroupContext, uids, visionTypes)
            }
        }
    }

    @JvmStatic
    fun DelPlayerGroupVisionType(context: LuaContextWrapper, uidsTable: Any, visionTypesTable: Any): Int {
        return context.onGroupContext {
            onVisionHandler {
                val uids = context.engine.getTable(uidsTable).getAsIntArray().toList()
                val visionTypes = context.engine.getTable(visionTypesTable).getAsIntArray().toList()
                delPlayerGroupVisionType(this@onGroupContext, uids, visionTypes)
            }
        }
    }

    @JvmStatic
    fun SetPlayerGroupVisionType(context: LuaContextWrapper, uidsTable: Any, visionTypesTable: Any): Int {
        return context.onGroupContext {
            onVisionHandler {
                val uids = context.engine.getTable(uidsTable).getAsIntArray().toList()
                val visionTypes = context.engine.getTable(visionTypesTable).getAsIntArray().toList()
                setPlayerGroupVisionType(this@onGroupContext, uids, visionTypes)
            }
        }
    }

    @JvmStatic
    fun SetPlayerEyePointStream(context: LuaContextWrapper, var1: Int, var2: Int, var3: Boolean): Int {
        return context.onGroupContext {
            onVisionHandler {
                setPlayerEyePointStream(this@onGroupContext, var1, var2, var3)
            }
        }
    }

    @JvmStatic
    fun SetPlayerEyePoint(context: LuaContextWrapper, configId: Int, configId2: Int): Int {
        return context.onGroupContext {
            onVisionHandler {
                checkConfigId(::SetPlayerEyePoint, configId)?.let {
                    return@onVisionHandler it.getValue()
                }
                checkConfigId(::SetPlayerEyePoint, configId2)?.let {
                    return@onVisionHandler it.getValue()
                }
                setPlayerEyePoint(this@onGroupContext, configId, configId2)
            }
        }
    }

    @JvmStatic
    fun SetPlayerEyePointLOD(context: LuaContextWrapper, configId: Int, configId2: Int, lodLevel: Int): Int {
        return context.onGroupContext {
            onVisionHandler {
                checkConfigId(::SetPlayerEyePointLOD, configId)?.let {
                    return@onVisionHandler it.getValue()
                }
                checkConfigId(::SetPlayerEyePointLOD, configId2)?.let {
                    return@onVisionHandler it.getValue()
                }
                setPlayerEyePointLOD(this@onGroupContext, configId, configId2, lodLevel)
            }
        }
    }

    @JvmStatic
    fun ClearPlayerEyePoint(context: LuaContextWrapper, var1: Int): Int {
        return context.onGroupContext {
            onVisionHandler {
                clearPlayerEyePoint(this@onGroupContext, var1)
            }
        }
    }

    @JvmStatic
    fun ForbidPlayerRegionVision(context: LuaContextWrapper, uid: Int): Int {
        return context.onGroupContext {
            onVisionHandler {
                checkUid(::ForbidPlayerRegionVision, uid)?.let {
                    return@onVisionHandler it.getValue()
                }
                forbidPlayerRegionVision(this@onGroupContext, uid)
            }
        }
    }


    @JvmStatic
    fun RevertPlayerRegionVision(context: LuaContextWrapper, uid: Int): Int {
        return context.onGroupContext {
            onVisionHandler {
                checkUid(::RevertPlayerRegionVision, uid)?.let {
                    return@onVisionHandler it.getValue()
                }
                revertPlayerRegionVision(this@onGroupContext, uid)
            }
        }
    }


    @JvmStatic
    fun MoveAvatarByPointArrayWithTemplate(
        context: LuaContextWrapper,
        uid: Int,
        pointArrayId: Int,
        routes: Any,
        templateId: Int,
        rawMoveParamsTable: Any
    ): Int {
        return context.onGroupContext {
            onVisionHandler {
                checkUid(::MoveAvatarByPointArrayWithTemplate, uid)?.let {
                    return@onVisionHandler it.getValue()
                }
                val paramsTable = context.engine.getTable(rawMoveParamsTable)
                val speed = paramsTable.optFloat("speed", -1f)
                val routeList = context.engine.getTable(routes).getAsIntArray().toList()
                moveAvatarByPointArrayWithTemplate(
                    this@onGroupContext,
                    uid,
                    pointArrayId,
                    routeList,
                    templateId,
                    speed
                )
                //TODO implement speed is sometimes int speed=10 and sometimes {4,60} (3.6 dev scene)
            }
        }
    }

    @JvmStatic
    fun MoveAvatarByPointArray(
        context: LuaContextWrapper,
        uid: Int,
        targetId: Int,
        routeListTable: Any,
        routeParamsTable: Any,
        clientParams: String
    ): Int {
        return context.onGroupContext {
            onVisionHandler {
                val routeList = context.engine.getTable(routeListTable).getAsIntArray().toList()
                val routeParams = context.engine.getTable(routeParamsTable)
                val speed = routeParams.optFloat("speed", -1f)
                if(speed == -1f){
                    scriptLogger.error { "[MoveAvatarByPointArray] Invalid params, missing speed parameter" }
                    return@onVisionHandler 1
                }
                moveAvatarByPointArray(this@onGroupContext, uid, targetId, routeList, speed, clientParams)
            }
        }
    }

    @JvmStatic
    fun SetMonsterBattleByGroup(context: LuaContextWrapper, configId: Int, groupId: Int): Int {
        return context.onGroupContext {
            onVisionHandler {
                checkGroupIdAndConfigId(::SetMonsterBattleByGroup, groupId, configId)?.let {
                    return@onVisionHandler it.getValue()
                }
                setMonsterBattleByGroup(this@onGroupContext, configId, groupId)
            }
        }
    }


    /* MiscNotifyScriptHandler*/


    @JvmStatic
    fun NotifyAllPlayerPerformOperation(
        context: LuaContextWrapper,
        teamEntityId: Int,
        type: Int,
        effectIndex: Int,
        hunterPos: Any,
        hunterRot: Any
    ): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val pos = context.engine.getTable(hunterPos).toVector()
                val rot = context.engine.getTable(hunterRot).toVector()
                notifyAllPlayerPerformOperation(
                    this@onGroupContext,
                    teamEntityId,
                    type,
                    effectIndex,
                    pos,
                    rot
                )
            }
        }
    }

    @JvmStatic
    fun SetEnvironmentEffectState(
        context: LuaContextWrapper,
        index: Int,
        key: String,
        floatParamTable: Any,
        intParams: Any
    ): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val intParamList = context.engine.getTable(intParams).getAsIntArray().toList()
                val floatParam = context.engine.getTable(floatParamTable).getAsFloatArray().toList()
                setEnvironmentEffectState(this@onGroupContext, index, key, floatParam, intParamList)
            }
        }
    }

    @JvmStatic
    fun SetLimitOptimization(context: LuaContextWrapper, uid: Int, isLimitOptimization: Boolean): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                setLimitOptimization(this@onGroupContext, uid, isLimitOptimization)
            }
        }
    }
    @JvmStatic
    fun SetPlayerInteractOption(context: LuaContextWrapper, key: String): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                setPlayerInteractOption(this@onGroupContext, key)
            }
        }
    }

    @JvmStatic
    fun ShowClientTutorial(context: LuaContextWrapper, tutorialId: Int, uids: Any): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val uidList = context.engine.getTable(uids).getAsIntArray().toList()
                showClientTutorial(this@onGroupContext, tutorialId, uidList)
            }
        }
    }

    @JvmStatic
    fun ShowCommonPlayerTips(context: LuaContextWrapper, type: Int, keys: Any): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val keysTable = context.engine.getTable(keys).getAsStringArray().toList()
                showCommonPlayerTips(this@onGroupContext, type, keysTable)
            }
        }
    }


    @JvmStatic
    fun ScenePlaySound(context: LuaContextWrapper, soundInfoTable: Any): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val soundInfo = context.engine.getTable(soundInfoTable).let {
                    ScenePlaySoundParams.fromLuaTable(it)
                } ?: run {
                    return@onMiscNotifyHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                scenePlaySound(this@onGroupContext, soundInfo)
            }
        }
    }
    @JvmStatic
    fun SendServerMessageByLuaKey(context: LuaContextWrapper, stringKey: String, targetsTable: Any): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val targets = context.engine.getTable(targetsTable).getAsIntArray().toList()
                sendServerMessageByLuaKey(this@onGroupContext, stringKey, targets)
            }
        }
    }

    @JvmStatic
    fun sendShowCommonTipsToClient(
        context: LuaContextWrapper,
        title: String,
        content: String,
        closeTime: Int
    ): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                sendShowCommonTipsToClient(this@onGroupContext, title, content, closeTime)
            }
        }
    }

    @JvmStatic
    fun sendCloseCommonTipsToClient(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                sendCloseCommonTipsToClient(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun AssignPlayerShowTemplateReminder(context: LuaContextWrapper, reminderId: Int, paramsTable: Any): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val params = context.engine.getTable(paramsTable).let {
                    AssignPlayerShowTemplateReminderParams.fromLuaTable(it)
                } ?: run {
                    return@onMiscNotifyHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                assignPlayerShowTemplateReminder(this@onGroupContext, reminderId, params)
            }
        }
    }

    @JvmStatic
    fun RevokePlayerShowTemplateReminder(context: LuaContextWrapper, reminderId: Int, uidListTable: Any): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val uidList = context.engine.getTable(uidListTable).getAsIntArray().toList()
                revokePlayerShowTemplateReminder(this@onGroupContext, reminderId, uidList)
            }
        }
    }
    @JvmStatic
    fun ShowReminder(context: LuaContextWrapper, reminderId: Int): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                showReminder(this@onGroupContext, reminderId)
            }
        }
    }

    @JvmStatic
    fun StopReminder(context: LuaContextWrapper, reminderId: Int): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                stopReminder(this@onGroupContext, reminderId)
            }
        }
    }

    @JvmStatic
    fun ShowReminderRadius(context: LuaContextWrapper, var1: Int, var2Table: Any, var3: Int): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val var2 = context.engine.getTable(var2Table).toVector()
                showReminderRadius(this@onGroupContext, var1, var2, var3)
            }
        }
    }

    @JvmStatic
    fun ShowReminderByUid(context: LuaContextWrapper, uids: Any, reminderId: Int): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val uidList = context.engine.getTable(uids).getAsIntArray().toList()
                showReminderByUid(this@onGroupContext, uidList, reminderId)
            }
        }
    }

    @JvmStatic
    fun ShowTemplateReminder(context: LuaContextWrapper, reminderId: Int, timerInfo: Any): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val timerInfoList = context.engine.getTable(timerInfo).getAsIntArray().toList()
                showTemplateReminder(this@onGroupContext, reminderId, timerInfoList)
            }
        }
    }

    @JvmStatic
    fun ShowClientGuide(context: LuaContextWrapper, guideName: String): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                showClientGuide(this@onGroupContext, guideName)
            }
        }
    }

    /**
     * @param paramsTable contains the following fields: param_index:int, param_list:Table, param_uid_list:Table,
     * duration:int, target_uid_list:Table
     */
    @JvmStatic
    fun AssignPlayerUidOpNotify(context: LuaContextWrapper, paramsTable: Any): Int {
        return context.onGroupContext {
            onMiscNotifyHandler {
                val params = context.engine.getTable(paramsTable)?.let {
                    AssignPlayerUidOpNotifyParams.fromLuaTable(it)
                } ?: run {
                    return@onMiscNotifyHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                assignPlayerUidOpNotify(this@onGroupContext, params)
            }
        }
    }


    /*                   */
    /* Activity Handlers */
    /*                   */

    /* GeneralActivityHandler */

    @JvmStatic
    fun GetActivityOpenAndCloseTimeByScheduleId(context: LuaContextWrapper, scheduleId: Int): Any {
        return context.onGroupContext {
            onActivityHandler {
                val result = context.engine.createTable()
                getActivityOpenAndCloseTimeByScheduleId(this@onGroupContext, scheduleId)?.toLuaTable(result)

                result.getRawTable()
            }
        }
    }

    @JvmStatic
    fun TryRecordActivityPushTips(context: LuaContextWrapper, pushTipId: Int): Int {
        return context.onGroupContext {
            onActivityHandler {
                tryRecordActivityPushTips(this@onGroupContext, pushTipId)
            }
        }
    }


    /* AsterScriptHandler */

    @JvmStatic
    fun CreateAsterMidGeneralRewardGadget(context: LuaContextWrapper, rawParamsTable: Any): Int {
        return context.onGroupContext {
            onAsterHandler {
                val paramsTable = context.engine.getTable(rawParamsTable)
                val configId = paramsTable.optInt("config_id", -1)
                val difficultyId = paramsTable.optInt("difficulty_id", -1)
                checkConfigId(::CreateAsterMidGeneralRewardGadget, configId, isTableParam = true)?.let {
                    return@onAsterHandler it.getValue()
                }
                if (difficultyId == -1) {
                    scriptLogger.error { "[CreateAsterMidGeneralRewardGadget] Invalid difficulty id $difficultyId" }
                    return@onAsterHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                createAsterMidGeneralRewardGadget(this@onGroupContext, configId, difficultyId)
            }
        }
    }

    /* ChannelerSlabHandler */

    @JvmStatic
    fun IsChannellerSlabLoopDungeonConditionSelected(context: LuaContextWrapper, conditionId: Int): Boolean {
        return context.onGroupContext {
            onChannelerSlapHandler {
                isChannellerSlabLoopDungeonConditionSelected(this@onGroupContext, conditionId)
            }
        }
    }

    @JvmStatic
    fun CreateChannellerSlabCampRewardGadget(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onChannelerSlapHandler {
                createChannellerSlabCampRewardGadget(this@onGroupContext, configId)
            }
        }
    }

    @JvmStatic
    fun GetChannellerSlabLoopDungeonLimitTime(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onChannelerSlapHandler {
                getChannellerSlabLoopDungeonLimitTime(this@onGroupContext)
            }
        }
    }


    /* CharAmusementScriptHandler */

    @JvmStatic
    fun CharAmusementMultistagePlaySwitchTeam(context: LuaContextWrapper, groupId: Int, var2: Int, stageIndex: Int): Int {
        return context.onGroupContext {
            onCharAmusementHandler {
                checkGroupId(::CharAmusementMultistagePlaySwitchTeam, groupId)?.let {
                    return@onCharAmusementHandler it.getValue()
                }
                charAmusementMultistagePlaySwitchTeam(this@onGroupContext, groupId, var2, stageIndex)
            }
        }
    }

    @JvmStatic
    fun CharAmusementUpdateScore(context: LuaContextWrapper, groupId: Int, var2: Int, scoreChange:Int): Int {
        return context.onGroupContext {
            onCharAmusementHandler {
                checkGroupId(::CharAmusementUpdateScore, groupId)?.let {
                    return@onCharAmusementHandler it.getValue()
                }
                charAmusementUpdateScore(this@onGroupContext, groupId, var2, scoreChange)
            }
        }
    }

    @JvmStatic
    fun GetCharAmusementGalleryTarget(context: LuaContextWrapper, galleryId: Int, isMultiplayer: Boolean): Int {
        return context.onGroupContext {
            onCharAmusementHandler {
                getCharAmusementGalleryTarget(this@onGroupContext, galleryId, isMultiplayer)
            }
        }
    }

    @JvmStatic
    fun GetCharAmusementMultistagePlayGalleryIdVec(context: LuaContextWrapper, groupId: Int, var2: Int): IntArray {
        return context.onGroupContext {
            onCharAmusementHandler {
                checkGroupId(::GetCharAmusementMultistagePlayGalleryIdVec, groupId)?.let {
                    return@onCharAmusementHandler intArrayOf(it.getValue())
                }
                getCharAmusementMultistagePlayGalleryIdVec(this@onGroupContext, groupId, var2).toIntArray()
            }
        }
    }


    /* ChessScriptHandler */

    @JvmStatic
    fun AddChessBuildingPoints(
        context: LuaContextWrapper,
        groupId: Int,
        playIndex: Int,
        uid: Int,
        pointsToAdd: Int
    ): Int {
        return context.onGroupContext {
            onChessHandler {
                checkGroupId(::AddChessBuildingPoints, groupId)?.let {
                    return@onChessHandler it.getValue()
                }
                checkUid(::AddChessBuildingPoints, uid)?.let {
                    return@onChessHandler it.getValue()
                }
                addChessBuildingPoints(this@onGroupContext, groupId, playIndex, uid, pointsToAdd)
            }
        }
    }

    @JvmStatic
    fun GetChessMonsterPoolIdVecByRound(context: LuaContextWrapper, groupId: Int, playIndex: Int, waveNumber: Int): IntArray? {
        return context.onGroupContext {
            onChessHandler {
                checkGroupId(::GetChessMonsterPoolIdVecByRound, groupId)?.let {
                    return@onChessHandler intArrayOf(it.getValue())
                }
                getChessMonsterPoolIdVecByRound(this@onGroupContext, groupId, playIndex, waveNumber)?.toIntArray()
            }
        }
    }

    @JvmStatic
    fun SetChessMystery(context: LuaContextWrapper, groupId: Int, playIndex: Int, rawChessPreviewTable: Any): Int {
        return context.onGroupContext {
            onChessHandler {
                checkGroupId(::SetChessMystery, groupId)?.let {
                    return@onChessHandler it.getValue()
                }
                val chessPreviewTable = context.engine.getTable(rawChessPreviewTable)
                val chessPreviewInfo = ChessPreviewInfo.fromLuaTable(chessPreviewTable) ?: run {
                    scriptLogger.error { "[SetChessMystery] Invalid chess preview info" }
                    return@onChessHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                setChessMystery(this@onGroupContext, groupId, playIndex, chessPreviewInfo)
            }
        }
    }


    /* CoinCollectScriptHandler */

    @JvmStatic
    fun GetCoinCollectGalleryPlayerSkillInfo(context: LuaContextWrapper, uid: Int, galleryId: Int): IntArray {
        return context.onGroupContext {
            onCoinCollectHandler {
                checkUid(::GetCoinCollectGalleryPlayerSkillInfo, uid)?.let {
                    return@onCoinCollectHandler intArrayOf(it.getValue())
                }
                getCoinCollectGalleryPlayerSkillInfo(this@onGroupContext, uid, galleryId).toIntArray()
            }
        }
    }


    /* CrystalLinkScriptHandler */

    @JvmStatic
    fun CrystalLinkDungeonTeamSetUp(context: LuaContextWrapper, configId: Int, rawSetupParamsTable: Any): Int {
        return context.onGroupContext {
            onCrystalLinkHandler {
                checkConfigId(::CrystalLinkDungeonTeamSetUp, configId)?.let {
                    return@onCrystalLinkHandler it.getValue()
                }
                val setupParamsTable = context.engine.getTable(rawSetupParamsTable)
                val setupParams = setupParamsTable.asObject(CrystalLinkTeamSetupParams::class.java) ?: run {
                    scriptLogger.error { "[CrystalLinkDungeonTeamSetUp] Invalid setup params table" }
                    return@onCrystalLinkHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }

                crystalLinkDungeonTeamSetUp(this@onGroupContext, configId, setupParams)
            }
        }
    }


    /* CrystalLinkScriptHandler */

    @JvmStatic
    fun DigRetractAllWidget(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onDigHandler {
                digRetractAllWidget(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun DigSetSearchingTarget(context: LuaContextWrapper, configId: Int): Int {
        return context.onGroupContext {
            onDigHandler {
                checkConfigId(::DigSetSearchingTarget, configId)?.let {
                    return@onDigHandler it.getValue()
                }
                digSetSearchingTarget(this@onGroupContext, configId)
            }
        }
    }


    /* EffigyChallengeScriptHandler */

    @JvmStatic
    fun CreateEffigyChallengeMonster(context: LuaContextWrapper, groupId: Int, rawMonsterPoolIdTable: Any): Int {
        return context.onGroupContext {
            onEffigyHandler {
                val monsterPoolIdList = context.engine.getTable(rawMonsterPoolIdTable).getAsIntArray().toList()
                createEffigyChallengeMonster(this@onGroupContext, groupId, monsterPoolIdList)
            }
        }
    }

    @JvmStatic
    fun GetEffigyChallengeMonsterLevel(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onEffigyHandler {
                getEffigyChallengeMonsterLevel(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetEffigyChallengeLimitTime(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onEffigyHandler {
                getEffigyChallengeLimitTime(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetEffigyChallengeV2DungeonDifficulty(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onEffigyHandler {
                getEffigyChallengeV2DungeonDifficulty(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun IsEffigyChallengeConditionSelected(context: LuaContextWrapper, conditionId: Int): Boolean {
        return context.onGroupContext {
            onEffigyHandler {
                isEffigyChallengeConditionSelected(this@onGroupContext, conditionId)
            }
        }
    }


    /* ExpeditionScriptHandler */
    @JvmStatic
    fun FinishExpeditionChallenge(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onExpeditionHandler {
                finishExpeditionChallenge(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun ExpeditionChallengeEnterRegion(context: LuaContextWrapper, isFinished: Boolean): Int {
        return context.onGroupContext {
            onExpeditionHandler {
                expeditionChallengeEnterRegion(this@onGroupContext, isFinished)
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
            onFleurFairHandler {
                checkGroupId(::AddFleurFairMultistagePlayBuffEnergy, groupId)?.let {
                    return@onFleurFairHandler it.getValue()
                }
                checkUid(::AddFleurFairMultistagePlayBuffEnergy, uid)?.let {
                    return@onFleurFairHandler it.getValue()
                }
                addFleurFairMultistagePlayBuffEnergy(this@onGroupContext, groupId, param2, uid, bonusId)
            }
        }
    }

    @JvmStatic
    fun FinishFleurFairGalleryStageByUid(context: LuaContextWrapper, groupId: Int, var2: Int, uid: Int, var4: Boolean): Int {
        return context.onGroupContext {
            onFleurFairHandler {
                checkGroupId(::FinishFleurFairGalleryStageByUid, groupId)?.let {
                    return@onFleurFairHandler it.getValue()
                }
                checkUid(::FinishFleurFairGalleryStageByUid, uid)?.let {
                    return@onFleurFairHandler it.getValue()
                }
                finishFleurFairGalleryStageByUid(this@onGroupContext, groupId, var2, uid, var4)
            }
        }
    }

    @JvmStatic
    fun GetFleurFairDungeonSectionId(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onFleurFairHandler {
                getFleurFairDungeonSectionId(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetFleurFairMultistagePlayBuffEnergy(context: LuaContextWrapper, groupId: Int, playIndex: Int, uid:Int): Int {
        return context.onGroupContext {
            onFleurFairHandler {
                checkGroupId(::GetFleurFairMultistagePlayBuffEnergy, groupId)?.let {
                    return@onFleurFairHandler it.getValue()
                }
                checkUid(::GetFleurFairMultistagePlayBuffEnergy, uid)?.let {
                    return@onFleurFairHandler it.getValue()
                }
                getFleurFairMultistagePlayBuffEnergy(this@onGroupContext, groupId, playIndex, uid)
            }
        }
    }

    @JvmStatic
    fun SetFleurFairMultistagePlayBuffEnergy(context: LuaContextWrapper, groupId: Int, playIndex: Int, uid:Int, energyValue:Int): Int {
        return context.onGroupContext {
            onFleurFairHandler {
                checkGroupId(::SetFleurFairMultistagePlayBuffEnergy, groupId)?.let {
                    return@onFleurFairHandler it.getValue()
                }
                checkUid(::GetFleurFairMultistagePlayBuffEnergy, uid)?.let {
                    return@onFleurFairHandler it.getValue()
                }
                setFleurFairMultistagePlayBuffEnergy(this@onGroupContext, groupId, playIndex, uid, energyValue)
            }
        }
    }

    @JvmStatic
    fun GetFleurFairMultistagePlayGalleryIdVec(context: LuaContextWrapper, groupId: Int, var2: Int): IntArray {
        return context.onGroupContext {
            onFleurFairHandler {
                checkGroupId(::GetFleurFairMultistagePlayGalleryIdVec, groupId)?.let {
                    return@onFleurFairHandler intArrayOf(it.getValue())
                }
                getFleurFairMultistagePlayGalleryIdVec(this@onGroupContext, groupId, var2).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetFleurFairMultistagePlayGalleryTempValue(context: LuaContextWrapper, groupId: Int, var2: Int, tmpValueKey: String): Int {
        return context.onGroupContext {
            onFleurFairHandler {
                checkGroupId(::GetFleurFairMultistagePlayGalleryTempValue, groupId)?.let {
                    return@onFleurFairHandler it.getValue()
                }
                getFleurFairMultistagePlayGalleryTempValue(this@onGroupContext, groupId, var2, tmpValueKey)
            }
        }
    }


    /* FungusFighterScriptHandler */

    @JvmStatic
    fun SetCurFungusFighterTrainingParams(context: LuaContextWrapper, rawTable: Any): Int {
        return context.onGroupContext {
            onFungusFighterHandler {
                val paramsTable = context.engine.getTable(rawTable)
                val randIndex = paramsTable.optInt("rand_index", -1)
                val monsterPoolList = paramsTable.getTable("monster_pool_list")?.getAsIntArray()?.toList()

                if(randIndex == -1 || monsterPoolList == null) {
                    scriptLogger.error { "SetCurFungusFighterTrainingParams: Invalid parameters" }
                    return@onFungusFighterHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }

                setCurFungusFighterTrainingParams(this@onGroupContext, FungusFighterTrainingParams(randIndex, monsterPoolList))
            }
        }
    }

    @JvmStatic
    fun GetCurFungusFighterPlotConfigIdList(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onFungusFighterHandler {
                getCurFungusFighterPlotConfigIdList(this@onGroupContext).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetCurFungusFighterTrainingParams(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onFungusFighterHandler {
                getCurFungusFighterTrainingParams(this@onGroupContext).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetCurFungusFighterTrainingValidBackupFungusIdList(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onFungusFighterHandler {
                getCurFungusFighterTrainingValidBackupFungusIdList(this@onGroupContext).toIntArray()
            }
        }
    }

    @JvmStatic
    fun IsFungusCaptured(context: LuaContextWrapper, uid:Int, fungusMonsterId:Int): Int {
        return context.onGroupContext {
            onFungusFighterHandler {
                checkUid(::IsFungusCaptured, uid)?.let {
                    return@onFungusFighterHandler it.getValue()
                }
                isFungusCaptured(this@onGroupContext, uid, fungusMonsterId)
            }
        }
    }


    /* GravenInnocenceScriptHandler */

    @JvmStatic
    fun InvalidGravenPhotoBundleMark(context: LuaContextWrapper, groupBundleId: Int): Int {
        return context.onGroupContext {
            onGravenInnocenceHandler {
                invalidGravenPhotoBundleMark(this@onGroupContext, groupBundleId)
            }
        }
    }

    // Compat for scripts calling it with a typo
    @Suppress("SpellCheckingInspection")
    @JvmStatic
    fun InvaildGravenPhotoBundleMark(context: LuaContextWrapper, groupBundleId: Int): Int {
        return InvaildGravenPhotoBundleMark(context, groupBundleId)
    }


    /* HideAndSeekScriptHandler*/

    @JvmStatic
    fun GetHideAndSeekPlayIndex(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onHideAndSeekHandler {
                getHideAndSeekPlayIndex(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekHunter(context: LuaContextWrapper, index: Int): Int {
        return context.onGroupContext {
            onHideAndSeekHandler {
                getHideAndSeekHunter(this@onGroupContext, index)
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekPreyUidList(context: LuaContextWrapper, index: Int): IntArray {
        return context.onGroupContext {
            onHideAndSeekHandler {
                getHideAndSeekPreyUidList(this@onGroupContext, index).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekMap(context: LuaContextWrapper, index: Int): Int {
        return context.onGroupContext {
            onHideAndSeekHandler {
                getHideAndSeekMap(this@onGroupContext, index)
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekPlayGalleryId(context: LuaContextWrapper, index: Int): Int {
        return context.onGroupContext {
            onHideAndSeekHandler {
                getHideAndSeekPlayGalleryId(this@onGroupContext, index)
            }
        }
    }

    @JvmStatic
    fun GetHideAndSeekPlayerSkillList(context: LuaContextWrapper, index: Int, uid: Int): IntArray {
        return context.onGroupContext {
            onHideAndSeekHandler {
                checkUid(::GetHideAndSeekPlayerSkillList, uid)?.let {
                    return@onHideAndSeekHandler intArrayOf(it.getValue())
                }
                getHideAndSeekPlayerSkillList(this@onGroupContext, index, uid).toIntArray()
            }
        }
    }


    /* InstableSprayScriptHandler */

    @JvmStatic
    fun InstableSprayGetSGVByBuffId(context: LuaContextWrapper, buffId: Int): String {
        return context.onGroupContext {
            onInstableSprayHandler {
                instableSprayGetSGVByBuffId(this@onGroupContext, buffId)
            }
        }
    }

    @JvmStatic
    fun InstableSprayRandomBuffs(context: LuaContextWrapper, galleryId: Int, stage: Int): IntArray {
        return context.onGroupContext {
            onInstableSprayHandler {
                instableSprayRandomBuffs(this@onGroupContext, galleryId, stage).toIntArray()
            }
        }
    }



    /* IrodoriChessScriptHandler */

    @JvmStatic
    fun AddIrodoriChessBuildingPoints(context: LuaContextWrapper, groupId: Int, playIndex: Int, points: Int): Int {
        return context.onGroupContext {
            onIrodoriChessHandler {
                checkGroupId(::AddIrodoriChessBuildingPoints, groupId)?.let {
                    return@onIrodoriChessHandler it.getValue()
                }
                addIrodoriChessBuildingPoints(this@onGroupContext, groupId, playIndex, points)
            }
        }
    }

    @JvmStatic
    fun AddIrodoriChessTowerServerGlobalValue(
        context: LuaContextWrapper,
        groupId: Int,
        playIndex: Int,
        gadgetId: Int,
        rawSgvDeltaTable: Any
    ): Int {
        return context.onGroupContext {
            onIrodoriChessHandler {
                checkGroupId(::AddIrodoriChessTowerServerGlobalValue, groupId)?.let {
                    return@onIrodoriChessHandler it.getValue()
                }
                val sgvDeltaTable = context.engine.getTable(rawSgvDeltaTable)
                val keys = sgvDeltaTable.getKeys()
                val sgvDeltaMap = keys.associateWith { sgvDeltaTable.getInt(it) }
                addIrodoriChessTowerServerGlobalValue(this@onGroupContext, groupId, playIndex, gadgetId, sgvDeltaMap)
            }
        }
    }

    @JvmStatic
    fun GetIrodoriChessSelectedCards(
        context: LuaContextWrapper,
        groupId: Int,
        playIndex: Int
    ): Int {
        return context.onGroupContext {
            onIrodoriChessHandler {
                checkGroupId(::GetIrodoriChessSelectedCards, groupId)?.let {
                    return@onIrodoriChessHandler it.getValue()
                }
                getIrodoriChessSelectedCards(this@onGroupContext, groupId, playIndex)
            }
        }
    }

    @JvmStatic
    fun DestroyIrodoriChessTower(
        context: LuaContextWrapper,
        entityId: Int,
        groupId: Int,
        playIndex: Int,
    ): Int {
        return context.onGroupContext {
            onIrodoriChessHandler {
                checkGroupId(::DestroyIrodoriChessTower, groupId)?.let {
                    return@onIrodoriChessHandler it.getValue()
                }
                destroyIrodoriChessTower(this@onGroupContext, entityId, groupId, playIndex)
            }
        }
    }

    @JvmStatic
    fun ForceSetIrodoriFoundationTowers(
        context: LuaContextWrapper,
        rawConfigIdGearTable: Any,
        groupId: Int,
        playIndex: Int,
    ): Int {
        return context.onGroupContext {
            onIrodoriChessHandler {
                checkGroupId(::ForceSetIrodoriFoundationTowers, groupId)?.let {
                    return@onIrodoriChessHandler it.getValue()
                }
                val configIdGearTable = context.engine.getTable(rawConfigIdGearTable)
                val keys = configIdGearTable.getKeys().map { it.toInt() }
                val configIdGearMap = keys.associateWith { configIdGearTable.getInt(it) }
                forceSetIrodoriFoundationTowers(this@onGroupContext, configIdGearMap, groupId, playIndex)
            }
        }
    }


    /* LanternRiteScriptHandler */

    @JvmStatic
    fun GetLanternRiteValue(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onLanternRiteHandler {
                getLanternRiteValue(this@onGroupContext)
            }
        }
    }
    @JvmStatic
    fun SetLanternRiteValue(context: LuaContextWrapper, value: Int): Int {
        return context.onGroupContext {
            onLanternRiteHandler {
                setLanternRiteValue(this@onGroupContext, value)
            }
        }
    }


    /* LanternRiteScriptHandler */

    @JvmStatic
    fun TryFinishLuminanceStoneChallengeStage(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onLuminanceStoneChallengeHandler {
                checkGroupId(::TryFinishLuminanceStoneChallengeStage, groupId)?.let {
                    return@onLuminanceStoneChallengeHandler it.getValue()
                }
                tryFinishLuminanceStoneChallengeStage(this@onGroupContext, groupId)
            }
        }
    }


    /* LunaRiteScriptHandler */

    @JvmStatic
    fun GetLunaRiteSacrificeNum(context: LuaContextWrapper, areaId: Int): Int {
        return context.onGroupContext {
            onLunaRiteHandler {
                getLunaRiteSacrificeNum(this@onGroupContext, areaId)
            }
        }
    }


    /* MechanicusScriptHandler */

    @JvmStatic
    fun AddMechanicusBuildingPoints(
        context: LuaContextWrapper,
        groupId: Int,
        playIndex: Int,
        uid: Int,
        delta: Int
    ): Int {
        return context.onGroupContext {
            onMechanicusHandler {
                checkGroupId(::AddMechanicusBuildingPoints, groupId)?.let {
                    return@onMechanicusHandler it.getValue()
                }
                checkUid(::AddMechanicusBuildingPoints, uid)?.let {
                    return@onMechanicusHandler it.getValue()
                }
                addMechanicusBuildingPoints(this@onGroupContext, groupId, playIndex, uid, delta)
            }
        }
    }

    @JvmStatic
    fun GetMechanicusBuildingPoints(
        context: LuaContextWrapper,
        groupId: Int,
        playIndex: Int,
        uid: Int,
    ): Int {
        return context.onGroupContext {
            onMechanicusHandler {
                checkGroupId(::GetMechanicusBuildingPoints, groupId)?.let {
                    return@onMechanicusHandler it.getValue()
                }
                checkUid(::GetMechanicusBuildingPoints, uid)?.let {
                    return@onMechanicusHandler it.getValue()
                }
                getMechanicusBuildingPoints(this@onGroupContext, groupId, playIndex, uid)
            }
        }
    }

    @JvmStatic
    fun SetMechanicusChallengeState(
        context: LuaContextWrapper,
        groupId: Int,
        playIndex: Int,
        cardId: Int,
        effectId: Int,
        stateIndex: Int,
    ): Int {
        return context.onGroupContext {
            onMechanicusHandler {
                checkGroupId(::SetMechanicusChallengeState, groupId)?.let {
                    return@onMechanicusHandler it.getValue()
                }
                checkEnumIndex<MechanicusChallengeState>(::SetMechanicusChallengeState, stateIndex)?.let {
                    return@onMechanicusHandler it.getValue()
                }
                val state = MechanicusChallengeState.entries[stateIndex]
                setMechanicusChallengeState(this@onGroupContext, groupId, playIndex, cardId, effectId, state)
            }
        }
    }

    @JvmStatic
    fun GetMechanicusMonsterPoolVec(
        context: LuaContextWrapper,
        groupId: Int,
        playIndex: Int
    ): IntArray {
        return context.onGroupContext {
            onMechanicusHandler {
                checkGroupId(::GetMechanicusMonsterPoolVec, groupId)?.let {
                    return@onMechanicusHandler intArrayOf(it.getValue())
                }
                getMechanicusMonsterPoolVec(this@onGroupContext, groupId, playIndex).toIntArray()
            }
        }
    }

    @JvmStatic
    fun SetMechanicusMonsterPoolVec(
        context: LuaContextWrapper,
        groupId: Int,
        playIndex: Int,
        rawMonsterPoolTable: Any
    ): Int {
        return context.onGroupContext {
            onMechanicusHandler {
                checkGroupId(::SetMechanicusMonsterPoolVec, groupId)?.let {
                    return@onMechanicusHandler it.getValue()
                }
                val monsterPoolList = context.engine.getTable(rawMonsterPoolTable).getAsIntArray().toList()
                setMechanicusMonsterPoolVec(this@onGroupContext, groupId, playIndex, monsterPoolList)
            }
        }
    }


    /* MichiaeMatsuriScriptHandler */

    @JvmStatic
    fun SetDarkPressureLevel(context: LuaContextWrapper, darkLevel: Int): Int {
        return context.onGroupContext {
            onMichiaeMatsuriHandler {
                setDarkPressureLevel(this@onGroupContext, darkLevel)
            }
        }
    }


    /* MistTrialScriptHandler */

    @JvmStatic
    fun FailMistTrialDungeonChallenge(context: LuaContextWrapper, challengeIndex: Int): Int {
        return context.onGroupContext {
            onMistTrialHandler {
                failMistTrialDungeonChallenge(this@onGroupContext, challengeIndex)
            }
        }
    }

    @JvmStatic
    fun SetMistTrialServerGlobalValue(context: LuaContextWrapper, floorLevel: Int): Int {
        return context.onGroupContext {
            onMistTrialHandler {
                setMistTrialServerGlobalValue(this@onGroupContext, floorLevel)
            }
        }
    }


    /* MoonfinScriptHandler */

    @JvmStatic
    fun StopFishing(context: LuaContextWrapper, uid: Int): Int {
        return context.onGroupContext {
            onMoonfinHandler {
                checkUid(::StopFishing, uid)?.let {
                    return@onMoonfinHandler it.getValue()
                }
                stopFishing(this@onGroupContext, uid)
            }
        }
    }


    /* PotionScriptHandler */

    @JvmStatic
    fun GetPotionDungeonAffixParams(context: LuaContextWrapper): IntArray? {
        return context.onGroupContext {
            onPotionHandler {
                getPotionDungeonAffixParams(this@onGroupContext)?.toIntArray()
            }
        }
    }


    /* RogueDiaryScriptHandler */

    @JvmStatic
    fun GetRogueDiaryDungeonStage(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onRogueDiaryHandler {
                getRogueDiaryDungeonStage(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetRogueDiaryRoundAndRoom(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onRogueDiaryHandler {
                getRogueDiaryRoundAndRoom(this@onGroupContext).toIntArray()
            }
        }
    }

    @JvmStatic
    fun FinishRogueDiaryDungeonSingleRoom(context: LuaContextWrapper, isFailed: Boolean): Int {
        return context.onGroupContext {
            onRogueDiaryHandler {
                finishRogueDiaryDungeonSingleRoom(this@onGroupContext, isFailed)
            }
        }
    }


    /* RoguelikeScriptHandler */

    @JvmStatic
    fun TriggerRoguelikeCurseByLua(context: LuaContextWrapper, uid: Int): Int {
        return context.onGroupContext {
            onRoguelikeHandler {
                checkUid(::TriggerRoguelikeCurseByLua, uid)?.let {
                    return@onRoguelikeHandler it.getValue()
                }
                triggerRoguelikeCurseByLua(this@onGroupContext, uid)
            }
        }
    }

    @JvmStatic
    fun DoRoguelikeCardGachaByLua(context: LuaContextWrapper, uid: Int): Int {
        return context.onGroupContext {
            onRoguelikeHandler {
                checkUid(::DoRoguelikeCardGachaByLua, uid)?.let {
                    return@onRoguelikeHandler it.getValue()
                }
                doRoguelikeCardGachaByLua(this@onGroupContext, uid)
            }
        }
    }

    @JvmStatic
    fun DisableRoguelikeTrapBySgv(context: LuaContextWrapper, sgvName: String, uid: Int): Int {
        return context.onGroupContext {
            onRoguelikeHandler {
                checkUid(::DisableRoguelikeTrapBySgv, uid)?.let {
                    return@onRoguelikeHandler it.getValue()
                }
                disableRoguelikeTrapBySgv(this@onGroupContext, sgvName, uid)
            }
        }
    }

    @JvmStatic
    fun SetRogueCellState(context: LuaContextWrapper, groupId: Int, state: Int): Int {
        return context.onGroupContext {
            onRoguelikeHandler {
                checkGroupId(::SetRogueCellState, groupId)?.let {
                    return@onRoguelikeHandler it.getValue()
                }
                setRogueCellState(this@onGroupContext, groupId, state)
            }
        }
    }

    @JvmStatic
    fun IsRogueBossCellPrevCellFinish(context: LuaContextWrapper): Boolean {
        return context.onGroupContext {
            onRoguelikeHandler {
                isRogueBossCellPrevCellFinish(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetRogueCellState(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onRoguelikeHandler {
                checkGroupId(::GetRogueCellState, groupId)?.let {
                    return@onRoguelikeHandler it.getValue()
                }
                getRogueCellState(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun EnterRogueCell(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onRoguelikeHandler {
                checkGroupId(::EnterRogueCell, groupId)?.let {
                    return@onRoguelikeHandler it.getValue()
                }
                enterRogueCell(this@onGroupContext, groupId)
            }
        }
    }

    @JvmStatic
    fun EnterRogueDungeonNextLevel(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onRoguelikeHandler {
                enterRogueDungeonNextLevel(this@onGroupContext)
            }
        }
    }


    /* SeaLampScriptHandler */

    @JvmStatic
    fun GetSeaLampActivityPhase(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onSeaLampHandler {
                getSeaLampActivityPhase(this@onGroupContext)
            }
        }
    }


    /* SummerTimeScriptHandler */

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
            onSummerTimeHandler {
                checkGroupIdAndConfigId(::UnlockFloatSignal, groupId, signalGadgetCfgId)?.let {
                    return@onSummerTimeHandler it.getValue()
                }
                unlockFloatSignal(this@onGroupContext, groupId, signalGadgetCfgId)
            }
        }
    }


    /*TreasureMapScriptHandler*/

    @JvmStatic
    fun CreateTreasureMapSpotRewardGadget(context: LuaContextWrapper, gadgetCfgId: Int): Int {
        return context.onGroupContext {
            onTreasureMapHandler {
                checkConfigId(::CreateTreasureMapSpotRewardGadget, gadgetCfgId)?.let {
                    return@onTreasureMapHandler it.getValue()
                }
                createTreasureMapSpotRewardGadget(this@onGroupContext, gadgetCfgId)
            }
        }
    }

    @JvmStatic
    fun GetBonusTreasureMapSolution(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onTreasureMapHandler {
                checkGroupId(::GetBonusTreasureMapSolution, groupId)?.let {
                    return@onTreasureMapHandler it.getValue()
                }
                getBonusTreasureMapSolution(this@onGroupContext, groupId)
            }
        }
    }


    /* TreasureSeelieScriptHandler */

    @JvmStatic
    fun TreasureSeelieCollectOrbsNotify(context: LuaContextWrapper, current: Int, total: Int): Int {
        return context.onGroupContext {
            onTreasureSeelieHandler {
                treasureSeelieCollectOrbsNotify(this@onGroupContext, current, total)
            }
        }
    }

    @JvmStatic
    fun GetTreasureSeelieDayByGroupId(context: LuaContextWrapper, groupId: Int): Int {
        return context.onGroupContext {
            onTreasureSeelieHandler {
                checkGroupId(::GetTreasureSeelieDayByGroupId, groupId)?.let {
                    return@onTreasureSeelieHandler it.getValue()
                }
                getTreasureSeelieDayByGroupId(this@onGroupContext, groupId)
            }
        }
    }


    /* UgcDungeonScriptHandler */

    @JvmStatic
    fun EnterCustomDungeonOfficialEdit(context: LuaContextWrapper, roomId: Int): Int {
        return context.onGroupContext {
            onUgcDungeonHandler {
                enterCustomDungeonOfficialEdit(this@onGroupContext, roomId)
            }
        }
    }

    @JvmStatic
    fun GetCurrentCustomDungeonForbidSkill(context: LuaContextWrapper): Boolean {
        return context.onGroupContext {
            onUgcDungeonHandler {
                getCurrentCustomDungeonForbidSkill(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetCurrentCustomDungeonParamVec(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onUgcDungeonHandler {
                getCurrentCustomDungeonParamVec(this@onGroupContext).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetCustomDungeonCoinNum(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onUgcDungeonHandler {
                getCustomDungeonCoinNum(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun GetCustomDungeonOpenRoomVec(context: LuaContextWrapper): IntArray {
        return context.onGroupContext {
            onUgcDungeonHandler {
                getCustomDungeonOpenRoomVec(this@onGroupContext).toIntArray()
            }
        }
    }


    /* VintageScriptHandler */

    @JvmStatic
    fun VintageFinishGroupByPresentId(context: LuaContextWrapper, presentId: Int): Int {
        return context.onGroupContext {
            onVintageHandler {
                vintageFinishGroupByPresentId(this@onGroupContext, presentId)
            }
        }
    }

    /* WaterSpiritChallengeScriptHandler*/

    @JvmStatic
    fun AddRegionRecycleProgress(context: LuaContextWrapper, regionId: Int, delta: Int): Int {
        return context.onGroupContext {
            onWaterSpiritChallengeHandler {
                addRegionRecycleProgress(this@onGroupContext, regionId, delta)
            }
        }
    }

    @JvmStatic
    fun AddRegionSearchProgress(context: LuaContextWrapper, regionId: Int, delta: Int): Int {
        return context.onGroupContext {
            onWaterSpiritChallengeHandler {
                addRegionSearchProgress(this@onGroupContext, regionId, delta)
            }
        }
    }


    /* WinterCampScriptHandler */

    @JvmStatic
    fun WinterCampGetBattleGroupBundleId(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onWinterCampHandler {
                winterCampGetBattleGroupBundleId(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun WinterCampGetExploreGroupBundleId(context: LuaContextWrapper): Int {
        return context.onGroupContext {
            onWinterCampHandler {
                winterCampGetExploreGroupBundleId(this@onGroupContext)
            }
        }
    }

    @JvmStatic
    fun WinterCampSnowDriftInteract(context: LuaContextWrapper, cfgId: Int): Int {
        return context.onGroupContext {
            onWinterCampHandler {
                checkConfigId(::WinterCampSnowDriftInteract, cfgId)?.let {
                    return@onWinterCampHandler it.getValue()
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
    fun GetGadgetArguments(context:LuaContextWrapper): IntArray {
        // TODO check because of IntArray return type
        return context.onControllerContext {
            onGadgetControllerHandler {
                getGadgetArguments(this@onControllerContext).toIntArray()
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
                val subFieldName = params.getString("subfield_name") ?: run {
                    scriptLogger.error { "DropSubfield: subfield_name is missing in params" }
                    return@onGadgetControllerHandler ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue()
                }
                dropSubfield(this@onControllerContext, subFieldName)
            }
        }
    }

    @JvmStatic
    fun GetGatherConfigIdList(context: LuaContextWrapper): IntArray {
        return context.onControllerContext {
            onGadgetControllerHandler {
                getGatherConfigIdList(this@onControllerContext).toIntArray()
            }
        }
    }

    @JvmStatic
    fun GetContextGadgetEntityId(context: LuaContextWrapper): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                getContextGadgetEntityId(this@onControllerContext)
            }
        }
    }

    @JvmStatic
    fun GadgetLuaNotifyGroup(context: LuaContextWrapper, var1: Int, var2: Int, var3: Int): Int {
        return context.onControllerContext {
            onGadgetControllerHandler {
                gadgetLuaNotifyGroup(this@onControllerContext, var1, var2, var3)
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
        if (configId <= 0) {
            scriptLogger.error { "[$caller] Invalid configId ($configId)" }
            return if(isTableParam) ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT else ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }

    private fun checkConfigIdOptional(caller: KCallable<*>, configId: Int, isTableParam: Boolean = false): ScriptLibErrors? {
        if (configId < 0) {
            scriptLogger.error { "[$caller] Invalid optional configId ($configId)" }
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

    private inline fun <reified E : Enum<E>> checkEnumIndex(caller: KCallable<*>, index: Int): ScriptLibErrors? {
        if (index < 0 || index >= enumValues<E>().size) {
            scriptLogger.error { "[$caller] Enum index $index out of bounds for enum ${E::class.simpleName}" }
            return ScriptLibErrors.INVALID_PARAMETER
        }
        return null
    }
}
