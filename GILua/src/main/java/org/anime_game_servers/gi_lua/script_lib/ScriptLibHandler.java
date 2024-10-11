package org.anime_game_servers.gi_lua.script_lib;

import org.anime_game_servers.core.gi.enums.QuestState;
import org.anime_game_servers.core.gi.models.Vector;
import org.anime_game_servers.gi_lua.models.constants.*;
import org.anime_game_servers.gi_lua.models.constants.ExhibitionPlayType;
import org.anime_game_servers.gi_lua.models.constants.FlowSuiteOperatePolicy;
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreType;
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreUIType;
import org.anime_game_servers.gi_lua.script_lib.handler.parameter.KillByConfigIdParams;
import org.anime_game_servers.lua.engine.LuaTable;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public interface ScriptLibHandler<GroupEventContext extends GroupEventLuaContext> {

    /**
     * GroupEventContext functions
     */

    void PrintGroupWarning(LuaContext context, String msg);

    /**
     * Sets a gadget interacteable based on the config id and group id.
     */
    int SetGadgetEnableInteract(GroupEventContext context, int groupId, int configId, boolean enable);
    int SetWorktopOptionsByGroupId(GroupEventContext context, int groupId, int configId, LuaTable options);
    int SetWorktopOptions(GroupEventContext context, LuaTable table);

    int DelWorktopOptionByGroupId(GroupEventContext context, int groupId, int configId, int option);
    int DelWorktopOption(GroupEventContext context, int var1);

    // Some fields are guessed
    int AutoMonsterTide(GroupEventContext context, int tideId, int groupId, Integer[] ordersConfigId, int tideCount, int sceneLimit, int param6);
    int KillMonsterTide(GroupEventContext context, int groupId, int tideId);
    int GoToGroupSuite(GroupEventContext context, int groupId, int suite);

    /**
     *
     * @param context
     * @param groupId either 0 for the current group, or a group id
     * @param suite
     * @return
     */
    int AddExtraGroupSuite(GroupEventContext context, int groupId, int suite);
    int RemoveExtraGroupSuite(GroupEventContext context, int groupId, int suite);
    int KillExtraGroupSuite(GroupEventContext context, int groupId, int suite);

    int AddExtraFlowSuite(GroupEventContext context, int groupId, int suiteId, FlowSuiteOperatePolicy flowSuitePolicy);
    int RemoveExtraFlowSuite(GroupEventContext context, int groupId, int suiteId, FlowSuiteOperatePolicy flowSuitePolicy);
    int KillExtraFlowSuite(GroupEventContext context, int groupId, int suiteId, FlowSuiteOperatePolicy flowSuitePolicy);

    int ActiveChallenge(GroupEventContext context, int challengeIndex, int challengeId, int timeLimitOrGroupId, int groupId, int objectiveKills, int param5);

    int StartChallenge(GroupEventContext context, int challengeIndex, int challengeId, LuaTable challengeParams);

    int StopChallenge(GroupEventContext context, int challengeIndex, int result);

    /**
     * Adds or removed time from the challenge
     * TODO verify and implement
     * @param context
     * @param challengeId The active target challenges id
     * @param duration The duration to add or remove
     * @return 0 if success, 1 if no challenge is active, 2 if the challenge id doesn't match the active challenge,
     * 3 if modifying the duration failed
     */
    int AddChallengeDuration(GroupEventContext context, int challengeId, int duration);

    int GetGroupMonsterCountByGroupId(GroupEventContext context, int groupId);

    int CreateVariable(GroupEventContext context, String type, LuaTable value);
    int SetVariableValue(GroupEventContext context, int var1);
    int GetVariableValue(GroupEventContext context, int var1);

    int GetGroupVariableValue(GroupEventContext context, String var);

    int GetGroupVariableValueByGroup(GroupEventContext context, String name, int groupId);

    int SetGroupVariableValue(GroupEventContext context, String varName, int value);

    int SetGroupVariableValueByGroup(GroupEventContext context, String key, int value, int groupId);

    int ChangeGroupVariableValue(GroupEventContext context, String varName, int value);

    int ChangeGroupVariableValueByGroup(GroupEventContext context, String name, int value, int groupId);


    /**
     * Set the actions and triggers to designated group
     */
    int RefreshGroup(GroupEventContext context, LuaTable table);

    int GetRegionEntityCount(GroupEventContext context, int regionEId, EntityType entityType);

    int GetRegionConfigId(GroupEventContext context, int regionEId);

    int TowerCountTimeStatus(GroupEventContext context, int isDone, int var2);
    int GetGroupMonsterCount(GroupEventContext context);

    int SetMonsterBattleByGroup(GroupEventContext context, int configId, int groupId);

    int CauseDungeonFail(GroupEventContext context);
    int CauseDungeonSuccess(GroupEventContext context);

    int SetEntityServerGlobalValueByConfigId(GroupEventContext context, int cfgId, String sgvName, int value);

    int SetGroupLogicStateValue(GroupEventContext context, String sgvName, int value);

    int SetIsAllowUseSkill(GroupEventContext context, int canUse);



    int CreateMonster(GroupEventContext context, LuaTable table);

    int TowerMirrorTeamSetUp(GroupEventContext context, int team, int var1) ;

    int CreateGadget(GroupEventContext context, LuaTable table);

    /**
     * Spawn a gadget from the caller group at the specified position
     * @param configId The config id of the gadget in the calling group
     * @param pos The position to spawn the gadget at
     * @param rot The rotation of the gadget when spawned
     */
    int CreateGadgetByConfigIdByPos(GroupEventContext context, int configId, Vector pos, Vector rot);


    /**
     * TODO preparsed parameters
     * Spawns a gadget based on the caller groups gadget with cfg id matching the specified id. It also applies additional parameters based on the parameters
     * @param creationParams parameters to spawn a gadget with
     */
    int CreateGadgetByParamTable(GroupEventContext context, LuaTable creationParams);

    /**
     * Spawn a vehicle gadget with the given parameters
     * @param uid The uid that will become the owner of the vehicle
     * @param gadgetId The gadgetId of the vehicle gadget to spawn
     * @param position The position to spawn the vehicle at
     * @param rot The rotation to spawn the vehicle with
     */
    int CreateVehicle(GroupEventContext context, int uid, int gadgetId, Vector position, Vector rot);

    int CheckRemainGadgetCountByGroupId(GroupEventContext context, LuaTable table);

    int MarkPlayerAction(GroupEventContext context, int var1, int var2, int var3);
    int AddQuestProgress(GroupEventContext context, String eventNotifyName);

    int GetSceneOwnerUid(GroupEventContext context);
    @Nonnull
    QuestState GetHostQuestState(GroupEventContext context, int questId);
    @Nonnull
    QuestState GetQuestState(GroupEventContext context, int entityId, int questId);
    int ShowReminder(GroupEventContext context, int reminderId);
    int RemoveEntityByConfigId(GroupEventContext context, int groupId, EntityType entityType, int configId);
    int CreateGroupTimerEvent(GroupEventContext context, int groupID, String source, double time);
    int CancelGroupTimerEvent(GroupEventContext context, int groupID, String source);

    int GetGroupSuite(GroupEventContext context, int groupId);
    int SetGroupReplaceable(GroupEventContext context, int groupId, boolean value) ;
    int[] GetSceneUidList(GroupEventContext context);
    int GetSeaLampActivityPhase(GroupEventContext context);
    int GadgetPlayUidOp(GroupEventContext context, int groupId, int gadget_crucible, int var3, int var4, String var5, int var6 );
    long GetServerTime(GroupEventContext context);
    long GetServerTimeByWeek(GroupEventContext context);
    int GetCurTriggerCount(GroupEventContext context);
    int GetChannellerSlabLoopDungeonLimitTime(GroupEventContext context);
    boolean IsPlayerAllAvatarDie(GroupEventContext context, int sceneUid);

    int sendShowCommonTipsToClient(GroupEventContext context, String title, String content, int closeTime);

    int sendCloseCommonTipsToClient(GroupEventContext context);
    int updateBundleMarkShowStateByGroupId(GroupEventContext context, int groupId, boolean val2);
    int CreateFatherChallenge(GroupEventContext context, int challengeIndex, int challengeId, int timeLimit, LuaTable conditionTable);
    int StartFatherChallenge(GroupEventContext context, int challengeIndex);
    int ModifyFatherChallengeProperty(GroupEventContext context, int challengeId, FatherChallengeProperty propertyTypeIndex, int value);
    int SetChallengeEventMark(GroupEventContext context, int challengeId, ChallengeEventMarkType eventMarkType);
    int AttachChildChallenge(GroupEventContext context, int fatherChallengeIndex, int childChallengeIndex,
                                           int childChallengeId, LuaTable var4, LuaTable var5, LuaTable var6);
    int CreateEffigyChallengeMonster(GroupEventContext context, int var1, LuaTable var2Table);
    int GetEffigyChallengeMonsterLevel(GroupEventContext context);
    int AddTeamEntityGlobalFloatValue(GroupEventContext context, LuaTable sceneUidListTable, String var2, int var3);
    int CreateBlossomChestByGroupId(GroupEventContext context, int groupId, int chestConfigId);
    int GetBlossomScheduleStateByGroupId(GroupEventContext context, int groupId);
    int SetBlossomScheduleStateByGroupId(GroupEventContext context, int groupId, int state);
    int RefreshBlossomGroup(GroupEventContext context, LuaTable table);
    int RefreshBlossomDropRewardByGroupId(GroupEventContext context, int groupId);
    int AddBlossomScheduleProgressByGroupId(GroupEventContext context, int groupId);
    int GetBlossomRefreshTypeByGroupId(GroupEventContext context, int groupId);
    int RefreshHuntingClueGroup(GroupEventContext context);
    int GetHuntingMonsterExtraSuiteIndexVec(GroupEventContext context);
    int SetGroupTempValue(GroupEventContext context, String name, int value, LuaTable var3Table);
    int GetGroupTempValue(GroupEventContext context, String name, LuaTable var2);

    int FinishExpeditionChallenge(GroupEventContext context);
    int ExpeditionChallengeEnterRegion(GroupEventContext context, boolean var1);
    int StartSealBattle(GroupEventContext context, int gadgetId, SealBattleParams params);

    int InitTimeAxis(GroupEventContext context, String var1, LuaTable var2, boolean var3);
    int EndTimeAxis(GroupEventContext context, String var1);

    int SetTeamEntityGlobalFloatValue(GroupEventContext context, LuaTable sceneUidListTable, String var2, int var3);

    int SetTeamServerGlobalValue(GroupEventContext context, int sceneUid, String var2, int var3);

    int AddTeamServerGlobalValue(GroupEventContext context, int ownerId, String sgvName, int value);

    int GetTeamServerGlobalValue(GroupEventContext context, int ownerId, String sgvName, int value);

    int GetLanternRiteValue(GroupEventContext context);

    int CreateMonsterFaceAvatar(GroupEventContext context, LuaTable table);

    int ChangeToTargetLevelTag(GroupEventContext context, int var1);

    int AddSceneTag(GroupEventContext context, int sceneId, int sceneTagId);

    int DelSceneTag(GroupEventContext context, int sceneId, int sceneTagId);

    boolean CheckSceneTag(GroupEventContext context, int sceneId, int sceneTagId);
    int StartHomeGallery(GroupEventContext context, int galleryId, int uid);

    int StartGallery(GroupEventContext context, int galleryId);

    int StopGallery(GroupEventContext context, int galleryId, boolean var2);

    int UpdatePlayerGalleryScore(GroupEventContext context, int galleryId, LuaTable var2);
    int InitGalleryProgressScore(GroupEventContext context, String name, int galleryId, LuaTable progressTable,
                                 GalleryProgressScoreUIType scoreUiType, GalleryProgressScoreType scoreType);
    int InitGalleryProgressWithScore(GroupEventContext context, String name, int galleryId, LuaTable progress,
                                               int maxProgress, GalleryProgressScoreUIType scoreUiType, GalleryProgressScoreType scoreType);
    int AddGalleryProgressScore(GroupEventContext context, String name, int galleryId, int score);
    int GetGalleryProgressScore(GroupEventContext context, String name, int galleryId) ;
    int SetHandballGalleryBallPosAndRot(GroupEventContext context, int galleryId, LuaTable positionTable, LuaTable rotationTable);



    int SendServerMessageByLuaKey(GroupEventContext context, String messageKey, int[] targets);

    int TryReallocateEntityAuthority(GroupEventContext context, int uid, int endConfig, int var3);

    int ForceRefreshAuthorityByConfigId(GroupEventContext context, int var1, int uid);

    int AddPlayerGroupVisionType(GroupEventContext context, int[] uids, int[] visionTypeList);

    int DelPlayerGroupVisionType(GroupEventContext context, int[] uids, int[] visionTypeList);
    int SetPlayerGroupVisionType(GroupEventContext context, int[] uids, int[] visionTypeList);

    int MoveAvatarByPointArray(GroupEventContext context, int uid, int targetId, LuaTable var3, String var4);

    int MovePlayerToPos(GroupEventContext context, int[] targetUIds, Vector pos, Vector rot, int radius, boolean isSkipUi);

    /**
     * This teleports the player to another position.
     * @param context a group event lua context
     * @param targetUIds the uids of the players to teleport
     * @param pos the position to teleport to
     * @param rot the rotation to teleport with
     * @param radius The radius around the target to place the target players in. -1 if the players should be placed at the exact position
     * @param isSkipUi
     * @param sceneId if this -1, the sceneId was not set, and the teleport target is in the same Scene the target is in
     * @return
     */
    int TransPlayerToPos(GroupEventContext context, int[] targetUIds, Vector pos, Vector rot, int radius, boolean isSkipUi, int sceneId);

    int PlayCutScene(GroupEventContext context, int cutsceneId, int var2);

    int PlayCutSceneWithParam(GroupEventContext context, int cutsceneId, int var2, LuaTable var3);

    int ScenePlaySound(GroupEventContext context, LuaTable soundInfoTable);

    int BeginCameraSceneLook(GroupEventContext context, LuaTable sceneLookParamsTable);

    int SetPlayerEyePointStream(GroupEventContext context, int var1, int var2, boolean var3);
    int ClearPlayerEyePoint(GroupEventContext context, int var1);

    int ShowReminderRadius(GroupEventContext context, int var1, LuaTable var2, int var3);
    int ShowClientGuide(GroupEventContext context, String guideName);

    /**
     * Activates a dungeon checkpoint.
     * @param context a group event lua context
     * @param pointId the scene point id of the dungeon checkpoint
     * @return 0 if successful, 1 if dungeon manager is null, 2 if dungeon manager failed to activate the checkpoint
     */
    int ActivateDungeonCheckPoint(GroupEventContext context, int pointId);

    //TODO check
    int SetWeatherAreaState(GroupEventContext context, int var1, int var2);

    int EnterWeatherArea(GroupEventContext context, int weatherAreaId);

    //TODO check
    boolean CheckIsInMpMode(GroupEventContext context);

    /**
     * TODO properly implement
     * var3 might contain the next point, sometimes is a single int, sometimes multiple ints as array
     * var4 has RouteType route_type, bool turn_mode
     */
    int SetPlatformPointArray(GroupEventContext context, int entityConfigId, int pointArrayId, LuaTable var3, LuaTable var4);

    //TODO check
    int SetPlatformRouteId(GroupEventContext context, int entityConfigId, int routeId);

    //TODO check
    int StartPlatform(GroupEventContext context, int configId);

    //TODO check
    int StopPlatform(GroupEventContext context, int configId);

    int CreateChannellerSlabCampRewardGadget(GroupEventContext context, int configId);

    int AssignPlayerShowTemplateReminder(GroupEventContext context, int var1, LuaTable var2);

    int RevokePlayerShowTemplateReminder(GroupEventContext context, int var1, LuaTable var2);

    int UnlockForce(GroupEventContext context, int force);

    int LockForce(GroupEventContext context, int force);

    int KillGroupEntityByCfgIds(GroupEventContext context, int groupId, int[] monsters, int[] gadgets);
    int KillGroupEntityByPolicy(GroupEventContext context, int groupId, GroupKillPolicy policy);
    int KillEntityByConfigId(GroupEventContext context, KillByConfigIdParams params);

    int GetMonsterIdByEntityId(GroupEventContext context, int entityId);
    int GetMonsterConfigId(GroupEventContext context, int entityId);
    int GetMonsterID(GroupEventContext context, int var1);
    int GetEntityIdByConfigId(GroupEventContext context, int configId);
    int GetAvatarEntityIdByUid(GroupEventContext context, int uid);


    Vector GetPosByEntityId(GroupEventContext context, int entityId);

    Vector GetRotationByEntityId(GroupEventContext context, int entityId);

    ActivityOpenAndCloseTime GetActivityOpenAndCloseTimeByScheduleId(GroupEventContext context, int scheduleId);

    int GetGameHour(GroupEventContext context);

    int ActivateGroupLinkBundle(GroupEventContext context, int groupId);
    int ActivateGroupLinkBundleByBundleId(GroupEventContext context, int bundleId);

    /**
     * TODO implement
     * @param context
     * @param givingId The id if the giving element found in [GivingData]
     * @param groupId The groupdId of the group containing the gadget
     * @param gadgetCfgId The gadgets target configId
     * @return 0 if success, something else if failed
     */
    int ActiveGadgetItemGiving(GroupEventContext context, int givingId, int groupId, int gadgetCfgId);

    int AddChessBuildingPoints(GroupEventContext context, int groupId, int param2, int uid, int pointsToAdd);

    int AddEntityGlobalFloatValueByConfigId(GroupEventContext context, LuaTable param1Table, String param2, int param3);

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2  probably the name of the data field
     * @param param3
     * @return
     */
    int AddExhibitionAccumulableData(GroupEventContext context, int uid, String param2, int param3);

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2 probably the name of the data field
     * @param param3
     * @param exhibitionPlayType
     * @param galleryId
     * @return
     */
    int AddExhibitionAccumulableDataAfterSuccess(GroupEventContext context, int uid, String param2, int param3, ExhibitionPlayType exhibitionPlayType, int galleryId);

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2  probably the name of the data field
     * @param param3
     * @return
     */
    int AddExhibitionReplaceableData(GroupEventContext context, int uid, String param2, int param3);

    /**
     * TODO implement
     * @param context
     * @param uid
     * @param param2 probably the name of the data field
     * @param param3
     * @param param4 contains the fields "play_type" is part of the enum [ExhibitionPlayType] and "gallery_id"
     * @return
     */
    int AddExhibitionReplaceableDataAfterSuccess(GroupEventContext context, int uid, String param2, int param3, LuaTable param4);

    int AddFleurFairMultistagePlayBuffEnergy(GroupEventContext context, int groupId, int param2, int uid, int bonusId);

    int AddGadgetPlayProgress(GroupEventContext context, int param1, int param2, int progressChange);

    int AddIrodoriChessBuildingPoints(GroupEventContext context, int groupId, int param2, int points);
    int AddIrodoriChessTowerServerGlobalValue(GroupEventContext context, int groupId, int param2, int param3, int delta);
    int AddMechanicusBuildingPoints(GroupEventContext context, int groupId, int param2, int uid, int delta);

    int AddRegionRecycleProgress(GroupEventContext context, int regionId, int delta);
    int AddRegionSearchProgress(GroupEventContext context, int regionId, int delta);
    int AddRegionalPlayVarValue(GroupEventContext context, int uid, int regionId, int delta);
    int AddSceneMultiStagePlayUidValue(GroupEventContext context, int groupId, int param2, String param3, int uid, int param5);
    int AddScenePlayBattleProgress(GroupEventContext context, int groupId, int progress);

    /**
     * TODO implement
     * @param context
     * @param param1Table contains the following fields: param_index:int, param_list:Table, param_uid_list:Table,
     *                    duration:int, target_uid_list:Table
     * @return
     */
    int AssignPlayerUidOpNotify(GroupEventContext context, LuaTable param1Table);

    int CreateTreasureMapSpotRewardGadget(GroupEventContext context, int gadgetCfgId);

    /**
     * TODO better parameter handling and verify active handling
     * Calls a lua function in the specified group if the group is active. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    int ExecuteActiveGroupLua(GroupEventLuaContext context, int groupId, String functionName, LuaTable callParamsTable);

    /**
     * TODO better parameter handling
     * Calls a lua function in the specified group. The call parameters are passed to the called parameters like this:
     * [new context], [this function calls context], [call parameter 1], [call parameter 2]...
     * @param groupId group id of the group to call the function in
     * @param functionName name of the function to call
     * @param callParamsTable lua array containing the parameters to pass to the function on call
     */
    int ExecuteGroupLua(GroupEventLuaContext context, int groupId, String functionName, LuaTable callParamsTable);


}
