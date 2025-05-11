package org.anime_game_servers.gi_lua.script_lib;

import org.anime_game_servers.core.gi.models.Vector;
import org.anime_game_servers.gi_lua.models.constants.*;
import org.anime_game_servers.gi_lua.models.constants.MultistagePlayType;
import org.anime_game_servers.lua.engine.LuaTable;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("unused")
public interface ScriptLibHandler<GroupEventContext extends GroupEventLuaContext> {

    /**
     * GroupEventContext functions
     */

    void PrintGroupWarning(LuaContext context, String msg);


    // monster tide
    int AutoMonsterTide(GroupEventContext context, int tideId, int groupId, Integer[] ordersConfigId, int tideCount, int sceneLimit, int param6);
    int KillMonsterTide(GroupEventContext context, int groupId, int tideId);
    int autoPoolMonsterTide(GroupEventContext context, int index, int groupId, int[] monsterPool, int routeId, int[] routePoints, int[] monsterAffix, LuaTable monsterPoolParam);
    int clearPoolMonsterTide(GroupEventContext context, int groupId, int tideNum);
    int endMonsterTide(GroupEventContext context, int groupId, int tideIndex, int endType);
    int endPoolMonsterTide(GroupEventContext context, int groupId, int index);
    int pauseAutoMonsterTide(GroupEventContext context, int groupId, int monsterTideIndex);
    int pauseAutoPoolMonsterTide(GroupEventContext context, int groupId, int tideStage);
    int resumeAutoPoolMonsterTide(GroupEventContext context, int groupId, int tideStage);
    int continueAutoMonster(GroupEventContext context, int groupId, int tideNum);


    // time axis
    int endAllTimeAxis(GroupEventContext context);
    int continueTimeAxis(GroupEventContext context, String key);
    int pauseTimeAxis(GroupEventContext context, String key);
    int InitTimeAxis(GroupEventContext context, String var1, LuaTable var2, boolean var3);
    int EndTimeAxis(GroupEventContext context, String var1);

    // multistage play
    int endSceneMultiStagePlay(GroupEventContext context, int playIndex, boolean isSucc);
    int endSceneMultiStagePlayStage(GroupEventContext context, int playIndex, String stageName, boolean isSucc);
    int getSceneMultiStagePlayUidValue(GroupEventContext context, int groupId, int index, String name, int uid);
    int initSceneMultistagePlay(GroupEventContext context, int index, MultistagePlayType playType, LuaTable paramTable, int[] uidList);
    int setSceneMultiStagePlayUidValue(GroupEventContext context, int groupId, int index, String tag, int value);
    int setSceneMultiStagePlayValue(GroupEventContext context, int index, String tag, int value, boolean isNotify);
    int setSceneMultiStagePlayValues(GroupEventContext context, int index, LuaTable paramTable, boolean isNotify);
    int startSceneMultiStagePlayStage(GroupEventContext context, int index, int time, String key, LuaTable paramTable);
    int AddSceneMultiStagePlayUidValue(GroupEventContext context, int groupId, int param2, String param3, int uid, int param5);

    // scene play
    int createScenePlayGeneralRewardGadget(GroupEventContext context, int groupId, int configId);
    int failScenePlayBattle(GroupEventContext context, int groupId);
    int getScenePlayBattleHostUid(GroupEventContext context, int groupId);
    int getScenePlayBattleType(GroupEventContext context, int groupId);
    int getScenePlayBattleUidValue(GroupEventContext context, int groupId, int uid, String key);
    int prestartScenePlayBattle(GroupEventContext context, LuaTable sceneParam);
    int scenePlayBattleUidOp(GroupEventContext context, int groupId, int configId, int[] uidList, int buffType, String paramString, int[] paramList, int[] paramTargetList, int index, int duration);
    int setScenePlayBattlePlayTeamEntityGadgetId(GroupEventContext context, int groupId, int gadgetId);
    int setScenePlayBattleUidValue(GroupEventContext context, int groupId, int uid, String key, int value);
    int AddScenePlayBattleProgress(GroupEventContext context, int groupId, int progress);

    // regional play
    int getRegionalPlayVarValue(GroupEventContext context, int uid, int type);
    int AddRegionalPlayVarValue(GroupEventContext context, int uid, int regionId, int delta);

    // gadget play
    int getGadgetPlayProgress(GroupEventContext context, int groupId, int configId);
    int getGadgetPlayStageBeginProgress(GroupEventContext context, int groupId, int configId);
    int getGadgetPlayUidValue(GroupEventContext context, int groupId, int configId, int uid, String name);
    int setGadgetPlayUidValue(GroupEventContext context, int groupId, int configId, int uid, String key, int value);
    int startGadgetPlay(GroupEventContext context, int groupId, int configId);
    int GadgetPlayUidOp(GroupEventContext context, int groupId, int gadgetCrucibleCfgId, List<Integer> uidList, int var4, String var5, LuaTable var6 );
    int AddGadgetPlayProgress(GroupEventContext context, int param1, int param2, int progressChange);

    // gadget
    int createGadgetWave(GroupEventContext context, int areaId, int suitId, int offset, Vector boxSize, Vector gadgetSize);
    int createGadgetWithGlobalValue(GroupEventContext context, int configId, LuaTable sgv);

    // aranara collection
    int getAranaraCollectableCountByTypeAndState(GroupEventContext context, int type, int state);
    int receiveAllAranaraCollectionByType(GroupEventContext context, int groupId, int type);

    // platform
    int getPlatformArrayInfoByPointId(GroupEventContext context, int arrayId, int pointId);
    int getPlatformPointArray(GroupEventContext context, int configId);
    int setPlatformRouteIndexToNext(GroupEventContext context, int configId);
    /**
     * TODO properly implement
     * var3 might contain the next point, sometimes is a single int, sometimes multiple ints as array
     * var4 has RouteType route_type, bool turn_mode
     */
    int SetPlatformPointArray(GroupEventContext context, int entityConfigId, int pointArrayId, LuaTable var3, LuaTable var4);
    int SetPlatformRouteId(GroupEventContext context, int entityConfigId, int routeId);
    int StartPlatform(GroupEventContext context, int configId);
    int StopPlatform(GroupEventContext context, int configId);

    // scene
    int getSceneTimeSeconds(GroupEventContext context);

    // player/world
    int GetGameHour(GroupEventContext context);
    int getGameTimePassed(GroupEventContext context);
    int skipTeyvatTime(GroupEventContext context, int time, int rate);
    long GetServerTime(GroupEventContext context);
    long GetServerTimeByWeek(GroupEventContext context);


    boolean isPlayerTransmittable(GroupEventContext context, int uid);


    // giving
    /**
     * TODO implement
     * @param context
     * @param givingId The id if the giving element found in [GivingData]
     * @param groupId The groupdId of the group containing the gadget
     * @param gadgetCfgId The gadgets target configId
     * @return 0 if success, something else if failed
     */
    int ActiveGadgetItemGiving(GroupEventContext context, int givingId, int groupId, int gadgetCfgId);
    @Nullable int[] getGivingItemList(GroupEventContext context, int givingId);

    // tower
    int TowerCountTimeStatus(GroupEventContext context, int isDone, int var2);
    int TowerMirrorTeamSetUp(GroupEventContext context, int team, int var1);

    // blossom
    int CreateBlossomChestByGroupId(GroupEventContext context, int groupId, int chestConfigId);
    int GetBlossomScheduleStateByGroupId(GroupEventContext context, int groupId);
    int SetBlossomScheduleStateByGroupId(GroupEventContext context, int groupId, int state);
    int RefreshBlossomGroup(GroupEventContext context, LuaTable table);
    int RefreshBlossomDropRewardByGroupId(GroupEventContext context, int groupId);
    int AddBlossomScheduleProgressByGroupId(GroupEventContext context, int groupId);
    int GetBlossomRefreshTypeByGroupId(GroupEventContext context, int groupId);

    // widget
    int setWidgetClientDetectorCoolDown(GroupEventContext context, int configId, boolean isSucc);
    boolean isWidgetEquipped(GroupEventContext context, int hostUid, int widgetId);

    // group timer
    int CreateGroupTimerEvent(GroupEventContext context, int groupID, String source, double time);
    int CancelGroupTimerEvent(GroupEventContext context, int groupID, String source);


    // Channeller Slab
    int CreateChannellerSlabCampRewardGadget(GroupEventContext context, int configId);
    int GetChannellerSlabLoopDungeonLimitTime(GroupEventContext context);

    int GetGroupMonsterCountByGroupId(GroupEventContext context, int groupId);
    boolean checkIsInGroup(GroupEventContext context, int groupId, int configId);
    @Nullable int[]  getGroupAliveMonsterList(GroupEventContext context, int groupId);
    int getGroupLogicStateValue(GroupEventContext context, String sgvName);

    // gallery home
    int StartHomeGallery(GroupEventContext context, int galleryId, int uid);

    // gallery handball
    int SetHandballGalleryBallPosAndRot(GroupEventContext context, int galleryId, LuaTable positionTable, LuaTable rotationTable);

    // hunting
    int RefreshHuntingClueGroup(GroupEventContext context);
    int GetHuntingMonsterExtraSuiteIndexVec(GroupEventContext context);

    // expedition challenge
    int FinishExpeditionChallenge(GroupEventContext context);
    int ExpeditionChallengeEnterRegion(GroupEventContext context, boolean var1);

    // weather/climate
    int SetWeatherAreaState(GroupEventContext context, int var1, int var2);
    int EnterWeatherArea(GroupEventContext context, int weatherAreaId);
    int modifyClimatePolygonParamTable(GroupEventContext context, int one, LuaTable climateTable);




    int createFoundation(GroupEventContext context, int[] uidList, int configId, int groupId, int index);
    int createFoundations(GroupEventContext context, LuaTable foundationTable, int groupId, int index);


    int enterCurve(GroupEventContext context, int uid, int curveId, int pointId, LuaTable oceanCurrent);
    int finishRandTask(GroupEventContext context, int optionId, boolean isSucc);
    int forbidPlayerRegionVision(GroupEventContext context, int uid);
    int getChainLevel(GroupEventContext context, int chainId);
    int getOfferingLevel(GroupEventContext context, int offeringId);
    int getPlayerVehicleType(GroupEventContext context, int uid);
    @Nullable int[] getSurroundUidList(GroupEventContext context, int configId, int radius);
    int invalidGravenPhotoBundleMark(GroupEventContext context, int groupBundleId);
    int markGroupLuaAction(GroupEventContext context, String action, String transaction, LuaTable log);
    int moveAvatarByPointArrayWithTemplate(GroupEventContext context, int uid, int pointArrayId, int[] routeList, int gadgetState, LuaTable speed);
    int notifyAllPlayerPerformOperation(GroupEventContext context, int teamEntityId, int type, int effectIndex, Vector hunterPos, Vector hunterRot);

    int revertPlayerRegionVision(GroupEventContext context, int uid);
    int setChainLevel(GroupEventContext context, int chainId, int level, boolean isNotify);
    int setDarkPressureLevel(GroupEventContext context, int darkLevel);
    int setEnvironmentEffectState(GroupEventContext context, int index, String key, LuaTable floatParamTable, int[] intParam);
    int setLimitOptimization(GroupEventContext context, int uid, boolean isLimitOptimization);
    int setPlayerEyePoint(GroupEventContext context, int configId, int configId2);
    int setPlayerEyePointLOD(GroupEventContext context, int configId, int configId2, int lodLevel);
    int setPlayerInteractOption(GroupEventContext context, String key);
    int showClientTutorial(GroupEventContext context, int tutorialId, int[] uidList);
    int showCommonPlayerTips(GroupEventContext context, int type, LuaTable keys);
    int showReminderByUid(GroupEventContext context, int[] uidList, int reminderId);
    int showTemplateReminder(GroupEventContext context, int reminderId, int[] timerInfo);
    int stopFishing(GroupEventContext context, int uid);
    int stopReminder(GroupEventContext context, int reminderId);
    int switchSceneEnvAnimal(GroupEventContext context, int animalId);
    int tryRecordActivityPushTips(GroupEventContext context, int pushTipId);
    int updateStakeHomePlayRecord(GroupEventContext context, int[] uidList);


    int GetRegionEntityCount(GroupEventContext context, int regionEId, EntityType entityType);

    int GetRegionConfigId(GroupEventContext context, int regionEId);

    int GetGroupMonsterCount(GroupEventContext context);

    int SetMonsterBattleByGroup(GroupEventContext context, int configId, int groupId);

    int SetIsAllowUseSkill(GroupEventContext context, int canUse);


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

    int GetSceneOwnerUid(GroupEventContext context);
    int ShowReminder(GroupEventContext context, int reminderId);

    int[] GetSceneUidList(GroupEventContext context);
    int GetSeaLampActivityPhase(GroupEventContext context);
    int GetCurTriggerCount(GroupEventContext context);
    boolean IsPlayerAllAvatarDie(GroupEventContext context, int uid);

    int sendShowCommonTipsToClient(GroupEventContext context, String title, String content, int closeTime);

    int sendCloseCommonTipsToClient(GroupEventContext context);
    int updateBundleMarkShowStateByGroupId(GroupEventContext context, int groupId, boolean val2);



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
    int beginCameraSceneLookWithTemplate(GroupEventContext context, int var1, LuaTable camParam);


    int SetPlayerEyePointStream(GroupEventContext context, int var1, int var2, boolean var3);
    int ClearPlayerEyePoint(GroupEventContext context, int var1);

    int ShowReminderRadius(GroupEventContext context, int var1, LuaTable var2, int var3);
    int ShowClientGuide(GroupEventContext context, String guideName);



    //TODO check
    boolean CheckIsInMpMode(GroupEventContext context);
    int AssignPlayerShowTemplateReminder(GroupEventContext context, int var1, LuaTable var2);

    int RevokePlayerShowTemplateReminder(GroupEventContext context, int var1, LuaTable var2);


    ActivityOpenAndCloseTime GetActivityOpenAndCloseTimeByScheduleId(GroupEventContext context, int scheduleId);


    int AddRegionRecycleProgress(GroupEventContext context, int regionId, int delta);
    int AddRegionSearchProgress(GroupEventContext context, int regionId, int delta);

    /**
     * TODO implement
     * @param context
     * @param param1Table contains the following fields: param_index:int, param_list:Table, param_uid_list:Table,
     *                    duration:int, target_uid_list:Table
     * @return
     */
    int AssignPlayerUidOpNotify(GroupEventContext context, LuaTable param1Table);

    int CreateTreasureMapSpotRewardGadget(GroupEventContext context, int gadgetCfgId);
}
