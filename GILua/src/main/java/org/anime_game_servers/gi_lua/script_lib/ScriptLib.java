package org.anime_game_servers.gi_lua.script_lib;

import lombok.val;

import static org.anime_game_servers.gi_lua.utils.ScriptUtils.posToLua;

public class ScriptLib {

    /**
     * Context free functions
     */

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
        context.getScriptLibHandler().PrintContextLog(context, msg);
    }


    /**
     * GroupEventLuaContext functions
     */

    public static int SetGadgetStateByConfigId(GroupEventLuaContext context, int configId, int gadgetState) {
		return context.getScriptLibHandler().SetGadgetStateByConfigId(context, configId, gadgetState);
	}

	public static int SetGroupGadgetStateByConfigId(GroupEventLuaContext context, int groupId, int configId, int gadgetState) {
		return context.getScriptLibHandler().SetGroupGadgetStateByConfigId(context, groupId, configId, gadgetState);
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
        return context.getScriptLibHandler().AddExtraFlowSuite(context, groupId, suiteId, flowSuitePolicy);
    }
    public static int RemoveExtraFlowSuite(GroupEventLuaContext context, int groupId, int suiteId, int flowSuitePolicy){
        return context.getScriptLibHandler().RemoveExtraFlowSuite(context, groupId, suiteId, flowSuitePolicy);
    }
    public static int KillExtraFlowSuite(GroupEventLuaContext context, int groupId, int suiteId, int flowSuitePolicy){
        return context.getScriptLibHandler().KillExtraFlowSuite(context, groupId, suiteId, flowSuitePolicy);
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
		return context.getScriptLibHandler().GetRegionEntityCount(context, table);
	}

    public int GetRegionConfigId(GroupEventLuaContext context, Object rawTable){
        val table = context.getEngine().getTable(rawTable);
        return context.getScriptLibHandler().GetRegionConfigId(context, table);
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
        return context.getScriptLibHandler().KillEntityByConfigId(context, table);
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

	public static int CheckRemainGadgetCountByGroupId(GroupEventLuaContext context, Object rawTable) {
        val table = context.getEngine().getTable(rawTable);
		return context.getScriptLibHandler().CheckRemainGadgetCountByGroupId(context, table);
	}

	public static int GetGadgetStateByConfigId(GroupEventLuaContext context, int groupId, int configId){
		return context.getScriptLibHandler().GetGadgetStateByConfigId(context, groupId, configId);
	}

	public static int MarkPlayerAction(GroupEventLuaContext context, int var1, int var2, int var3){
        return context.getScriptLibHandler().MarkPlayerAction(context, var1, var2, var3);
	}

	public static int AddQuestProgress(GroupEventLuaContext context, String eventNotifyName){
        return context.getScriptLibHandler().AddQuestProgress(context, eventNotifyName);
	}

	/**
	 * change the state of gadget
	 */
	public static int ChangeGroupGadget(GroupEventLuaContext context, Object rawTable) {
        val table = context.getEngine().getTable(rawTable);
		return context.getScriptLibHandler().ChangeGroupGadget(context, table);
	}

    public static int GetSceneOwnerUid(GroupEventLuaContext context){
        return context.getScriptLibHandler().GetSceneOwnerUid(context);
    }

    public static int GetHostQuestState(GroupEventLuaContext context, int questId){
        return context.getScriptLibHandler().GetHostQuestState(context, questId);
    }

    public static int GetQuestState(GroupEventLuaContext context, int entityId, int questId){
        return context.getScriptLibHandler().GetQuestState(context, entityId, questId);
    }

    public static int ShowReminder(GroupEventLuaContext context, int reminderId){
        return context.getScriptLibHandler().ShowReminder(context, reminderId);
    }

    public static int RemoveEntityByConfigId(GroupEventLuaContext context, int groupId, int entityType, int configId){
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

    public static Object GetSceneUidList(GroupEventLuaContext context){
        val list = context.getScriptLibHandler().GetSceneUidList(context);

        val result = context.getEngine().createTable();

        for(int i = 0; i< list.length; i++){
            result.set(Integer.toString(i+1), list[i]);
        }
        return result;
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

    public static int CreateFatherChallenge(GroupEventLuaContext context, int challengeIndex, int challengeId, int timeLimit, Object conditionTable){
        val conditionLuaTable = context.getEngine().getTable(conditionTable);
        return context.getScriptLibHandler().CreateFatherChallenge(context, challengeIndex, challengeId, timeLimit, conditionLuaTable);
    }
    public static int StartFatherChallenge(GroupEventLuaContext context, int challengeIndex){
        return context.getScriptLibHandler().StartFatherChallenge(context, challengeIndex);
    }
    public static int ModifyFatherChallengeProperty(GroupEventLuaContext context, int challengeId, int propertyTypeIndex, int value){
        return context.getScriptLibHandler().ModifyFatherChallengeProperty(context, challengeId, propertyTypeIndex, value);
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

    public static int StartSealBattle(GroupEventLuaContext context, int gadgetId, Object var2Table) {
        val var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().StartSealBattle(context, gadgetId, var2);
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
        return context.getScriptLibHandler().InitGalleryProgressScore(context, name, galleryId, progress, scoreUiTypeIndex, scoreTypeIndex);
    }
    public static int InitGalleryProgressWithScore(GroupEventLuaContext context, String name, int galleryId, Object progressTable,
                                               int maxProgress, int scoreUiTypeIndex, int scoreTypeIndex) {
        val progress = context.getEngine().getTable(progressTable);
        return context.getScriptLibHandler().InitGalleryProgressWithScore(context, name, galleryId, progress, maxProgress, scoreUiTypeIndex, scoreTypeIndex);
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

    public static int UnlockFloatSignal(GroupEventLuaContext context, int groupId, int gadgetSignalId){
        return context.getScriptLibHandler().UnlockFloatSignal(context, groupId, gadgetSignalId);
    }

    public static int SendServerMessageByLuaKey(GroupEventLuaContext context, String var1, Object var2Table){
        var var2 = context.getEngine().getTable(var2Table);
        return context.getScriptLibHandler().SendServerMessageByLuaKey(context, var1, var2);
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

    public static int MovePlayerToPos(GroupEventLuaContext context, Object rawTable) {
        val table = context.getEngine().getTable(rawTable);
        return context.getScriptLibHandler().MovePlayerToPos(context, table);
    }

    public static int TransPlayerToPos(GroupEventLuaContext context, Object rawTable) {
        val var1 = context.getEngine().getTable(rawTable);
        return context.getScriptLibHandler().TransPlayerToPos(context, var1);
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

    public int ClearPlayerEyePoint(GroupEventLuaContext context, int var1){
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
        return context.getScriptLibHandler().KillGroupEntity(context, table);
    }

    public static int GetGadgetIdByEntityId(GroupEventLuaContext context, int entityId){
        return context.getScriptLibHandler().GetGadgetIdByEntityId(context, entityId);
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
        val rot = context.getScriptLibHandler().GetPosByEntityId(context, entityId);
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
        return context.getScriptLibHandler().AddExhibitionAccumulableDataAfterSuccess(context, uid, param2, param3, param4);
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


    /**
     * Methods used in EntityControllers/using ControllerLuaContext
     */

    public static int SetGadgetState(ControllerLuaContext<?> context, int gadgetState) {
        return context.getScriptLibHandler().SetGadgetState(context, gadgetState);
    }

    public static int GetGadgetState(ControllerLuaContext<?> context) {
        return context.getScriptLibHandler().GetGadgetState(context);
    }

    public static int ResetGadgetState(ControllerLuaContext<?> context, int gadgetState) {
        return context.getScriptLibHandler().ResetGadgetState(context, gadgetState);
    }

    public static int SetGearStartValue(ControllerLuaContext<?> context, int startValue) {
        return context.getScriptLibHandler().SetGearStartValue(context, startValue);
    }

    public static int GetGearStartValue(ControllerLuaContext<?> context) {
        return context.getScriptLibHandler().GetGearStartValue(context);
    }

    public static int SetGearStopValue(ControllerLuaContext<?> context, int startValue) {
        return context.getScriptLibHandler().SetGearStopValue(context, startValue);
    }

    public static int GetGearStopValue(ControllerLuaContext<?> context) {
        return context.getScriptLibHandler().GetGearStopValue(context);
    }

    public static int GetGadgetStateBeginTime(ControllerLuaContext<?> context) {
        return context.getScriptLibHandler().GetGadgetStateBeginTime(context);
    }

    public static int GetContextGadgetConfigId(ControllerLuaContext<?> context) {
        return context.getScriptLibHandler().GetContextGadgetConfigId(context);
    }

    public static int GetContextGroupId(ControllerLuaContext<?> context) {
        return context.getScriptLibHandler().GetContextGroupId(context);
    }

    public static int SetGadgetEnableInteract(ControllerLuaContext<?> context, int groupId, int configId, boolean enable) {
        return context.getScriptLibHandler().SetGadgetEnableInteract(context, groupId, configId, enable);
    }

    public static int DropSubfield(ControllerLuaContext<?> context, Object paramsTable) {
        val params = context.getEngine().getTable(paramsTable);
        return context.getScriptLibHandler().DropSubfield(context, params);
    }

    public static int[] GetGatherConfigIdList(ControllerLuaContext<?> context) {
        return context.getScriptLibHandler().GetGatherConfigIdList(context);
    }
}
