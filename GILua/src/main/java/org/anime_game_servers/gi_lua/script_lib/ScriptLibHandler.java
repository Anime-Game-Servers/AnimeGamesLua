package org.anime_game_servers.gi_lua.script_lib;

import org.anime_game_servers.core.gi.models.Vector;
import org.anime_game_servers.gi_lua.models.constants.*;
import org.anime_game_servers.gi_lua.models.constants.ExhibitionPlayType;
import org.anime_game_servers.lua.engine.LuaTable;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("unused")
public interface ScriptLibHandler<GroupEventContext extends GroupEventLuaContext> {

    /**
     * GroupEventContext functions
     */

    void PrintGroupWarning(LuaContext context, String msg);



    // Some fields are guessed
    int AutoMonsterTide(GroupEventContext context, int tideId, int groupId, Integer[] ordersConfigId, int tideCount, int sceneLimit, int param6);
    int KillMonsterTide(GroupEventContext context, int groupId, int tideId);

    int GetGroupMonsterCountByGroupId(GroupEventContext context, int groupId);





    int GetRegionEntityCount(GroupEventContext context, int regionEId, EntityType entityType);

    int GetRegionConfigId(GroupEventContext context, int regionEId);

    int TowerCountTimeStatus(GroupEventContext context, int isDone, int var2);
    int GetGroupMonsterCount(GroupEventContext context);

    int SetMonsterBattleByGroup(GroupEventContext context, int configId, int groupId);

    int SetIsAllowUseSkill(GroupEventContext context, int canUse);

    int TowerMirrorTeamSetUp(GroupEventContext context, int team, int var1) ;

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
    int CreateGroupTimerEvent(GroupEventContext context, int groupID, String source, double time);
    int CancelGroupTimerEvent(GroupEventContext context, int groupID, String source);

    int[] GetSceneUidList(GroupEventContext context);
    int GetSeaLampActivityPhase(GroupEventContext context);
    int GadgetPlayUidOp(GroupEventContext context, int groupId, int gadgetCrucibleCfgId, List<Integer> uidList, int var4, String var5, LuaTable var6 );
    long GetServerTime(GroupEventContext context);
    long GetServerTimeByWeek(GroupEventContext context);
    int GetCurTriggerCount(GroupEventContext context);
    int GetChannellerSlabLoopDungeonLimitTime(GroupEventContext context);
    boolean IsPlayerAllAvatarDie(GroupEventContext context, int uid);

    int sendShowCommonTipsToClient(GroupEventContext context, String title, String content, int closeTime);

    int sendCloseCommonTipsToClient(GroupEventContext context);
    int updateBundleMarkShowStateByGroupId(GroupEventContext context, int groupId, boolean val2);



    int CreateBlossomChestByGroupId(GroupEventContext context, int groupId, int chestConfigId);
    int GetBlossomScheduleStateByGroupId(GroupEventContext context, int groupId);
    int SetBlossomScheduleStateByGroupId(GroupEventContext context, int groupId, int state);
    int RefreshBlossomGroup(GroupEventContext context, LuaTable table);
    int RefreshBlossomDropRewardByGroupId(GroupEventContext context, int groupId);
    int AddBlossomScheduleProgressByGroupId(GroupEventContext context, int groupId);
    int GetBlossomRefreshTypeByGroupId(GroupEventContext context, int groupId);
    int RefreshHuntingClueGroup(GroupEventContext context);
    int GetHuntingMonsterExtraSuiteIndexVec(GroupEventContext context);

    int FinishExpeditionChallenge(GroupEventContext context);
    int ExpeditionChallengeEnterRegion(GroupEventContext context, boolean var1);

    int InitTimeAxis(GroupEventContext context, String var1, LuaTable var2, boolean var3);
    int EndTimeAxis(GroupEventContext context, String var1);


    int StartHomeGallery(GroupEventContext context, int galleryId, int uid);
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





    ActivityOpenAndCloseTime GetActivityOpenAndCloseTimeByScheduleId(GroupEventContext context, int scheduleId);

    int GetGameHour(GroupEventContext context);

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


}
