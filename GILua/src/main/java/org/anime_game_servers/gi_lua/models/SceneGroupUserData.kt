package org.anime_game_servers.gi_lua.models

import org.anime_game_servers.gi_lua.models.scene.block.SceneGroupInfo

interface SceneGroupUserData {
    val groupId: Int
    var isReplaceable: Boolean?

    fun initWithSceneGroup(groupInfo: SceneGroupInfo) {
        isReplaceable = groupInfo.getIsReplaceable()?.isValue
    }
}