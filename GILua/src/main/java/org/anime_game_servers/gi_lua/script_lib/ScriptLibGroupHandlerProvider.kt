package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.script_lib.handler.activity.*
import org.anime_game_servers.gi_lua.script_lib.handler.entites.AbilityScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupEntityHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupGadgetHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupMonsterHandler
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

    /* player handler */
    fun getQuestHandler(): QuestScriptHandler<GroupEventContext>? = null
    fun getExhibitionHandler(): ExhibitionScriptHandler<GroupEventContext>? = null

    /* scene handler*/
    fun getChallengeHandler(): ChallengeScriptHandler<GroupEventContext>? = null
    fun getDungeonHandler(): DungeonScriptHandler<GroupEventContext>? = null
    fun getDeathZoneHandler(): DeathZoneScriptHandler<GroupEventContext>? = null
    fun getGalleryScriptHandler(): GalleryScriptHandler<GroupEventContext>? = null
    fun getGroupManagementHandler(): GroupManagementScriptHandler<GroupEventContext>? = null
    fun getSceneStateHandler(): SceneStateScriptHandler<GroupEventContext>? = null
    fun getSealBattleHandler(): SealBattleScriptHandler<GroupEventContext>? = null

    /* Activity handlers */
    fun getAsterScriptHandlerHandler(): AsterScriptHandler<GroupEventContext>? = null
    fun getCharAmusementScriptHandlerHandler(): CharAmusementScriptHandler<GroupEventContext>? = null
    fun getChessScriptHandlerHandler(): ChessScriptHandler<GroupEventContext>? = null
    fun getEffigyScriptHandlerHandler(): EffigyScriptHandler<GroupEventContext>? = null
    fun getFleurFairScriptHandlerHandler(): FleurFairScriptHandler<GroupEventContext>? = null
    fun getFungusFighterScriptHandlerHandler(): FungusFighterScriptHandler<GroupEventContext>? = null
    fun getHideAndSeekScriptHandlerHandler(): HideAndSeekScriptHandler<GroupEventContext>? = null
    fun getIrodoriChessScriptHandlerHandler(): IrodoriChessScriptHandler<GroupEventContext>? = null
    fun getLanternRiteScriptHandlerHandler(): LanternRiteScriptHandler<GroupEventContext>? = null
    fun getLunaRiteScriptHandlerHandler(): LunaRiteScriptHandler<GroupEventContext>? = null
    fun getMechanicusScriptHandlerHandler(): MechanicusScriptHandler<GroupEventContext>? = null
    fun getSummerTimeScriptHandler(): SummerTimeScriptHandler<GroupEventContext>? = null
    fun getTreasureSeelieScriptHandlerHandler(): TreasureSeelieScriptHandler<GroupEventContext>? = null
    fun getVintageScriptHandlerHandler(): VintageScriptHandler<GroupEventContext>? = null
    fun getWinterCampScriptHandlerHandler(): WinterCampScriptHandler<GroupEventContext>? = null



}