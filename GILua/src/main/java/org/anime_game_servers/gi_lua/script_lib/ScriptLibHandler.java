package org.anime_game_servers.gi_lua.script_lib;

import org.anime_game_servers.gi_lua.models.Position;
import org.anime_game_servers.lua.engine.LuaTable;

@SuppressWarnings("unused")
public interface ScriptLibHandler {

    /**
     * Context independent functions
     */
    void PrintContextLog(LuaContext context, String msg);


    /**
     * GroupEventLuaContext functions
     */

    int SetGadgetStateByConfigId(GroupEventLuaContext context, int configId, int gadgetState);
	int SetGroupGadgetStateByConfigId(GroupEventLuaContext context, int groupId, int configId, int gadgetState);
	int SetWorktopOptionsByGroupId(GroupEventLuaContext context, int groupId, int configId, LuaTable options);
	int SetWorktopOptions(GroupEventLuaContext context, LuaTable table);

	int DelWorktopOptionByGroupId(GroupEventLuaContext context, int groupId, int configId, int option);
    int DelWorktopOption(GroupEventLuaContext context, int var1);

	// Some fields are guessed
	int AutoMonsterTide(GroupEventLuaContext context, int challengeIndex, int groupId, Integer[] ordersConfigId, int tideCount, int sceneLimit, int param6);
    int GoToGroupSuite(GroupEventLuaContext context, int groupId, int suite);

	int AddExtraGroupSuite(GroupEventLuaContext context, int groupId, int suite);
	int RemoveExtraGroupSuite(GroupEventLuaContext context, int groupId, int suite);
	int KillExtraGroupSuite(GroupEventLuaContext context, int groupId, int suite);

    int AddExtraFlowSuite(GroupEventLuaContext context, int groupId, int suiteId, int flowSuitePolicy);
    int RemoveExtraFlowSuite(GroupEventLuaContext context, int groupId, int suiteId, int flowSuitePolicy);
    int KillExtraFlowSuite(GroupEventLuaContext context, int groupId, int suiteId, int flowSuitePolicy);

	int ActiveChallenge(GroupEventLuaContext context, int challengeIndex, int challengeId, int timeLimitOrGroupId, int groupId, int objectiveKills, int param5);

    int StartChallenge(GroupEventLuaContext context, int challengeIndex, int challengeId, LuaTable challengeParams);

    int StopChallenge(GroupEventLuaContext context, int challengeIndex, int result);

    /**
     * Adds or removed time from the challenge
     * TODO verify and implement
     * @param context
     * @param challengeId The active target challenges id
     * @param duration The duration to add or remove
     * @return 0 if success, 1 if no challenge is active, 2 if the challenge id doesn't match the active challenge,
     * 3 if modifying the duration failed
     */
    int AddChallengeDuration(GroupEventLuaContext context, int challengeId, int duration);

	int GetGroupMonsterCountByGroupId(GroupEventLuaContext context, int groupId);

	int CreateVariable(GroupEventLuaContext context, String type, LuaTable value);
	int SetVariableValue(GroupEventLuaContext context, int var1);
	int GetVariableValue(GroupEventLuaContext context, int var1);

	int GetGroupVariableValue(GroupEventLuaContext context, String var);

    int GetGroupVariableValueByGroup(GroupEventLuaContext context, String name, int groupId);

	int SetGroupVariableValue(GroupEventLuaContext context, String varName, int value);

    int SetGroupVariableValueByGroup(GroupEventLuaContext context, String key, int value, int groupId);

	int ChangeGroupVariableValue(GroupEventLuaContext context, String varName, int value);

    int ChangeGroupVariableValueByGroup(GroupEventLuaContext context, String name, int value, int groupId);


	/**
	 * Set the actions and triggers to designated group
	 */
	int RefreshGroup(GroupEventLuaContext context, LuaTable table);

	int GetRegionEntityCount(GroupEventLuaContext context, LuaTable table);

    int GetRegionConfigId(GroupEventLuaContext context, LuaTable table);

	int TowerCountTimeStatus(GroupEventLuaContext context, int isDone, int var2);
	int GetGroupMonsterCount(GroupEventLuaContext context);

	int SetMonsterBattleByGroup(GroupEventLuaContext context, int configId, int groupId);

	int CauseDungeonFail(GroupEventLuaContext context);

    int SetEntityServerGlobalValueByConfigId(GroupEventLuaContext context, int cfgId, String sgvName, int value);

    int SetGroupLogicStateValue(GroupEventLuaContext context, String sgvName, int value);

	int SetIsAllowUseSkill(GroupEventLuaContext context, int canUse);

    int KillEntityByConfigId(LuaContext context, LuaTable table);

	int CreateMonster(GroupEventLuaContext context, LuaTable table);

	int TowerMirrorTeamSetUp(GroupEventLuaContext context, int team, int var1) ;

	int CreateGadget(GroupEventLuaContext context, LuaTable table);

	int CheckRemainGadgetCountByGroupId(GroupEventLuaContext context, LuaTable table);

	int GetGadgetStateByConfigId(GroupEventLuaContext context, int groupId, int configId);

	int MarkPlayerAction(GroupEventLuaContext context, int var1, int var2, int var3);
	int AddQuestProgress(GroupEventLuaContext context, String eventNotifyName);

	/**
	 * change the state of gadget
	 */
	int ChangeGroupGadget(GroupEventLuaContext context, LuaTable table) ;

    int GetSceneOwnerUid(GroupEventLuaContext context);
    int GetHostQuestState(GroupEventLuaContext context, int questId);
    int GetQuestState(GroupEventLuaContext context, int entityId, int questId);
    int ShowReminder(GroupEventLuaContext context, int reminderId);
    int RemoveEntityByConfigId(GroupEventLuaContext context, int groupId, int entityType, int configId);
    int CreateGroupTimerEvent(GroupEventLuaContext context, int groupID, String source, double time);
    int CancelGroupTimerEvent(GroupEventLuaContext context, int groupID, String source);

    int GetGroupSuite(GroupEventLuaContext context, int groupId);
    int SetGroupReplaceable(GroupEventLuaContext context, int groupId, boolean value) ;
    Object GetSceneUidList(GroupEventLuaContext context);
    int GetSeaLampActivityPhase(GroupEventLuaContext context);
    int GadgetPlayUidOp(GroupEventLuaContext context, int groupId, int gadget_crucible, int var3, int var4, String var5, int var6 );
    long GetServerTime(GroupEventLuaContext context);
    long GetServerTimeByWeek(GroupEventLuaContext context);
    int GetCurTriggerCount(GroupEventLuaContext context);
    int GetChannellerSlabLoopDungeonLimitTime(GroupEventLuaContext context);
    boolean IsPlayerAllAvatarDie(GroupEventLuaContext context, int sceneUid);

    int sendShowCommonTipsToClient(GroupEventLuaContext context, String title, String content, int closeTime);

    int sendCloseCommonTipsToClient(GroupEventLuaContext context);
    int CreateFatherChallenge(GroupEventLuaContext context, int challengeIndex, int challengeId, int timeLimit, LuaTable conditionTable);
    int StartFatherChallenge(GroupEventLuaContext context, int challengeIndex);
    int ModifyFatherChallengeProperty(GroupEventLuaContext context, int challengeId, int propertyTypeIndex, int value);
    int AttachChildChallenge(GroupEventLuaContext context, int fatherChallengeIndex, int childChallengeIndex,
                                           int childChallengeId, LuaTable var4, LuaTable var5, LuaTable var6);
    int CreateEffigyChallengeMonster(GroupEventLuaContext context, int var1, LuaTable var2Table);
    int GetEffigyChallengeMonsterLevel(GroupEventLuaContext context);
    int AddTeamEntityGlobalFloatValue(GroupEventLuaContext context, LuaTable sceneUidListTable, String var2, int var3);
    int CreateBlossomChestByGroupId(GroupEventLuaContext context, int groupId, int chestConfigId);
    int GetBlossomScheduleStateByGroupId(GroupEventLuaContext context, int groupId);
    int SetBlossomScheduleStateByGroupId(GroupEventLuaContext context, int groupId, int state);
    int RefreshBlossomGroup(GroupEventLuaContext context, LuaTable table);
    int RefreshBlossomDropRewardByGroupId(GroupEventLuaContext context, int groupId);
    int AddBlossomScheduleProgressByGroupId(GroupEventLuaContext context, int groupId);
    int GetBlossomRefreshTypeByGroupId(GroupEventLuaContext context, int groupId);
    int RefreshHuntingClueGroup(GroupEventLuaContext context);
    int GetHuntingMonsterExtraSuiteIndexVec(GroupEventLuaContext context);
    int SetGroupTempValue(GroupEventLuaContext context, String name, int value, LuaTable var3Table);
    int GetGroupTempValue(GroupEventLuaContext context, String name, LuaTable var2);

    int FinishExpeditionChallenge(GroupEventLuaContext context);
    int ExpeditionChallengeEnterRegion(GroupEventLuaContext context, boolean var1);
    int StartSealBattle(GroupEventLuaContext context, int gadgetId, LuaTable var2);

    int InitTimeAxis(GroupEventLuaContext context, String var1, LuaTable var2, boolean var3);
    int EndTimeAxis(GroupEventLuaContext context, String var1);

    int SetTeamEntityGlobalFloatValue(GroupEventLuaContext context, LuaTable sceneUidListTable, String var2, int var3);

    int SetTeamServerGlobalValue(GroupEventLuaContext context, int sceneUid, String var2, int var3);

    int AddTeamServerGlobalValue(GroupEventLuaContext context, int ownerId, String sgvName, int value);

    int GetTeamServerGlobalValue(GroupEventLuaContext context, int ownerId, String sgvName, int value);

    int GetLanternRiteValue(GroupEventLuaContext context);

    int CreateMonsterFaceAvatar(GroupEventLuaContext context, LuaTable table);

    int ChangeToTargetLevelTag(GroupEventLuaContext context, int var1);

    int AddSceneTag(GroupEventLuaContext context, int sceneId, int sceneTagId);

    int DelSceneTag(GroupEventLuaContext context, int sceneId, int sceneTagId);

    boolean CheckSceneTag(GroupEventLuaContext context, int sceneId, int sceneTagId);
    int StartHomeGallery(GroupEventLuaContext context, int galleryId, int uid);

    int StartGallery(GroupEventLuaContext context, int galleryId);

    int StopGallery(GroupEventLuaContext context, int galleryId, boolean var2);

    int UpdatePlayerGalleryScore(GroupEventLuaContext context, int galleryId, LuaTable var2);
    int InitGalleryProgressScore(GroupEventLuaContext context, String name, int galleryId, LuaTable progressTable,
                                               int scoreUiTypeIndex, int scoreTypeIndex);
    int InitGalleryProgressWithScore(GroupEventLuaContext context, String name, int galleryId, LuaTable progress,
                                               int maxProgress, int scoreUiTypeIndex, int scoreTypeIndex);
    int AddGalleryProgressScore(GroupEventLuaContext context, String name, int galleryId, int score);
    int GetGalleryProgressScore(GroupEventLuaContext context, String name, int galleryId) ;
    int SetHandballGalleryBallPosAndRot(GroupEventLuaContext context, int galleryId, LuaTable positionTable, LuaTable rotationTable);

    int UnlockFloatSignal(GroupEventLuaContext context, int groupId, int gadgetSignalId);

    int SendServerMessageByLuaKey(GroupEventLuaContext context, String var1, LuaTable var2);

    int TryReallocateEntityAuthority(GroupEventLuaContext context, int uid, int endConfig, int var3);

    int ForceRefreshAuthorityByConfigId(GroupEventLuaContext context, int var1, int uid);

    int AddPlayerGroupVisionType(GroupEventLuaContext context, LuaTable uidsTable, LuaTable var2);

    int DelPlayerGroupVisionType(GroupEventLuaContext context, LuaTable uidsTable, LuaTable var2);

    int MoveAvatarByPointArray(GroupEventLuaContext context, int uid, int targetId, LuaTable var3, String var4);

    int MovePlayerToPos(GroupEventLuaContext context, LuaTable table);

    int TransPlayerToPos(GroupEventLuaContext context, LuaTable table);

    int PlayCutScene(GroupEventLuaContext context, int cutsceneId, int var2);

    int PlayCutSceneWithParam(GroupEventLuaContext context, int cutsceneId, int var2, LuaTable var3);

    int ScenePlaySound(GroupEventLuaContext context, LuaTable soundInfoTable);

    int BeginCameraSceneLook(GroupEventLuaContext context, LuaTable sceneLookParamsTable);

    public int ClearPlayerEyePoint(GroupEventLuaContext context, int var1);

    int ShowReminderRadius(GroupEventLuaContext context, int var1, LuaTable var2, int var3);
    int ShowClientGuide(GroupEventLuaContext context, String guideName);

    /**
     * Activates a dungeon checkpoint.
     * @param context a group event lua context
     * @param pointId the scene point id of the dungeon checkpoint
     * @return 0 if successful, 1 if dungeon manager is null, 2 if dungeon manager failed to activate the checkpoint
     */
    int ActivateDungeonCheckPoint(GroupEventLuaContext context, int pointId);

    //TODO check
    int SetWeatherAreaState(GroupEventLuaContext context, int var1, int var2);

    int EnterWeatherArea(GroupEventLuaContext context, int weatherAreaId);

    //TODO check
    boolean CheckIsInMpMode(GroupEventLuaContext context);

    /**
     * TODO properly implement
     * var3 might contain the next point, sometimes is a single int, sometimes multiple ints as array
     * var4 has RouteType route_type, bool turn_mode
     */
    int SetPlatformPointArray(GroupEventLuaContext context, int entityConfigId, int pointArrayId, LuaTable var3, LuaTable var4);

    //TODO check
    int SetPlatformRouteId(GroupEventLuaContext context, int entityConfigId, int routeId);

    //TODO check
    int StartPlatform(GroupEventLuaContext context, int configId);

    //TODO check
    int StopPlatform(GroupEventLuaContext context, int configId);

    int CreateChannellerSlabCampRewardGadget(GroupEventLuaContext context, int configId);

    int AssignPlayerShowTemplateReminder(GroupEventLuaContext context, int var1, LuaTable var2);

    int RevokePlayerShowTemplateReminder(GroupEventLuaContext context, int var1, LuaTable var2);

    int UnlockForce(GroupEventLuaContext context, int force);

    int LockForce(GroupEventLuaContext context, int force);

    int KillGroupEntity(GroupEventLuaContext context, LuaTable table);
    int GetGadgetIdByEntityId(GroupEventLuaContext context, int entityId);
    int GetMonsterIdByEntityId(GroupEventLuaContext context, int entityId);
    int GetMonsterConfigId(GroupEventLuaContext context, int entityId);
    int GetMonsterID(GroupEventLuaContext context, int var1);
    int GetEntityIdByConfigId(GroupEventLuaContext context, int configId);
    int GetAvatarEntityIdByUid(GroupEventLuaContext context, int uid);


    Position GetPosByEntityId(GroupEventLuaContext context, int entityId);

    Position GetRotationByEntityId(GroupEventLuaContext context, int entityId);

    ActivityOpenAndCloseTime GetActivityOpenAndCloseTimeByScheduleId(GroupEventLuaContext context, int scheduleId);

    int GetGameHour(GroupEventLuaContext context);

    int ActivateGroupLinkBundle(GroupEventLuaContext context, int groupId);
    int ActivateGroupLinkBundleByBundleId(GroupEventLuaContext context, int bundleId);

    /**
     * TODO implement
     * @param context
     * @param givingId The id if the giving element found in [GivingData]
     * @param groupId The groupdId of the group containing the gadget
     * @param gadgetCfgId The gadgets target configId
     * @return 0 if success, something else if failed
     */
    int ActiveGadgetItemGiving(GroupEventLuaContext context, int givingId, int groupId, int gadgetCfgId);

    int AddChessBuildingPoints(GroupEventLuaContext context, int groupId, int param2, int uid, int pointsToAdd);

    int AddEntityGlobalFloatValueByConfigId(GroupEventLuaContext context, Object param1Table, String param2, int param3);

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2  probably the name of the data field
     * @param param3
     * @return
     */
    int AddExhibitionAccumulableData(GroupEventLuaContext context, int uid, String param2, int param3);

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2 probably the name of the data field
     * @param param3
     * @param param4 contains the fields "play_type" is part of the enum [ExhibitionPlayType] and "gallery_id"
     * @return
     */
    int AddExhibitionAccumulableDataAfterSuccess(GroupEventLuaContext context, int uid, String param2, int param3, LuaTable param4);

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2  probably the name of the data field
     * @param param3
     * @return
     */
    int AddExhibitionReplaceableData(GroupEventLuaContext context, int uid, String param2, int param3);

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2 probably the name of the data field
     * @param param3
     * @param param4 contains the fields "play_type" is part of the enum [ExhibitionPlayType] and "gallery_id"
     * @return
     */
    int AddExhibitionReplaceableDataAfterSuccess(GroupEventLuaContext context, int uid, String param2, int param3, LuaTable param4);

    int AddFleurFairMultistagePlayBuffEnergy(GroupEventLuaContext context, int groupId, int param2, int uid, int bonusId);

    int AddGadgetPlayProgress(GroupEventLuaContext context, int param1, int param2, int progressChange);

    int AddIrodoriChessBuildingPoints(GroupEventLuaContext context, int groupId, int param2, int points);
    int AddIrodoriChessTowerServerGlobalValue(GroupEventLuaContext context, int groupId, int param2, int param3, int delta);
    int AddMechanicusBuildingPoints(GroupEventLuaContext context, int groupId, int param2, int uid, int delta);

    int AddRegionRecycleProgress(GroupEventLuaContext context, int regionId, int delta);
    int AddRegionSearchProgress(GroupEventLuaContext context, int regionId, int delta);
    int AddRegionalPlayVarValue(GroupEventLuaContext context, int uid, int regionId, int delta);
    int AddSceneMultiStagePlayUidValue(GroupEventLuaContext context, int groupId, int param2, String param3, int uid, int param5);
    int AddScenePlayBattleProgress(GroupEventLuaContext context, int groupId, int progress);

    /**
     * TODO implement
     * @param context
     * @param param1Table contains the following fields: param_index:int, param_list:Table, param_uid_list:Table,
     *                    duration:int, target_uid_list:Table
     * @return
     */
    int AssignPlayerUidOpNotify(GroupEventLuaContext context, LuaTable param1Table);


    /**
     * Methods used in EntityControllers/using ControllerLuaContext
     */

    int SetGadgetState(ControllerLuaContext context, int gadgetState);

    int GetGadgetState(ControllerLuaContext context);

    int ResetGadgetState(ControllerLuaContext context, int gadgetState) ;

    int SetGearStartValue(ControllerLuaContext context, int startValue);

    int GetGearStartValue(ControllerLuaContext context);

    int SetGearStopValue(ControllerLuaContext context, int startValue);

    int GetGearStopValue(ControllerLuaContext context) ;

    int GetGadgetStateBeginTime(ControllerLuaContext context);

    int GetContextGadgetConfigId(ControllerLuaContext context) ;

    int GetContextGroupId(ControllerLuaContext context);

    int SetGadgetEnableInteract(ControllerLuaContext context, int groupId, int configId, boolean enable);

    int DropSubfield(ControllerLuaContext context, LuaTable paramsTable);

    int[] GetGatherConfigIdList(ControllerLuaContext context);
}
