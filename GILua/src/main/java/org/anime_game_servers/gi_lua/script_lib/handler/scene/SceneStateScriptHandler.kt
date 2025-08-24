package org.anime_game_servers.gi_lua.script_lib.handler.scene

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.utils.ScriptUtils.toVector
import org.anime_game_servers.lua.engine.LuaTable

data class ChangeLevelTagParams(
    var pos: Vector,
    var rot: Vector,
    var radius: Int = -1
) {
    companion object {
        fun fromLuaTable(table: LuaTable): ChangeLevelTagParams? {
            val pos = table.getTable("pos")?.toVector() ?: return null
            val rot = table.getTable("rot")?.toVector() ?: return null
            val radius = table.optInt("radius", -1)
            return ChangeLevelTagParams(pos, rot, radius)
        }
    }
}

/**
 * Handler for scriptlib functions used in
 * These are only callable from a lua group context.
 */
interface SceneStateScriptHandler<GroupEventContext : GroupEventLuaContext> {

    /* Scene Point handling*/
    fun unhideScenePoint(context: GroupEventContext, scenePointId: Int): Int
    fun unlockScenePoint(context: GroupEventContext, scenePointId: Int): Int

    /** force handling */
    fun unlockForce(context: GroupEventContext, force: Int): Int
    fun lockForce(context: GroupEventContext, force: Int): Int

    /* scene tag */
    fun addSceneTag(context: GroupEventContext, sceneId: Int, sceneTagId: Int): Int
    fun delSceneTag(context: GroupEventContext, sceneId: Int, sceneTagId: Int): Int
    fun checkSceneTag(context: GroupEventContext, sceneId: Int, sceneTagId: Int): Boolean

    /* level tag handling */
    fun changeToTargetLevelTag(context: GroupEventContext, levelTagId: Int): Int
    fun changeToTargetLevelTagWithParamTable(
        context: GroupEventContext,
        levelTagId: Int,
        params: ChangeLevelTagParams
    ): Int

    fun getCurrentLevelTagVec(context: GroupEventContext, levelTagGroupId: Int): List<Int>
    fun getLevelTagNameById(context: GroupEventContext, levelTagId: Int): String
    fun isLevelTagChangeInCD(context: GroupEventContext, levelTagGroupId: Int): Boolean

    /* misc */
    fun getSceneUidList(context: GroupEventContext): IntArray
    fun getSceneOwnerUid(context: GroupEventContext): Int
    fun checkIsInMpMode(context: GroupEventContext): Boolean
}