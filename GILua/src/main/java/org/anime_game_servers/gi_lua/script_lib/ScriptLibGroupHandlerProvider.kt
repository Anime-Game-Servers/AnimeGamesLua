package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.script_lib.handler.activity.*
import org.anime_game_servers.gi_lua.script_lib.handler.entites.AbilityScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupEntityHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupGadgetHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupMonsterHandler
import org.anime_game_servers.gi_lua.script_lib.handler.scene.ChallengeScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.scene.DungeonScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.scene.SceneStateScriptHandler

interface ScriptLibGroupHandlerProvider<GroupEventContext : GroupEventLuaContext> {
    fun getScriptLibHandler(): ScriptLibHandler<GroupEventContext>

    /* Entity handlers */
    fun getGroupAbilityHandler(): AbilityScriptHandler<GroupEventContext>? = null
    fun getGroupEntityHandler(): GroupEntityHandler<GroupEventContext>? = null
    fun getGroupGadgetHandler(): GroupGadgetHandler<GroupEventContext>? = null
    fun getGroupMonsterHandler(): GroupMonsterHandler<GroupEventContext>? = null

    /* scene handler*/
    fun getDungeonHandler(): DungeonScriptHandler<GroupEventContext>? = null
    fun getSceneStateHandler(): SceneStateScriptHandler<GroupEventContext>? = null
    fun getChallengeHandler(): ChallengeScriptHandler<GroupEventContext>? = null

    fun getGalleryScriptHandler(): GalleryScriptHandler<GroupEventContext>? = null
    /* Activity handlers */
    fun getSummerTimeScriptHandler(): SummerTimeScriptHandler<GroupEventContext>? = null
    fun getEffigyScriptHandlerHandler(): EffigyScriptHandler<GroupEventContext>? = null
    fun getFleurFairScriptHandlerHandler(): FleurFairScriptHandler<GroupEventContext>? = null
    fun getFungusFighterScriptHandlerHandler(): FungusFighterScriptHandler<GroupEventContext>? = null
    fun getCharAmusementScriptHandlerHandler(): CharAmusementScriptHandler<GroupEventContext>? = null
    fun getTreasureSeelieScriptHandlerHandler(): TreasureSeelieScriptHandler<GroupEventContext>? = null
    fun getWinterCampScriptHandlerHandler(): WinterCampScriptHandler<GroupEventContext>? = null
    fun getLanternRiteScriptHandlerHandler(): LanternRiteScriptHandler<GroupEventContext>? = null
    fun getLunaRiteScriptHandlerHandler(): LunaRiteScriptHandler<GroupEventContext>? = null



}