package org.anime_game_servers.gi_lua.script_lib;

import org.anime_game_servers.core.gi.models.Vector;
import org.anime_game_servers.lua.engine.LuaTable;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("unused")
public interface ScriptLibHandler<GroupEventContext extends GroupEventLuaContext> {

    /**
     * GroupEventContext functions
     */

    void printGroupWarning(LuaContext context, String msg);

    // time axis
    int endAllTimeAxis(GroupEventContext context);
    int continueTimeAxis(GroupEventContext context, String key);
    int pauseTimeAxis(GroupEventContext context, String key);
    int initTimeAxis(GroupEventContext context, String var1, LuaTable var2, boolean var3);
    int endTimeAxis(GroupEventContext context, String var1);


    // regional play
    int getRegionalPlayVarValue(GroupEventContext context, int uid, int type);
    int addRegionalPlayVarValue(GroupEventContext context, int uid, int regionId, int delta);


    // gadget
    int createGadgetWave(GroupEventContext context, int areaId, int suitId, int offset, Vector boxSize, Vector gadgetSize);
    int createGadgetWithGlobalValue(GroupEventContext context, int configId, LuaTable sgv);

    // scene
    int getSceneTimeSeconds(GroupEventContext context);
    int[] getSceneUidList(GroupEventContext context);
    int scenePlaySound(GroupEventContext context, LuaTable soundInfoTable);


    // player/world
    int getGameHour(GroupEventContext context);
    int getGameTimePassed(GroupEventContext context);
    int skipTeyvatTime(GroupEventContext context, int time, int rate);
    long getServerTime(GroupEventContext context);
    long getServerTimeByWeek(GroupEventContext context);


    boolean checkIsInMpMode(GroupEventContext context);
    boolean isPlayerTransmittable(GroupEventContext context, int uid);
    int getSceneOwnerUid(GroupEventContext context);



    // blossom
    int createBlossomChestByGroupId(GroupEventContext context, int groupId, int chestConfigId);
    int getBlossomScheduleStateByGroupId(GroupEventContext context, int groupId);
    int setBlossomScheduleStateByGroupId(GroupEventContext context, int groupId, int state);
    int refreshBlossomGroup(GroupEventContext context, LuaTable table);
    int refreshBlossomDropRewardByGroupId(GroupEventContext context, int groupId);
    int addBlossomScheduleProgressByGroupId(GroupEventContext context, int groupId);
    int getBlossomRefreshTypeByGroupId(GroupEventContext context, int groupId);

    // widget
    int setWidgetClientDetectorCoolDown(GroupEventContext context, int configId, boolean isSucc);
    boolean isWidgetEquipped(GroupEventContext context, int hostUid, int widgetId);

    // group timer
    int createGroupTimerEvent(GroupEventContext context, int groupID, String source, double time);
    int cancelGroupTimerEvent(GroupEventContext context, int groupID, String source);


    int getGroupMonsterCountByGroupId(GroupEventContext context, int groupId);
    boolean checkIsInGroup(GroupEventContext context, int groupId, int configId);
    @Nullable int[]  getGroupAliveMonsterList(GroupEventContext context, int groupId);
    int getGroupLogicStateValue(GroupEventContext context, String sgvName);

    // gallery home
    int startHomeGallery(GroupEventContext context, int galleryId, int uid);

    // gallery handball
    int setHandballGalleryBallPosAndRot(GroupEventContext context, int galleryId, LuaTable positionTable, LuaTable rotationTable);

    // hunting
    int refreshHuntingClueGroup(GroupEventContext context);
    int getHuntingMonsterExtraSuiteIndexVec(GroupEventContext context);


    // weather/climate
    int setWeatherAreaState(GroupEventContext context, int var1, int var2);
    int enterWeatherArea(GroupEventContext context, int weatherAreaId);
    int modifyClimatePolygonParamTable(GroupEventContext context, int one, LuaTable climateTable);


    // rand task
    int finishRandTask(GroupEventContext context, int optionId, boolean isSucc);

    //Reminder
    int showReminderByUid(GroupEventContext context, int[] uidList, int reminderId);
    int showTemplateReminder(GroupEventContext context, int reminderId, int[] timerInfo);
    int stopReminder(GroupEventContext context, int reminderId);
    int showReminder(GroupEventContext context, int reminderId);
    int showReminderRadius(GroupEventContext context, int var1, LuaTable var2, int var3);
    int assignPlayerShowTemplateReminder(GroupEventContext context, int var1, LuaTable var2);
    int revokePlayerShowTemplateReminder(GroupEventContext context, int var1, LuaTable var2);
    // todo related to reminders?
    int showClientGuide(GroupEventContext context, String guideName);
    int showClientTutorial(GroupEventContext context, int tutorialId, int[] uidList);
    int showCommonPlayerTips(GroupEventContext context, int type, LuaTable keys);
    int sendShowCommonTipsToClient(GroupEventContext context, String title, String content, int closeTime);
    int sendCloseCommonTipsToClient(GroupEventContext context);



    // GadgetChainExcelConfigData
    int getChainLevel(GroupEventContext context, int uid, int chainId);
    int setChainLevel(GroupEventContext context, int chainId, int level, boolean isNotify);

    int createFoundation(GroupEventContext context, int[] uidList, int configId, int groupId, int index);
    int createFoundations(GroupEventContext context, LuaTable foundationTable, int groupId, int index);

    int revertPlayerRegionVision(GroupEventContext context, int uid);
    int forbidPlayerRegionVision(GroupEventContext context, int uid);

    int enterCurve(GroupEventContext context, int uid, int curveId, int pointId, LuaTable oceanCurrent);
    int getOfferingLevel(GroupEventContext context, int offeringId);
    @Nullable int[] getSurroundUidList(GroupEventContext context, int configId, int radius);
    int markGroupLuaAction(GroupEventContext context, String action, String transaction, LuaTable log);
    int notifyAllPlayerPerformOperation(GroupEventContext context, int teamEntityId, int type, int effectIndex, Vector hunterPos, Vector hunterRot);


    int setEnvironmentEffectState(GroupEventContext context, int index, String key, LuaTable floatParamTable, int[] intParam);
    int setLimitOptimization(GroupEventContext context, int uid, boolean isLimitOptimization);
    int setPlayerInteractOption(GroupEventContext context, String key);
    int stopFishing(GroupEventContext context, int uid);
    int switchSceneEnvAnimal(GroupEventContext context, int animalId);
    int updateStakeHomePlayRecord(GroupEventContext context, int[] uidList);



    int getGroupMonsterCount(GroupEventContext context);

    int setMonsterBattleByGroup(GroupEventContext context, int configId, int groupId);

    int setIsAllowUseSkill(GroupEventContext context, int canUse);


    /**
     * Spawn a vehicle gadget with the given parameters
     * @param uid The uid that will become the owner of the vehicle
     * @param gadgetId The gadgetId of the vehicle gadget to spawn
     * @param position The position to spawn the vehicle at
     * @param rot The rotation to spawn the vehicle with
     */
    int createVehicle(GroupEventContext context, int uid, int gadgetId, Vector position, Vector rot);


    int markPlayerAction(GroupEventContext context, int var1, int var2, int var3);

    int getCurTriggerCount(GroupEventContext context);

    int updateBundleMarkShowStateByGroupId(GroupEventContext context, int groupId, boolean val2);

    int sendServerMessageByLuaKey(GroupEventContext context, String messageKey, int[] targets);

    int tryReallocateEntityAuthority(GroupEventContext context, int uid, int endConfig, int var3);
    int forceRefreshAuthorityByConfigId(GroupEventContext context, int var1, int uid);

    int addPlayerGroupVisionType(GroupEventContext context, int[] uids, int[] visionTypeList);
    int delPlayerGroupVisionType(GroupEventContext context, int[] uids, int[] visionTypeList);
    int setPlayerGroupVisionType(GroupEventContext context, int[] uids, int[] visionTypeList);

    int moveAvatarByPointArray(GroupEventContext context, int uid, int targetId, LuaTable var3, String var4);
    int moveAvatarByPointArrayWithTemplate(GroupEventContext context, int uid, int pointArrayId, int[] routeList, int gadgetState, LuaTable speed);


    int getPlayerVehicleType(GroupEventContext context, int uid);
    boolean isPlayerAllAvatarDie(GroupEventContext context, int uid);
    int movePlayerToPos(GroupEventContext context, int[] targetUIds, Vector pos, Vector rot, int radius, boolean isSkipUi);

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
    int transPlayerToPos(GroupEventContext context, int[] targetUIds, Vector pos, Vector rot, int radius, boolean isSkipUi, int sceneId);

    int playCutScene(GroupEventContext context, int cutsceneId, int var2);
    int playCutSceneWithParam(GroupEventContext context, int cutsceneId, int var2, LuaTable var3);


    int beginCameraSceneLook(GroupEventContext context, LuaTable sceneLookParamsTable);
    int beginCameraSceneLookWithTemplate(GroupEventContext context, int var1, LuaTable camParam);


    int setPlayerEyePointStream(GroupEventContext context, int var1, int var2, boolean var3);
    int clearPlayerEyePoint(GroupEventContext context, int var1);
    int setPlayerEyePoint(GroupEventContext context, int configId, int configId2);
    int setPlayerEyePointLOD(GroupEventContext context, int configId, int configId2, int lodLevel);


    int addRegionRecycleProgress(GroupEventContext context, int regionId, int delta);
    int addRegionSearchProgress(GroupEventContext context, int regionId, int delta);

    /**
     * TODO implement
     * @param context
     * @param param1Table contains the following fields: param_index:int, param_list:Table, param_uid_list:Table,
     *                    duration:int, target_uid_list:Table
     * @return
     */
    int assignPlayerUidOpNotify(GroupEventContext context, LuaTable param1Table);
}
