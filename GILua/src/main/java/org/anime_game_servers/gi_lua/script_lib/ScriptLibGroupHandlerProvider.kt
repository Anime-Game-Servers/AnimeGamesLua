package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.script_lib.handler.activity.*
import org.anime_game_servers.gi_lua.script_lib.handler.entites.AbilityScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupEntityHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupGadgetHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupMonsterHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupRegionScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.player.ExhibitionScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.player.QuestScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.scene.*

interface ScriptLibGroupHandlerProvider<GroupEventContext : GroupEventLuaContext> {
    fun getScriptLibHandler(): ScriptLibHandler<GroupEventContext>

    /* Entity handlers */
    fun getGroupAbilityHandler(): AbilityScriptHandler<GroupEventContext>? = null
    fun getGroupEntityHandler(): GroupEntityHandler<GroupEventContext>? = null
    fun getGroupGadgetHandler(): GroupGadgetHandler<GroupEventContext>? = null
    fun getGroupMonsterHandler(): GroupMonsterHandler<GroupEventContext>? = null
    fun getGroupRegionHandler(): GroupRegionScriptHandler<GroupEventContext>? = null

    /* player handler */
    fun getQuestHandler(): QuestScriptHandler<GroupEventContext>? = null
    fun getExhibitionHandler(): ExhibitionScriptHandler<GroupEventContext>? = null

    /* scene handler*/
    fun getChallengeHandler(): ChallengeScriptHandler<GroupEventContext>? = null
    fun getDungeonHandler(): DungeonScriptHandler<GroupEventContext>? = null
    fun getDeathZoneHandler(): DeathZoneScriptHandler<GroupEventContext>? = null
    fun getGalleryHandler(): GalleryScriptHandler<GroupEventContext>? = null
    fun getGroupManagementHandler(): GroupManagementScriptHandler<GroupEventContext>? = null
    fun getMonsterTideHandler(): MonsterTideScriptHandler<GroupEventContext>? = null
    fun getScenePlayHandler(): ScenePlayScriptHandler<GroupEventContext>? = null
    fun getSceneStateHandler(): SceneStateScriptHandler<GroupEventContext>? = null
    fun getSealBattleHandler(): SealBattleScriptHandler<GroupEventContext>? = null

    /* Activity handlers */
    fun getActivityHandler(): GeneralActivityScriptHandler<GroupEventContext>? = null
    fun getAsterHandler(): AsterScriptHandler<GroupEventContext>? = null
    fun getChannelerSlabHandler(): ChannelerSlabScriptHandler<GroupEventContext>? = null
    fun getCharAmusementHandler(): CharAmusementScriptHandler<GroupEventContext>? = null
    fun getChessHandler(): ChessScriptHandler<GroupEventContext>? = null
    fun getCoinCollectHandler(): CoinCollectScriptHandler<GroupEventContext>? = null
    fun getCrystalLinkHandler(): CrystalLinkScriptHandler<GroupEventContext>? = null
    fun getDigHandler(): DigScriptHandler<GroupEventContext>? = null
    fun getEffigyHandler(): EffigyScriptHandler<GroupEventContext>? = null
    fun getExpeditionHandler(): ExpeditionScriptHandler<GroupEventContext>? = null
    fun getFleurFairHandler(): FleurFairScriptHandler<GroupEventContext>? = null
    fun getFungusFighterHandler(): FungusFighterScriptHandler<GroupEventContext>? = null
    fun getGravenInnocenceHandler(): GravenInnocenceScriptHandler<GroupEventContext>? = null
    fun getHideAndSeekHandler(): HideAndSeekScriptHandler<GroupEventContext>? = null
    fun getInstableSprayHandler(): InstableSprayScriptHandler<GroupEventContext>? = null
    fun getIrodoriChessHandler(): IrodoriChessScriptHandler<GroupEventContext>? = null
    fun getLanternRiteHandler(): LanternRiteScriptHandler<GroupEventContext>? = null
    fun getLuminanceStoneChallengeHandler(): LuminanceStoneChallengeScriptHandler<GroupEventContext>? = null
    fun getLunaRiteHandler(): LunaRiteScriptHandler<GroupEventContext>? = null
    fun getMechanicusHandler(): MechanicusScriptHandler<GroupEventContext>? = null
    fun getMichiaeMatsuriHandler(): MichiaeMatsuriScriptHandler<GroupEventContext>? = null
    fun getMistTrialHandler(): MistTrialScriptHandler<GroupEventContext>? = null
    fun getPotionHandler(): PotionScriptHandler<GroupEventContext>? = null
    fun getRogueDiaryHandler(): RogueDiaryScriptHandler<GroupEventContext>? = null
    fun getRoguelikeHandler(): RoguelikeScriptHandler<GroupEventContext>? = null
    fun getSeaLampHandler(): SeaLampScriptHandler<GroupEventContext>? = null
    fun getSummerTimeHandler(): SummerTimeScriptHandler<GroupEventContext>? = null
    fun getTreasureMapHandler(): TreasureMapScriptHandler<GroupEventContext>? = null
    fun getTreasureSeelieHandler(): TreasureSeelieScriptHandler<GroupEventContext>? = null
    fun getUgcDungeonHandler(): UgcDungeonScriptHandler<GroupEventContext>? = null
    fun getVintageHandler(): VintageScriptHandler<GroupEventContext>? = null
    fun getWinterCampHandler(): WinterCampScriptHandler<GroupEventContext>? = null



}