package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreType
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreUIType
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.lua.engine.LuaTable

/**
 * This handles script lib functions that are specific to the gallery system used in mini games.
 * These are only callable from a group context.
 */
interface GalleryScriptHandler<GroupEventContext : GroupEventLuaContext> {
    fun getGalleryTransaction(context: GroupEventContext, galleryId: Int): Int
    fun getGalleryUidList(context: GroupEventContext, galleryId: Int): List<Int>
    fun isGalleryStart(context: GroupEventContext, galleryId: Int): Boolean
    fun setGalleryRevivePoint(context: GroupEventContext, galleryId: Int, groupId: Int, pointId: Int): Int
    fun setPlayerStartGallery(context: GroupEventContext, galleryId: Int, uidList: List<Int>): Int

    fun startGallery(context: GroupEventContext, galleryId: Int): Int

    fun stopGallery(context: GroupEventContext, galleryId: Int, isFailed: Boolean): Int
    fun stopGalleryByReason(context: GroupEventContext, galleryId: Int, stopReason: Int): Int

    fun updatePlayerGalleryScore(context: GroupEventContext, galleryId: Int, params: LuaTable): Int

    // progress
    fun initGalleryProgressScore(
        context: GroupEventContext, name: String, galleryId: Int, progress: List<Int>,
        scoreUiType: GalleryProgressScoreUIType, scoreType: GalleryProgressScoreType
    ): Int

    fun initGalleryProgressWithScore(
        context: GroupEventContext, name: String, galleryId: Int, progress: List<Int>,
        maxProgress: Int, scoreUiType: GalleryProgressScoreUIType, scoreType: GalleryProgressScoreType
    ): Int

    fun getGalleryProgressScore(context: GroupEventContext, name: String, galleryId: Int): Int

    fun addGalleryProgressScore(context: GroupEventContext, name: String, galleryId: Int, score: Int): Int


    // gallery ability
    fun attachGalleryAbilityGroup(context: GroupEventContext, uidList: List<Int>, galleryId: Int, abilityGroupIndex: Int): Int
    fun attachGalleryTeamAbilityGroup(context: GroupEventContext, uidList: List<Int>, galleryId: Int, abilityGroupIndex: Int): Int
    fun delGalleryAbilityGroup(context: GroupEventContext, uidList: List<Int>, galleryId: Int, abilityGroupIndex: Int): Int

    // home gallery
    fun startHomeGallery(context: GroupEventContext, galleryId: Int, uid: Int): Int
    fun updateStakeHomePlayRecord(context: GroupEventContext, uidList: IntArray): Int

    // handball
    // gallery home
    fun setHandballGalleryBallPosAndRot(
        context: GroupEventContext,
        galleryId: Int,
        positionTable: Vector,
        rotationTable: Vector
    ): Int

}
