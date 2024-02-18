package org.anime_game_servers.gi_lua.script_lib;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.val;
import org.anime_game_servers.core.base.annotations.lua.LuaStatic;
import org.anime_game_servers.gi_lua.models.constants.*;
import org.anime_game_servers.gi_lua.models.constants.ExhibitionPlayType;
import org.anime_game_servers.gi_lua.models.constants.FlowSuiteOperatePolicy;
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreType;
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreUIType;
import org.anime_game_servers.gi_lua.script_lib.handler.ScriptLibStaticHandler;
import org.anime_game_servers.gi_lua.script_lib.handler.parameter.KillByConfigIdParams;
import org.anime_game_servers.lua.engine.LuaTable;

import javax.annotation.Nullable;
import java.util.Arrays;

import static org.anime_game_servers.gi_lua.utils.ScriptUtils.luaToPos;
import static org.anime_game_servers.gi_lua.utils.ScriptUtils.posToLua;
import static org.anime_game_servers.gi_lua.script_lib.ScriptLibErrors.*;

@LuaStatic
public class ScriptLib {

    /**
     * Context free functions
     */

    private static KLogger scriptLogger = KotlinLogging.INSTANCE.logger(ScriptLib.class.getName());
    public static ScriptLibStaticHandler staticHandler;
    public static void PrintLog(String msg) {
        staticHandler.PrintLog(msg);
    }

    public static int GetEntityType(int entityId){
        return staticHandler.GetEntityType(entityId);
    }


    /**
     * Context independent functions
     */
    public static void PrintContextLog(LuaContext context, String msg) {
        staticHandler.PrintContextLog(context, msg);
    }

    /**
     * GroupEventLuaContext functions
     */
    public static void PrintGroupWarning(GroupEventLuaContext context, String msg) {
        context.getScriptLibHandler().PrintGroupWarning(context, msg);
    }

	public static int SetWorktopOptionsByGroupId(GroupEventLuaContext context, int groupId, int configId, Object optionsTable) {
        val options = context.getEngine().getTable(optionsTable);
        return context.getScriptLibHandler().SetWorktopOptionsByGroupId(context, groupId, configId, options);
	}

	public static int SetWorktopOptions(GroupEventLuaContext context, Object rawTable){
        val table = context.getEngine().getTable(rawTable);
        return context.getScriptLibHandler().SetWorktopOptions(context, table);
	}

	public static int DelWorktopOptionByGroupId(GroupEventLuaContext context, int groupId, int configId, int option) {
        return context.getScriptLibHandler().DelWorktopOptionByGroupId(context, groupId, configId, option);
	}
    public static int DelWorktopOption(GroupEventLuaContext context, int var1){
        return context.getScriptLibHandler().DelWorktopOption(context, var1);
    }

	// Some fields are guessed
	public static int AutoMonsterTide(GroupEventLuaContext context, int challengeIndex, int groupId, Integer[] ordersConfigId, int tideCount, int sceneLimit, int param6) {
        return context.getScriptLibHandler().AutoMonsterTide(context, challengeIndex, groupId, ordersConfigId, tideCount, sceneLimit, param6);
	}

    public static int GoToGroupSuite(GroupEventLuaContext context, int groupId, int suite) {
        return context.getScriptLibHandler().GoToGroupSuite(context, groupId, suite);
    }

	public static int AddExtraGroupSuite(GroupEventLuaContext context, int groupId, int suite) {
        return context.getScriptLibHandler().AddExtraGroupSuite(context, groupId, suite);
	}
	public static int RemoveExtraGroupSuite(GroupEventLuaContext context, int groupId, int suite) {
        return context.getScriptLibHandler().RemoveExtraGroupSuite(context, groupId, suite);
	}
	public static int KillExtraGroupSuite(GroupEventLuaContext context, int groupId, int suite) {
        return context.getScriptLibHandler().KillExtraGroupSuite(context, groupId, suite);
	}

    public static int AddExtraFlowSuite(GroupEventLuaContext context, int groupId, int suiteId, int flowSuitePolicy){
        if(flowSuitePolicy < 0 || flowSuitePolicy >= FlowSuiteOperatePolicy.values().length){
            scriptLogger.error(() -> "[AddExtraFlowSuite] Invalid flow suite policy " + flowSuitePolicy);
            return 1;
        }
        val flowSuitePolicyEnum = FlowSuiteOperatePolicy.values()[flowSuitePolicy];
        return context.getScriptLibHandler().AddExtraFlowSuite(context, groupId, suiteId, flowSuitePolicyEnum);
    }
    public static int RemoveExtraFlowSuite(GroupEventLuaContext context, int groupId, int suiteId, int flowSuitePolicy){
        if(flowSuitePolicy < 0 || flowSuitePolicy >= FlowSuiteOperatePolicy.values().length){
            scriptLogger.error(() -> "[RemoveExtraFlowSuite] Invalid flow suite policy " + flowSuitePolicy);
            return 1;
        }
        val flowSuitePolicyEnum = FlowSuiteOperatePolicy.values()[flowSuitePolicy];
        return context.getScriptLibHandler().RemoveExtraFlowSuite(context, groupId, suiteId, flowSuitePolicyEnum);
    }
    public static int KillExtraFlowSuite(GroupEventLuaContext context, int groupId, int suiteId, int flowSuitePolicy){
        if(flowSuitePolicy < 0 || flowSuitePolicy >= FlowSuiteOperatePolicy.values().length){
            scriptLogger.error(() -> "[KillExtraFlowSuite] Invalid flow suite policy " + flowSuitePolicy);
            return 1;
        }
        val flowSuitePolicyEnum = FlowSuiteOperatePolicy.values()[flowSuitePolicy];
        return context.getScriptLibHandler().KillExtraFlowSuite(context, groupId, suiteId, flowSuitePolicyEnum);
    }

	public static int ActiveChallenge(GroupEventLuaContext context, int challengeIndex, int challengeId, int timeLimitOrGroupId, int groupId, int objectiveKills, int param5) {
        return context.getScriptLibHandler().ActiveChallenge(context, challengeIndex, challengeId, timeLimitOrGroupId, groupId, objectiveKills, param5);
	}

    public static int StartChallenge(GroupEventLuaContext context, int challengeIndex, int challengeId, Object challengeParams) {
        val conditionParamTable = context.getEngine().getTable(challengeParams);
        return context.getScriptLibHandler().StartChallenge(context, challengeIndex, challengeId, conditionParamTable);
    }

    public static int StopChallenge(GroupEventLuaContext context, int challengeIndex, int result) {
        return context.getScriptLibHandler().StopChallenge(context, challengeIndex, result);
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
    public static int AddChallengeDuration(GroupEventLuaContext context, int challengeId, int duration) {
        return context.getScriptLibHandler().AddChallengeDuration(context, challengeId, duration);
    }

	public static int GetGroupMonsterCountByGroupId(GroupEventLuaContext context, int groupId) {
        return context.getScriptLibHandler().GetGroupMonsterCountByGroupId(context, groupId);
	}

    // TODO check existence
	public static int CreateVariable(GroupEventLuaContext context, String type, Object value) {
        //TODO implement
        switch (type){
            case "int":
            default:
        }
		return 0;
	}
    // TODO check existence
	public static int SetVariableValue(GroupEventLuaContext context, int var1) {
        //TODO implement var1 type
		return 0;
	}
	public static int GetVariableValue(GroupEventLuaContext context, int var1) {
        //TODO implement var1 type
		return 0;
	}

	public static int GetGroupVariableValue(GroupEventLuaContext context, String var) {
        return context.getScriptLibHandler().GetGroupVariableValue(context, var);
	}

    public static int GetGroupVariableValueByGroup(GroupEventLuaContext context, String name, int groupId){
        return context.getScriptLibHandler().GetGroupVariableValueByGroup(context, name, groupId);
    }

	public static int SetGroupVariableValue(GroupEventLuaContext context, String varName, int value) {
        return context.getScriptLibHandler().SetGroupVariableValue(context, varName, value);
    }

    public static int SetGroupVariableValueByGroup(GroupEventLuaContext context, String key, int value, int groupId){
        return context.getScriptLibHandler().SetGroupVariableValueByGroup(context, key, value, groupId);
    }

	public static int ChangeGroupVariableValue(GroupEventLuaContext context, String varName, int value) {
		return context.getScriptLibHandler().ChangeGroupVariableValue(context, varName, value);
	}

    public static int ChangeGroupVariableValueByGroup(GroupEventLuaContext context, String name, int value, int groupId){
        return context.getScriptLibHandler().ChangeGroupVariableValueByGroup(context, name, value, groupId);
    }


	/**
	 * Set the actions and triggers to designated group
	 */
	public static int RefreshGroup(GroupEventLuaContext context, Object rawTable) {
        val table = context.getEngine().getTable(rawTable);
		return context.getScriptLibHandler().RefreshGroup(context, table);
	}

	public static int GetRegionEntityCount(GroupEventLuaContext context, Object rawTable) {
        val table = context.getEngine().getTable(rawTable);


        int regionId = table.getInt("region_eid");
        int entityType = table.getInt("entity_type");
        if(entityType < 0 || entityType >= EntityType.values().length){
            scriptLogger.error(() -> "[GetRegionEntityCount] Invalid entity type " + entityType);
            return 0;
        }
        val entityTypeEnum = EntityType.values()[entityType];

		return context.getScriptLibHandler().GetRegionEntityCount(context, regionId, entityTypeEnum);
	}

    public static int GetRegionConfigId(GroupEventLuaContext context, Object rawTable){
        val table = context.getEngine().getTable(rawTable);
        val regionEid = table.getInt("region_eid");
        return context.getScriptLibHandler().GetRegionConfigId(context, regionEid);
    }

	public static int TowerCountTimeStatus(GroupEventLuaContext context, int isDone, int var2){
		return context.getScriptLibHandler().TowerCountTimeStatus(context, isDone, var2);
	}
	public static int GetGroupMonsterCount(GroupEventLuaContext context){
		return context.getScriptLibHandler().GetGroupMonsterCount(context);
	}

	public static int SetMonsterBattleByGroup(GroupEventLuaContext context, int configId, int groupId) {
		return context.getScriptLibHandler().SetMonsterBattleByGroup(context, configId, groupId);
	}

	public static int CauseDungeonFail(GroupEventLuaContext context){
		return context.getScriptLibHandler().CauseDungeonFail(context);
	}

	public static int CauseDungeonSuccess(GroupEventLuaContext context){
		return context.getScriptLibHandler().CauseDungeonSuccess(context);
	}

    public static int SetEntityServerGlobalValueByConfigId(GroupEventLuaContext context, int cfgId, String sgvName, int value){
        return context.getScriptLibHandler().SetEntityServerGlobalValueByConfigId(context, cfgId, sgvName, value);
    }

    public static int SetGroupLogicStateValue(GroupEventLuaContext context, String sgvName, int value){
        return context.getScriptLibHandler().SetGroupLogicStateValue(context, sgvName, value);
    }

	public static int SetIsAllowUseSkill(GroupEventLuaContext context, int canUse){
		return context.getScriptLibHandler().SetIsAllowUseSkill(context, canUse);
	}

    public static int KillEntityByConfigId(LuaContext context, Object rawTable) {
        val table = context.getEngine().getTable(rawTable);
        val configId = table.optInt("config_id", 0);
        if (configId == 0){
            scriptLogger.error(() -> "[KillEntityByConfigId] Invalid config id " + configId);
            return INVALID_PARAMETER.getValue();
        }
        val groupId = table.optInt("group_id", 0);
        val entityTypeValue = table.optInt("entity_type", 0);
        if(entityTypeValue < 0 || entityTypeValue >= EntityType.values().length){
            scriptLogger.error(() -> "[KillEntityByConfigId] Invalid entity type " + entityTypeValue);
            return INVALID_PARAMETER.getValue();
        }
        val entityType = EntityType.values()[entityTypeValue];
        val params = new KillByConfigIdParams(configId, groupId, entityType);
        if (context instanceof GroupEventLuaContext gContext){
            return gContext.getScriptLibHandlerProvider().getScriptLibHandler().KillEntityByConfigId(gContext, params);
        } else if (context instanceof ControllerLuaContext cContext) {
            return cContext.getScriptLibHandlerProvider().getGadgetControllerHandler().KillEntityByConfigId(cContext, params);
        }
        scriptLogger.error(() -> "[KillEntityByConfigId] unknown context type " + context.getClass().getName());
        return NOT_IMPLEMENTED.getValue();
    }

	public static int CreateMonster(GroupEventLuaContext context, Object rawTable){
        val table = context.getEngine().getTable(rawTable);
		return context.getScriptLibHandler().CreateMonster(context, table);
	}

	public static int TowerMirrorTeamSetUp(GroupEventLuaContext context, int team, int var1) {
		return context.getScriptLibHandler().TowerMirrorTeamSetUp(context, team, var1);
	}

	public static int CreateGadget(GroupEventLuaContext context, Object rawTable){
        val table = context.getEngine().getTable(rawTable);
        return context.getScriptLibHandler().CreateGadget(context, table);
	}

    /**
     * Spawn a gadget from the caller group at the specified position
     * @param configId The config id of the gadget in the calling group
     * @param posTable The position to spawn the gadget at
     * @param rotTable The rotation of the gadget when spawned
     */
	public static int CreateGadgetByConfigIdByPos(GroupEventLuaContext context, int configId, Object posTable, Object rotTable){
        val luaPos = context.getEngine().getTable(posTable);
        val luaRot = context.getEngine().getTable(rotTable);
        return context.getScriptLibHandler().CreateGadgetByConfigIdByPos(context, configId, luaToPos(luaPos), luaToPos(luaRot));
	}

    /**
     * TODO parse the table before passing it to the handler
     * Spawns a gadget based on the caller groups gadget with cfg id matching the specified id. It also applies additional parameters based on the parameters
     * @param creationParamsTable parameters to spawn a gadget with
     */
	public static int CreateGadgetByParamTable(GroupEventLuaContext context, Object creationParamsTable){
        val table = context.getEngine().getTable(creationParamsTable);
        return context.getScriptLibHandler().CreateGadget(context, table);
	}

    public static int CreateVehicle(GroupEventLuaContext context, int uid, int gadgetId, Object posTable, Object rotTable){
        val luaPos = context.getEngine().getTable(posTable);
        val luaRot = context.getEngine().getTable(rotTable);
        return context.getScriptLibHandler().CreateVehicle(context, uid, gadgetId, luaToPos(luaPos), luaToPos(luaRot));
	}

	public static int CheckRemainGadgetCountByGroupId(GroupEventLuaContext context, Object rawTable) {
        val table = context.getEngine().getTable(rawTable);
		return context.getScriptLibHandler().CheckRemainGadgetCountByGroupId(context, table);
	}

	public static int MarkPlayerAction(GroupEventLuaContext context, int var1, int var2, int var3){
        return context.getScriptLibHandler().MarkPlayerAction(context, var1, var2, var3);
	}

	public static int AddQuestProgress(GroupEventLuaContext context, String eventNotifyName){
        return context.getScriptLibHandler().AddQuestProgress(context, eventNotifyName);
	}



    public static int GetSceneOwnerUid(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetSceneOwnerUid(context);
    }

    public static int GetHostQuestState(GroupEventLuaContext context, int questId){
        return context.getScriptLibHandler().GetHostQuestState(context, questId).getValue();
    }

    public static int GetQuestState(GroupEventLuaContext context, int entityId, int questId){
        return context.getScriptLibHandler().GetQuestState(context, entityId, questId).getValue();
    }

    public static int ShowReminder(GroupEventLuaContext context, int reminderId){
        return context.getScriptLibHandler().ShowReminder(context, reminderId);
    }

    public static int RemoveEntityByConfigId(GroupEventLuaContext context, int groupId, int entityTypeValue, int configId){
        val entityType = EntityType.values()[entityTypeValue];
        return context.getScriptLibHandler().RemoveEntityByConfigId(context, groupId, entityType, configId);
    }

    public static int CreateGroupTimerEvent(GroupEventLuaContext context, int groupID, String source, double time) {
        return context.getScriptLibHandler().CreateGroupTimerEvent(context, groupID, source, time);
    }

    public static int CancelGroupTimerEvent(GroupEventLuaContext context, int groupID, String source) {
        return context.getScriptLibHandler().CancelGroupTimerEvent(context, groupID, source);
    }

    public static int GetGroupSuite(GroupEventLuaContext context, int groupId) {
        return context.getScriptLibHandler().GetGroupSuite(context, groupId);
    }
    public static int SetGroupReplaceable(GroupEventLuaContext context, int groupId, boolean value) {
        return context.getScriptLibHandler().SetGroupReplaceable(context, groupId, value);
    }

    public static int[] GetSceneUidList(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetSceneUidList(context);
    }

    public static int GetSeaLampActivityPhase(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetSeaLampActivityPhase(context);
    }
    public static int GadgetPlayUidOp(GroupEventLuaContext context, int groupId, int gadget_crucible, int var3, int var4, String var5, int var6 ){
        return context.getScriptLibHandler().GadgetPlayUidOp(context, groupId, gadget_crucible, var3, var4, var5, var6);
    }
    public static long GetServerTime(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetServerTime(context);
    }
    public static long GetServerTimeByWeek(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetServerTimeByWeek(context);
    }
    public static int GetCurTriggerCount(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetCurTriggerCount(context);
    }
    public static int GetChannellerSlabLoopDungeonLimitTime(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetChannellerSlabLoopDungeonLimitTime(context);
    }
    public static boolean IsPlayerAllAvatarDie(GroupEventLuaContext context, int sceneUid){
        return context.getScriptLibHandler().IsPlayerAllAvatarDie(context, sceneUid);
    }

    public static int sendShowCommonTipsToClient(GroupEventLuaContext context, String title, String content, int closeTime) {
        return context.getScriptLibHandler().sendShowCommonTipsToClient(context, title, content, closeTime);
    }

    public static int sendCloseCommonTipsToClient(GroupEventLuaContext context){
        return context.getScriptLibHandler().sendCloseCommonTipsToClient(context);
    }

    public static int updateBundleMarkShowStateByGroupId(GroupEventLuaContext context, int groupId, boolean val2){
        return context.getScriptLibHandler().updateBundleMarkShowStateByGroupId(context, groupId, val2);
    }

    public static int CreateFatherChallenge(GroupEventLuaContext context, int challengeIndex, int challengeId, int timeLimit, Object conditionTable){
        val conditionLuaTable = context.getEngine().getTable(conditionTable);
        return context.getScriptLibHandler().CreateFatherChallenge(context, challengeIndex, challengeId, timeLimit, conditionLuaTable);
    }
    public static int StartFatherChallenge(GroupEventLuaContext context, int challengeIndex){
        return context.getScriptLibHandler().StartFatherChallenge(context, challengeIndex);
    }
    public static int ModifyFatherChallengeProperty(GroupEventLuaContext context, int challengeId, int propertyTypeIndex, int value){
        val propertyType = FatherChallengeProperty.values()[propertyTypeIndex];
        return context.getScriptLibHandler().ModifyFatherChallengeProperty(context, challengeId, propertyType, value);
    }

    public static int SetChallengeEventMark(GroupEventLuaContext context, int challengeId, int markType){
        if(markType < 0 || markType >= ChallengeEventMarkType.values().length){
            scriptLogger.error(() -> "[SetChallengeEventMark] Invalid mark type " + markType);
            return 1;
        }
        val markTypeEnum = ChallengeEventMarkType.values()[markType];
        return context.getScriptLibHandler().SetChallengeEventMark(context, challengeId, markTypeEnum);
    }

    public static int AttachChildChallenge(GroupEventLuaContext context, int fatherChallengeIndex, int childChallengeIndex,
                                           int childChallengeId, Object var4Table, Object var5Table, Object var6Table){
        val conditionArray = context.getEngine().getTable(var4Table);
        val var5 = context.getEngine().getTable(var5Table);
        val conditionTable = context.getEngine().getTable(var6Table);
        return context.getScriptLibHandler().AttachChildChallenge(context, fatherChallengeIndex, childChallengeIndex,
            childChallengeId, conditionArray, var5, conditionTable);
    }
    public static int CreateEffigyChallengeMonster(GroupEventLuaContext context, int var1, Object var2Table){
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().CreateEffigyChallengeMonster(context, var1, var2);
    }
    public static int GetEffigyChallengeMonsterLevel(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetEffigyChallengeMonsterLevel(context);
    }
    public static int AddTeamEntityGlobalFloatValue(GroupEventLuaContext context, Object sceneUidListTable, String var2, int var3){
        val sceneUidList = context.getEngine().getTable(sceneUidListTable);
        return context.getScriptLibHandler().AddTeamEntityGlobalFloatValue(context, sceneUidList, var2, var3);
    }
    public static int CreateBlossomChestByGroupId(GroupEventLuaContext context, int groupId, int chestConfigId){
        return context.getScriptLibHandler().CreateBlossomChestByGroupId(context, groupId, chestConfigId);
    }
    public static int GetBlossomScheduleStateByGroupId(GroupEventLuaContext context, int groupId){
        return context.getScriptLibHandler().GetBlossomScheduleStateByGroupId(context, groupId);
    }
    public static int SetBlossomScheduleStateByGroupId(GroupEventLuaContext context, int groupId, int state){
        return context.getScriptLibHandler().SetBlossomScheduleStateByGroupId(context, groupId, state);
    }
    public static int RefreshBlossomGroup(GroupEventLuaContext context, Object rawTable) {
        val configTable = context.getEngine().getTable(rawTable);
        return context.getScriptLibHandler().RefreshBlossomGroup(context, configTable);
    }
    public static int RefreshBlossomDropRewardByGroupId(GroupEventLuaContext context, int groupId){
        return context.getScriptLibHandler().RefreshBlossomDropRewardByGroupId(context, groupId);
    }
    public static int AddBlossomScheduleProgressByGroupId(GroupEventLuaContext context, int groupId){
        return context.getScriptLibHandler().AddBlossomScheduleProgressByGroupId(context, groupId);
    }
    public static int GetBlossomRefreshTypeByGroupId(GroupEventLuaContext context, int groupId){
        return context.getScriptLibHandler().GetBlossomRefreshTypeByGroupId(context, groupId);
    }
    public static int RefreshHuntingClueGroup(GroupEventLuaContext context){
        return context.getScriptLibHandler().RefreshHuntingClueGroup(context);
    }
    public static int GetHuntingMonsterExtraSuiteIndexVec(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetHuntingMonsterExtraSuiteIndexVec(context);
    }
    public static int SetGroupTempValue(GroupEventLuaContext context, String name, int value, Object var3Table) {
        val var3 = context.getEngine().getTable(var3Table);
        return context.getScriptLibHandler().SetGroupTempValue(context, name, value, var3);
    }
    public static int GetGroupTempValue(GroupEventLuaContext context, String name, Object var2Table) {
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().GetGroupTempValue(context, name, var2);
    }

    public static int FinishExpeditionChallenge(GroupEventLuaContext context){
        return context.getScriptLibHandler().FinishExpeditionChallenge(context);
    }
    public static int ExpeditionChallengeEnterRegion(GroupEventLuaContext context, boolean var1){
        return context.getScriptLibHandler().ExpeditionChallengeEnterRegion(context, var1);
    }

    public static int StartSealBattle(GroupEventLuaContext context, int gadgetId, Object battleParamsTable) {
        val battleParams = context.getEngine().getTable(battleParamsTable);
        val battleType = battleParams.optInt("battle_type", -1);
        if(battleType < 0 || battleType >= SealBattleType.values().length){
            scriptLogger.error(() -> "[StartSealBattle] Invalid battle type " + battleType);
            return -1;
        }
        val battleTypeEnum = SealBattleType.values()[battleType];
        val handlerParams = switch (battleTypeEnum){
            case NONE -> parseSealBattleNoneParams(battleParams);
            case KILL_MONSTER -> parseSealBattleMonsterKillParams(battleParams);
            case ENERGY_CHARGE -> parseEnergySealBattleTimeParams(battleParams);
        };
        return context.getScriptLibHandler().StartSealBattle(context, gadgetId, handlerParams);
    }

    private static SealBattleParams parseSealBattleNoneParams(LuaTable battleParams){
        val radius = battleParams.optInt("radius", -1);
        val inAdd = battleParams.optInt("in_add", -1);
        val outSub = battleParams.optInt("out_sub", -1);
        val failTime = battleParams.optInt("fail_time", -1);
        val maxProgress = battleParams.optInt("max_progress", -1);
        // TODO check params and maybe return error?
        return new DefaultSealBattleParams(radius, inAdd, outSub, failTime, maxProgress);
    }

    private static SealBattleParams parseSealBattleMonsterKillParams(LuaTable battleParams){
        val radius = battleParams.optInt("radius", -1);
        val killTime = battleParams.optInt("kill_time", -1);
        val monsterGroupId = battleParams.optInt("monster_group_id", -1);
        val maxProgress = battleParams.optInt("max_progress", -1);
        // TODO check params and maybe return error?
        return new MonsterSealBattleParams(radius, killTime, monsterGroupId, maxProgress);
    }

    private static SealBattleParams parseEnergySealBattleTimeParams(LuaTable battleParams){
        val radius = battleParams.optInt("radius", -1);
        val battleTime = battleParams.optInt("battle_time", -1);
        val monsterGroupId = battleParams.optInt("monster_group_id", -1);
        val defaultKillCharge = battleParams.optInt("default_kill_charge", -1);
        val autoCharge = battleParams.optInt("auto_charge", -1);
        val autoDecline = battleParams.optInt("auto_decline", -1);
        val maxEnergy = battleParams.optInt("max_energy", -1);
        // TODO check params and maybe return error?
        return new EnergySealBattleParams(radius, battleTime, monsterGroupId, defaultKillCharge, autoCharge, autoDecline, maxEnergy);
    }

    public static int InitTimeAxis(GroupEventLuaContext context, String var1, Object var2Table, boolean var3){
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().InitTimeAxis(context, var1, var2, var3);
    }
    public static int EndTimeAxis(GroupEventLuaContext context, String var1){
        return context.getScriptLibHandler().EndTimeAxis(context, var1);
    }

    public static int SetTeamEntityGlobalFloatValue(GroupEventLuaContext context, Object sceneUidListTable, String var2, int var3){
        val sceneUidList = context.getEngine().getTable(sceneUidListTable);
        return context.getScriptLibHandler().SetTeamEntityGlobalFloatValue(context, sceneUidList, var2, var3);
    }

    public static int SetTeamServerGlobalValue(GroupEventLuaContext context, int sceneUid, String var2, int var3){
        return context.getScriptLibHandler().SetTeamServerGlobalValue(context, sceneUid, var2, var3);
    }

    public static int AddTeamServerGlobalValue(GroupEventLuaContext context, int ownerId, String sgvName, int value){
        return context.getScriptLibHandler().AddTeamServerGlobalValue(context, ownerId, sgvName, value);
    }

    public static int GetTeamServerGlobalValue(GroupEventLuaContext context, int ownerId, String sgvName, int value){
        return context.getScriptLibHandler().GetTeamServerGlobalValue(context, ownerId, sgvName, value);
    }

    public static int GetLanternRiteValue(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetLanternRiteValue(context);
    }

    public static int CreateMonsterFaceAvatar(GroupEventLuaContext context, Object rawTable) {
        val table = context.getEngine().getTable(rawTable);
        return context.getScriptLibHandler().CreateMonsterFaceAvatar(context, table);
    }

    public static int ChangeToTargetLevelTag(GroupEventLuaContext context, int var1){
        return context.getScriptLibHandler().ChangeToTargetLevelTag(context, var1);
    }

    public static int AddSceneTag(GroupEventLuaContext context, int sceneId, int sceneTagId){
        return context.getScriptLibHandler().AddSceneTag(context, sceneId, sceneTagId);
    }

    public static int DelSceneTag(GroupEventLuaContext context, int sceneId, int sceneTagId){
        return context.getScriptLibHandler().DelSceneTag(context, sceneId, sceneTagId);
    }

    public static boolean CheckSceneTag(GroupEventLuaContext context, int sceneId, int sceneTagId){
        return context.getScriptLibHandler().CheckSceneTag(context, sceneId, sceneTagId);
    }
    public static int StartHomeGallery(GroupEventLuaContext context, int galleryId, int uid){
        return context.getScriptLibHandler().StartHomeGallery(context, galleryId, uid);
    }

    public static int StartGallery(GroupEventLuaContext context, int galleryId){
        return context.getScriptLibHandler().StartGallery(context, galleryId);
    }

    public static int StopGallery(GroupEventLuaContext context, int galleryId, boolean var2){
        return context.getScriptLibHandler().StopGallery(context, galleryId, var2);
    }

    public static int UpdatePlayerGalleryScore(GroupEventLuaContext context, int galleryId, Object var2Table) {
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().UpdatePlayerGalleryScore(context, galleryId, var2);
    }
    public static int InitGalleryProgressScore(GroupEventLuaContext context, String name, int galleryId, Object progressTable,
                                               int scoreUiTypeIndex, int scoreTypeIndex) {
        val progress = context.getEngine().getTable(progressTable);

        if(scoreUiTypeIndex < 0 || scoreUiTypeIndex >= GalleryProgressScoreUIType.values().length){
            scriptLogger.error(() -> "[InitGalleryProgressScore] Invalid score ui type " + scoreUiTypeIndex);
            return 1;
        }
        val uiScoreType = GalleryProgressScoreUIType.values()[scoreUiTypeIndex];

        if(scoreTypeIndex < 0 || scoreTypeIndex >= GalleryProgressScoreType.values().length){
            scriptLogger.error(() -> "[InitGalleryProgressScore] Invalid score type " + scoreTypeIndex);
            return 2;
        }
        val scoreType = GalleryProgressScoreType.values()[scoreTypeIndex];
        return context.getScriptLibHandler().InitGalleryProgressScore(context, name, galleryId, progress, uiScoreType, scoreType);
    }
    public static int InitGalleryProgressWithScore(GroupEventLuaContext context, String name, int galleryId, Object progressTable,
                                               int maxProgress, int scoreUiTypeIndex, int scoreTypeIndex) {
        val progress = context.getEngine().getTable(progressTable);

        if(scoreUiTypeIndex < 0 || scoreUiTypeIndex >= GalleryProgressScoreUIType.values().length){
            scriptLogger.error(() -> "[InitGalleryProgressWithScore] Invalid score ui type " + scoreUiTypeIndex);
            return 1;
        }
        val uiScoreType = GalleryProgressScoreUIType.values()[scoreUiTypeIndex];

        if(scoreTypeIndex < 0 || scoreTypeIndex >= GalleryProgressScoreType.values().length){
            scriptLogger.error(() -> "[InitGalleryProgressWithScore] Invalid score type " + scoreTypeIndex);
            return 2;
        }
        val scoreType = GalleryProgressScoreType.values()[scoreTypeIndex];

        return context.getScriptLibHandler().InitGalleryProgressWithScore(context, name, galleryId, progress, maxProgress, uiScoreType, scoreType);
    }
    public static int AddGalleryProgressScore(GroupEventLuaContext context, String name, int galleryId, int score) {
        return context.getScriptLibHandler().AddGalleryProgressScore(context, name, galleryId, score);
    }
    public static int GetGalleryProgressScore(GroupEventLuaContext context, String name, int galleryId) {
        return context.getScriptLibHandler().GetGalleryProgressScore(context, name, galleryId);
    }
    public static int SetHandballGalleryBallPosAndRot(GroupEventLuaContext context, int galleryId, Object positionTable, Object rotationTable){
        val position = context.getEngine().getTable(positionTable);
        val rotation = context.getEngine().getTable(rotationTable);
        return context.getScriptLibHandler().SetHandballGalleryBallPosAndRot(context, galleryId, position, rotation);
    }

    /**
     * This signalizes the server that it should unlock the float signal gadget with the specified id in the specified group
     * @param context group context in which this function is called
     * @param groupId group id of the group containing the float signal gadget that should be unlocked
     * @param signalGadgetCfgId the config id identifying the of the float signal gadget that should be unlocked
     * @return 0 on success, otherwise an error code
     */
    public static int UnlockFloatSignal(GroupEventLuaContext context, int groupId, int signalGadgetCfgId){
        val handler = context.getScriptLibHandlerProvider().getSummerTimeScriptHandler();
        if (handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.UnlockFloatSignal(context, groupId, signalGadgetCfgId);
    }

    public static int SendServerMessageByLuaKey(GroupEventLuaContext context, String stringKey, Object targetsTable){
        var targets = context.getEngine().getTable(targetsTable);
        return context.getScriptLibHandler().SendServerMessageByLuaKey(context, stringKey, targets.getAsIntArray());
    }

    public static int TryReallocateEntityAuthority(GroupEventLuaContext context, int uid, int endConfig, int var3){
        return context.getScriptLibHandler().TryReallocateEntityAuthority(context, uid, endConfig, var3);
    }

    public static int ForceRefreshAuthorityByConfigId(GroupEventLuaContext context, int var1, int uid){
        return context.getScriptLibHandler().ForceRefreshAuthorityByConfigId(context, var1, uid);
    }

    public static int AddPlayerGroupVisionType(GroupEventLuaContext context, Object uidsTable, Object var2Table){
        val uids = context.getEngine().getTable(uidsTable);
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().AddPlayerGroupVisionType(context, uids, var2);
    }

    public static int DelPlayerGroupVisionType(GroupEventLuaContext context, Object uidsTable, Object var2Table){
        val uids = context.getEngine().getTable(uidsTable);
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().DelPlayerGroupVisionType(context, uids, var2);
    }

    public static int MoveAvatarByPointArray(GroupEventLuaContext context, int uid, int targetId, Object var3Table, String var4){
        val var3 = context.getEngine().getTable(var3Table);
        return context.getScriptLibHandler().MoveAvatarByPointArray(context, uid, targetId, var3, var4);
    }

    public static int MovePlayerToPos(GroupEventLuaContext context, Object transportationParamsTable) {
        val transportationParams = context.getEngine().getTable(transportationParamsTable);
        val targetsTable = transportationParams.getTable("uid_list");
        val luaPos = transportationParams.getTable("pos");
        val luaRot = transportationParams.getTable("rot");
        val radius = transportationParams.optInt("radius", -1);
        val isSkipUi = transportationParams.optBoolean("is_skip_ui", false);


        if(targetsTable==null || targetsTable.getSize() ==0 || luaPos == null){
            scriptLogger.error(() -> "[TransPlayerToPos] Invalid params, either missing uid_list or pos");
            return 1;
        }

        val pos = luaToPos(luaPos);
        val rot = luaToPos(luaRot);
        val targets = targetsTable.getAsIntArray();

        return context.getScriptLibHandler().MovePlayerToPos(context, targets, pos, rot, radius, isSkipUi);
    }

    public static int TransPlayerToPos(GroupEventLuaContext context, Object transportationParamsTable) {
        val transportationParams = context.getEngine().getTable(transportationParamsTable);
        val targetsTable = transportationParams.getTable("uid_list");
        val luaPos = transportationParams.getTable("pos");
        val luaRot = transportationParams.getTable("rot");
        val radius = transportationParams.optInt("radius", -1);
        val isSkipUi = transportationParams.optBoolean("is_skip_ui", false);


        if(targetsTable==null || targetsTable.getSize() ==0 || luaPos == null){
            scriptLogger.error(() -> "[TransPlayerToPos] Invalid params, either missing uid_list or pos");
            return 1;
        }

        val pos = luaToPos(luaPos);
        val rot = luaToPos(luaRot);
        val targets = targetsTable.getAsIntArray();

        return context.getScriptLibHandler().TransPlayerToPos(context, targets, pos, rot, radius, isSkipUi);
    }

    public static int PlayCutScene(GroupEventLuaContext context, int cutsceneId, int var2){
        return context.getScriptLibHandler().PlayCutScene(context, cutsceneId, var2);
    }

    public static int PlayCutSceneWithParam(GroupEventLuaContext context, int cutsceneId, int var2, Object var3Table){
        val var3 = context.getEngine().getTable(var3Table);
        return context.getScriptLibHandler().PlayCutSceneWithParam(context, cutsceneId, var2, var3);
    }

    public static int ScenePlaySound(GroupEventLuaContext context, Object soundInfoTable) {
        val soundInfo = context.getEngine().getTable(soundInfoTable);
        return context.getScriptLibHandler().ScenePlaySound(context, soundInfo);
    }

    public static int BeginCameraSceneLook(GroupEventLuaContext context, Object sceneLookParamsTable){
        val sceneLookParams = context.getEngine().getTable(sceneLookParamsTable);
        return context.getScriptLibHandler().BeginCameraSceneLook(context, sceneLookParams);
    }

    public static int SetPlayerEyePointStream(GroupEventLuaContext context, int var1, int var2, boolean var3){
        return context.getScriptLibHandler().SetPlayerEyePointStream(context, var1, var2, var3);
    }

    public static int ClearPlayerEyePoint(GroupEventLuaContext context, int var1){
        return context.getScriptLibHandler().ClearPlayerEyePoint(context, var1);
    }

    public static int ShowReminderRadius(GroupEventLuaContext context, int var1, Object var2Table, int var3){
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().ShowReminderRadius(context, var1, var2, var3);
    }
    public static int ShowClientGuide(GroupEventLuaContext context, String guideName){
        return context.getScriptLibHandler().ShowClientGuide(context, guideName);
    }

    /**
     * Activates a dungeon checkpoint.
     * @param context a group event lua context
     * @param pointId the scene point id of the dungeon checkpoint
     * @return 0 if successful, 1 if dungeon manager is null, 2 if dungeon manager failed to activate the checkpoint
     */
    public static int ActivateDungeonCheckPoint(GroupEventLuaContext context, int pointId){
        return context.getScriptLibHandler().ActivateDungeonCheckPoint(context, pointId);
    }

    public static int SetWeatherAreaState(GroupEventLuaContext context, int var1, int var2){
        return context.getScriptLibHandler().SetWeatherAreaState(context, var1, var2);
    }

    public static int EnterWeatherArea(GroupEventLuaContext context, int weatherAreaId){
        return context.getScriptLibHandler().EnterWeatherArea(context, weatherAreaId);
    }

    public static boolean CheckIsInMpMode(GroupEventLuaContext context){
        return context.getScriptLibHandler().CheckIsInMpMode(context);
    }

    /**
     * TODO properly implement
     * var3 might contain the next point, sometimes is a single int, sometimes multiple ints as array
     * var4 has RouteType route_type, bool turn_mode
     */
    public static int SetPlatformPointArray(GroupEventLuaContext context, int entityConfigId, int pointArrayId, Object var3Table, Object var4Table){
        val var3 = context.getEngine().getTable(var3Table);
        val var4 = context.getEngine().getTable(var4Table);
        return context.getScriptLibHandler().SetPlatformPointArray(context, entityConfigId, pointArrayId, var3, var4);
    }

    //TODO check
    public static int SetPlatformRouteId(GroupEventLuaContext context, int entityConfigId, int routeId){
        return context.getScriptLibHandler().SetPlatformRouteId(context, entityConfigId, routeId);
    }

    //TODO check
    public static int StartPlatform(GroupEventLuaContext context, int configId){
        return context.getScriptLibHandler().StartPlatform(context, configId);
    }

    //TODO check
    public static int StopPlatform(GroupEventLuaContext context, int configId){
        return context.getScriptLibHandler().StopPlatform(context, configId);
    }

    public static int CreateChannellerSlabCampRewardGadget(GroupEventLuaContext context, int configId){
        return context.getScriptLibHandler().CreateChannellerSlabCampRewardGadget(context, configId);
    }

    public static int AssignPlayerShowTemplateReminder(GroupEventLuaContext context, int var1, Object var2Table){
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().AssignPlayerShowTemplateReminder(context, var1, var2);
    }

    public static int RevokePlayerShowTemplateReminder(GroupEventLuaContext context, int var1, Object var2Table){
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().RevokePlayerShowTemplateReminder(context, var1, var2);
    }

    public static int UnlockForce(GroupEventLuaContext context, int force){
        return context.getScriptLibHandler().UnlockForce(context, force);
    }

    public static int LockForce(GroupEventLuaContext context, int force){
        return context.getScriptLibHandler().LockForce(context, force);
    }

    public static int KillGroupEntity(GroupEventLuaContext context, Object rawTable) {
        val table = context.getEngine().getTable(rawTable);
        val groupId = table.optInt("group_id", -1);
        val killPolicyId = table.optInt("kill_policy", -1);
        if(groupId == -1){
            scriptLogger.error(() -> "[KillGroupEntity] groupId not set");
            return INVALID_PARAMETER_TABLE_CONTENT.getValue();
        }
        if(killPolicyId == -1){
            return killByCfgIds(context, groupId, table);
        }
        return killByGroupPolicy(context, groupId, killPolicyId);
    }

    private static int killByGroupPolicy(GroupEventLuaContext context, int groupId, int killPolicyId){
        scriptLogger.debug(() -> "[KillGroupEntity] kill by group policy");
        if(killPolicyId >= GroupKillPolicy.values().length){
            scriptLogger.error(() -> "[KillGroupEntity] kill_policy out of bounds");
            return INVALID_PARAMETER.getValue();
        }
        val policy = GroupKillPolicy.values()[killPolicyId];
        return context.getScriptLibHandler().KillGroupEntityByPolicy(context, groupId, policy);
    }

    private static int killByCfgIds(GroupEventLuaContext context, int groupId, LuaTable luaTable){
        scriptLogger.debug(() -> "[KillGroupEntity] kill by cfg ids");
        val monsterList = luaTable.getTable("monsters");
        val gadgetList = luaTable.getTable("gadgets");
        val monsters = monsterList != null ? monsterList.getAsIntArray() : new int[0];
        val gadgets = gadgetList != null ? gadgetList.getAsIntArray() : new int[0];

        return context.getScriptLibHandler().KillGroupEntityByCfgIds(context, groupId, monsters, gadgets);
    }

    public static int GetMonsterIdByEntityId(GroupEventLuaContext context, int entityId){
        return context.getScriptLibHandler().GetMonsterIdByEntityId(context, entityId);
    }
    public static int GetMonsterConfigId(GroupEventLuaContext context, int entityId){
        return context.getScriptLibHandler().GetMonsterConfigId(context, entityId);
    }
    public static int GetMonsterID(GroupEventLuaContext context, int var1){
        return context.getScriptLibHandler().GetMonsterID(context, var1);
    }
    public static int GetEntityIdByConfigId(GroupEventLuaContext context, int configId){
        return context.getScriptLibHandler().GetEntityIdByConfigId(context, configId);
    }
    public static int GetAvatarEntityIdByUid(GroupEventLuaContext context, int uid){
        return context.getScriptLibHandler().GetAvatarEntityIdByUid(context, uid);
    }



    public static Object GetPosByEntityId(GroupEventLuaContext context, int entityId){
        val pos = context.getScriptLibHandler().GetPosByEntityId(context, entityId);
        return posToLua(pos, context.getEngine()).getRawTable();
    }

    public static Object GetRotationByEntityId(GroupEventLuaContext context, int entityId){
        val rot = context.getScriptLibHandler().GetRotationByEntityId(context, entityId);
        return posToLua(rot, context.getEngine()).getRawTable();
    }

    public static Object GetActivityOpenAndCloseTimeByScheduleId(GroupEventLuaContext context, int scheduleId){
        var result = context.getEngine().createTable();
        var activityConfig = context.getScriptLibHandler().GetActivityOpenAndCloseTimeByScheduleId(context, scheduleId);

        if(activityConfig != null){
            result.set(1, activityConfig.getOpenTime().getTime());
            result.set(2, activityConfig.getCloseTime().getTime());
        }

        return result.getRawTable();
    }

    public static int GetGameHour(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetGameHour(context);
    }

    public static int ActivateGroupLinkBundle(GroupEventLuaContext context, int groupId){
        return context.getScriptLibHandler().ActivateGroupLinkBundle(context, groupId);
    }
    public static int ActivateGroupLinkBundleByBundleId(GroupEventLuaContext context, int bundleId){
        return context.getScriptLibHandler().ActivateGroupLinkBundleByBundleId(context, bundleId);
    }

    /**
     * TODO implement
     * @param context
     * @param givingId The id if the giving element found in [GivingData]
     * @param groupId The groupdId of the group containing the gadget
     * @param gadgetCfgId The gadgets target configId
     * @return 0 if success, something else if failed
     */
    public static int ActiveGadgetItemGiving(GroupEventLuaContext context, int givingId, int groupId, int gadgetCfgId){
        return context.getScriptLibHandler().ActiveGadgetItemGiving(context, givingId, groupId, gadgetCfgId);
    }

    public static int AddChessBuildingPoints(GroupEventLuaContext context, int groupId, int param2, int uid, int pointsToAdd){
        return context.getScriptLibHandler().AddChessBuildingPoints(context, groupId, param2, uid, pointsToAdd);
    }

    public static int AddEntityGlobalFloatValueByConfigId(GroupEventLuaContext context, Object param1Table, String param2, int param3){
        val param1 = context.getEngine().getTable(param1Table);
        return context.getScriptLibHandler().AddEntityGlobalFloatValueByConfigId(context, param1, param2, param3);
    }

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2  probably the name of the data field
     * @param param3
     * @return
     */
    public static int AddExhibitionAccumulableData(GroupEventLuaContext context, int uid, String param2, int param3){
        return context.getScriptLibHandler().AddExhibitionAccumulableData(context, uid, param2, param3);
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
    public static int AddExhibitionAccumulableDataAfterSuccess(GroupEventLuaContext context, int uid, String param2, int param3, Object param4Table){
        val param4 = context.getEngine().getTable(param4Table);
        val exhibitionTypeIndex = param4.optInt("play_type", -1);
        val galleryId = param4.optInt("gallery_id", -1);
        if(exhibitionTypeIndex < 0 || exhibitionTypeIndex >= ExhibitionPlayType.values().length){
            scriptLogger.error(() -> "[AddExhibitionAccumulableDataAfterSuccess] Invalid exhibition type " + exhibitionTypeIndex);
            return INVALID_PARAMETER_TABLE_CONTENT.getValue();
        }
        if (galleryId == -1){
            scriptLogger.error(() -> "[AddExhibitionAccumulableDataAfterSuccess] Invalid gallery id " + galleryId);
            return INVALID_PARAMETER_TABLE_CONTENT.getValue();
        }
        val exhibitionTypeEnum = ExhibitionPlayType.values()[exhibitionTypeIndex];
        return context.getScriptLibHandler().AddExhibitionAccumulableDataAfterSuccess(context, uid, param2, param3, exhibitionTypeEnum, galleryId);
    }

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2  probably the name of the data field
     * @param param3
     * @return
     */
    public static int AddExhibitionReplaceableData(GroupEventLuaContext context, int uid, String param2, int param3){
        return context.getScriptLibHandler().AddExhibitionReplaceableData(context, uid, param2, param3);
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
    public static int AddExhibitionReplaceableDataAfterSuccess(GroupEventLuaContext context, int uid, String param2, int param3, Object param4Table){
        val param4 = context.getEngine().getTable(param4Table);
        return context.getScriptLibHandler().AddExhibitionReplaceableDataAfterSuccess(context, uid, param2, param3, param4);
    }

    public static int AddFleurFairMultistagePlayBuffEnergy(GroupEventLuaContext context, int groupId, int param2, int uid, int bonusId){
        return context.getScriptLibHandler().AddFleurFairMultistagePlayBuffEnergy(context, groupId, param2, uid, bonusId);
    }

    public static int AddGadgetPlayProgress(GroupEventLuaContext context, int param1, int param2, int progressChange){
        return context.getScriptLibHandler().AddGadgetPlayProgress(context, param1, param2, progressChange);
    }

    public static int AddIrodoriChessBuildingPoints(GroupEventLuaContext context, int groupId, int param2, int points){
        return context.getScriptLibHandler().AddIrodoriChessBuildingPoints(context, groupId, param2, points);
    }
    public static int AddIrodoriChessTowerServerGlobalValue(GroupEventLuaContext context, int groupId, int param2, int param3, int delta){
        return context.getScriptLibHandler().AddIrodoriChessTowerServerGlobalValue(context, groupId, param2, param3, delta);
    }
    public static int AddMechanicusBuildingPoints(GroupEventLuaContext context, int groupId, int param2, int uid, int delta){
        return context.getScriptLibHandler().AddMechanicusBuildingPoints(context, groupId, param2, uid, delta);
    }

    public static int AddRegionRecycleProgress(GroupEventLuaContext context, int regionId, int delta){
        return context.getScriptLibHandler().AddRegionRecycleProgress(context, regionId, delta);
    }
    public static int AddRegionSearchProgress(GroupEventLuaContext context, int regionId, int delta){
        return context.getScriptLibHandler().AddRegionSearchProgress(context, regionId, delta);
    }
    public static int AddRegionalPlayVarValue(GroupEventLuaContext context, int uid, int regionId, int delta){
        return context.getScriptLibHandler().AddRegionalPlayVarValue(context, uid, regionId, delta);
    }
    public static int AddSceneMultiStagePlayUidValue(GroupEventLuaContext context, int groupId, int param2, String param3, int uid, int param5){
        return context.getScriptLibHandler().AddSceneMultiStagePlayUidValue(context, groupId, param2, param3, uid, param5);
    }
    public static int AddScenePlayBattleProgress(GroupEventLuaContext context, int groupId, int progress){
        return context.getScriptLibHandler().AddScenePlayBattleProgress(context, groupId, progress);
    }

    /**
     * TODO implement
     * @param context
     * @param param1Table contains the following fields: param_index:int, param_list:Table, param_uid_list:Table,
     *                    duration:int, target_uid_list:Table
     * @return
     */
    public static int AssignPlayerUidOpNotify(GroupEventLuaContext context, Object param1Table){
        val param1 = context.getEngine().getTable(param1Table);
        return context.getScriptLibHandler().AssignPlayerUidOpNotify(context, param1);
    }

    public static int CreateTreasureMapSpotRewardGadget(GroupEventLuaContext context, int gadgetCfgId){
        return context.getScriptLibHandler().CreateTreasureMapSpotRewardGadget(context, gadgetCfgId);
    }


    /**
     * TODO better parameter handling and verify active handling
     * Calls a lua function in the specified group if the group is active. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    public static int ExecuteActiveGroupLua(GroupEventLuaContext context, int groupId, String functionName, Object callParamsTable){
        val callParams = context.getEngine().getTable(callParamsTable);
        return context.getScriptLibHandler().ExecuteActiveGroupLua(context, groupId, functionName, callParams);
    }

    /**
     * TODO better parameter handling
     * Calls a lua function in the specified group. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    public static int ExecuteGroupLua(GroupEventLuaContext context, int groupId, String functionName, Object callParamsTable){
        val callParams = context.getEngine().getTable(callParamsTable);
        return context.getScriptLibHandler().ExecuteGroupLua(context, groupId, functionName, callParams);
    }


    /* GroupGadgetHandler methods */

    /**
     * Change the state of a gadget in the current group
     * @param context The context of the group event
     * @param configId config id of a gadget in the current caller group
     * @param gadgetState target state for the gadget
     */
    public static int SetGadgetStateByConfigId(GroupEventLuaContext context, int configId, int gadgetState) {
        if(configId <= 0){
            scriptLogger.error(() -> "[SetGadgetStateByConfigId] Invalid configId (" + configId + ")");
            return INVALID_PARAMETER.getValue();
        }
        return context.getScriptLibHandlerProvider().getGroupGadgetHandler().SetGadgetStateByConfigId(context, configId, gadgetState);
    }


    /**
     * Change the state of a gadget in the defined group
     * @param context The context of the group event
     * @param groupId The group containing the target gadget or the caller group if 0
     * @param configId config id of a gadget in the target group
     * @param gadgetState target state for the gadget
     */
    public static int SetGroupGadgetStateByConfigId(GroupEventLuaContext context, int groupId, int configId, int gadgetState) {
        if(groupId < 0 || configId <= 0){
            scriptLogger.error(() -> "[SetGroupGadgetStateByConfigId] Invalid groupId (" + groupId + ") or configId (" + configId + ")");
            return INVALID_PARAMETER.getValue();
        }
        return context.getScriptLibHandlerProvider().getGroupGadgetHandler().SetGroupGadgetStateByConfigId(context, groupId, configId, gadgetState);
    }

    /**
     * Returns the state of a gadget based on the group id and config id
     * @param context The context of the group event
     * @param groupId group to search for the gadget in, 0 for the caller group.
     * @param configId config id of the gadget in the group.
     */
    public static int GetGadgetStateByConfigId(GroupEventLuaContext context, int groupId, int configId){
        if(groupId < 0 || configId <= 0){
            scriptLogger.error(() -> "[GetGadgetStateByConfigId] Invalid groupId (" + groupId + ") or configId (" + configId + ")");
            return INVALID_PARAMETER.getValue();
        }
        return context.getScriptLibHandlerProvider().getGroupGadgetHandler().GetGadgetStateByConfigId(context, groupId, configId);
    }


    /**
     * Change the state if a gadget in the current group, based in the parametersTable
     * @param context The context of the group event
     * @param parametersTable The parameter table, contains the following fields: config_id:int, state:int
     */
    public static int ChangeGroupGadget(GroupEventLuaContext context, Object parametersTable) {
        val table = context.getEngine().getTable(parametersTable);
        val configId = table.optInt("config_id", -1);
        val state = table.optInt("state", -1);
        if(configId <= 0 || state <= 0){
            scriptLogger.error(() -> "[ChangeGroupGadget] Invalid configId (" + configId + ") or state (" + state + ")");
            return INVALID_PARAMETER_TABLE_CONTENT.getValue();
        }
        return context.getScriptLibHandlerProvider().getGroupGadgetHandler().ChangeGroupGadget(context, configId, state);
    }

    /**
     * Retrieves and returns the gadget id of a gadget entity based on the entity id.
     * @param context The context of the group event
     * @param entityId The entity id of the gadget requested.
     */
    public static int GetGadgetIdByEntityId(GroupEventLuaContext context, int entityId){
        if(entityId <= 0){
            scriptLogger.error(() -> "[GetGadgetIdByEntityId] Invalid or missing group_eid " + entityId);
            return INVALID_PARAMETER.getValue();
        }
        return context.getScriptLibHandlerProvider().getGroupGadgetHandler().GetGadgetIdByEntityId(context, entityId);
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
    public static int ExecuteGadgetLua(GroupEventLuaContext context, int groupId, int gadgetCfgId, int activityType, int var4, int val5){
        return context.getScriptLibHandlerProvider().getGroupGadgetHandler().ExecuteGadgetLua(context, groupId, gadgetCfgId, activityType, var4, val5);
    }

    /**
     * Returns the config id of the gadget with the eid (gadget_eid)
     * @param context The context of the group event
     * @param paramsTable The parameter table, contains only `gadget_eid`, which contains the entity id of the gadget requested.
     */
    public static int GetGadgetConfigId(GroupEventLuaContext context, Object paramsTable){
        val params = context.getEngine().getTable(paramsTable);
        val groupEid = params.optInt("group_eid", -1);
        if(groupEid <= 0){
            scriptLogger.error(() -> "[GetGadgetConfigId] Invalid or missing group_eid " + groupEid);
            return INVALID_PARAMETER_TABLE_CONTENT.getValue();
        }
        return context.getScriptLibHandlerProvider().getGroupGadgetHandler().GetGadgetConfigId(context, groupEid);
    }

    /**
     * Returns the hp in percent of a gadget based on the group id and config id
     * @param context The context of the group event
     * @param groupId group to search for the gadget entity in.
     * @param configId config id of the gadget in the group.
     */
    public static int GetGadgetHpPercent(GroupEventLuaContext context, int groupId, int configId){
        if(groupId < 0 || configId <= 0){
            scriptLogger.error(() -> "[GetGadgetHpPercent] Invalid groupId (" + groupId + ") or configId (" + configId + ")");
            return INVALID_PARAMETER.getValue();
        }
        return context.getScriptLibHandlerProvider().getGroupGadgetHandler().GetGadgetHpPercent(context, groupId, configId);
    }


    /**
     * Returns a float global value from the gadgets ability definitions.
     * @param context The context of the group event
     * @param groupId group to search for the gadget entity in.
     * @param configId config id of the gadget in the group.
     * @param abilitySGVName name of the abilities svg value to get the float value from.
     * */
    public static float GetGadgetAbilityFloatValue(GroupEventLuaContext context, int groupId, int configId, String abilitySGVName) {
        if(groupId < 0 || configId <= 0){
            scriptLogger.error(() -> "[GetGadgetAbilityFloatValue] Invalid groupId (" + groupId + ") or configId (" + configId + ")");
            return INVALID_PARAMETER.getValue();
        }
        if(abilitySGVName == null || abilitySGVName.isEmpty()){
            scriptLogger.error(() -> "[GetGadgetAbilityFloatValue] Invalid ability SGV name (" + abilitySGVName + ")");
            return INVALID_PARAMETER.getValue();
        }
        return context.getScriptLibHandlerProvider().getGroupGadgetHandler().GetGadgetAbilityFloatValue(context, groupId, configId, abilitySGVName);
    }


    /* GadgetControllerHandler methods */
    /**
     * Methods used in EntityControllers/using ControllerLuaContext
     */

    public static int SetGadgetState(ControllerLuaContext<Object> context, int gadgetState) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.SetGadgetState(context, gadgetState);
    }

    public static int GetGadgetState(ControllerLuaContext<Object> context) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.GetGadgetState(context);
    }

    @Nullable
    public static int[] GetGadgetArguments(ControllerLuaContext<Object> context) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return null;
        }
        return handler.GetGadgetArguments(context);
    }

    public static int ResetGadgetState(ControllerLuaContext<Object> context, int gadgetState) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.ResetGadgetState(context, gadgetState);
    }

    public static int SetGearStartValue(ControllerLuaContext<Object> context, int startValue) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.SetGearStartValue(context, startValue);
    }

    public static int GetGearStartValue(ControllerLuaContext<Object> context) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.GetGearStartValue(context);
    }

    public static int SetGearStopValue(ControllerLuaContext<Object> context, int startValue) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.SetGearStopValue(context, startValue);
    }

    public static int GetGearStopValue(ControllerLuaContext<Object> context) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.GetGearStopValue(context);
    }

    public static int GetGadgetStateBeginTime(ControllerLuaContext<Object> context) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.GetGadgetStateBeginTime(context);
    }

    public static int GetContextGadgetConfigId(ControllerLuaContext<Object> context) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.GetContextGadgetConfigId(context);
    }

    public static int GetContextGroupId(ControllerLuaContext<Object> context) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return handler.GetContextGroupId(context);
    }

    public static int SetGadgetEnableInteract(LuaContext context, int groupId, int configId, boolean enable) {
        if(context instanceof ControllerLuaContext cContext){
            val handler = cContext.getScriptLibHandlerProvider().getGadgetControllerHandler();
            if(handler == null){
                return NOT_IMPLEMENTED.getValue();
            }
            return handler.SetGadgetEnableInteract(cContext, groupId, configId, enable);
        } else if(context instanceof GroupEventLuaContext gContext){
            val handler = gContext.getScriptLibHandlerProvider().getScriptLibHandler();
            if(handler == null){
                return NOT_IMPLEMENTED.getValue();
            }
            return handler.SetGadgetEnableInteract(gContext, groupId, configId, enable);
        }
        return NOT_IMPLEMENTED.getValue();
    }

    public static int DropSubfield(ControllerLuaContext<Object> context, Object paramsTable) {
        val params = context.getEngine().getTable(paramsTable);

        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return NOT_IMPLEMENTED.getValue();
        }
        return context.getScriptLibHandlerProvider().getGadgetControllerHandler().DropSubfield(context, params);
    }

    public static int[] GetGatherConfigIdList(ControllerLuaContext<Object> context) {
        val handler = context.getScriptLibHandlerProvider().getGadgetControllerHandler();
        if(handler == null){
            return null;
        }
        return handler.GetGatherConfigIdList(context);
    }
}
