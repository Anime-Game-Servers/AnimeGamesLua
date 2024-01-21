package org.anime_game_servers.gi_lua.utils

import org.anime_game_servers.gi_lua.models.SceneGroupUserData
import org.anime_game_servers.gi_lua.models.scene.SceneGroupReplacement
import org.anime_game_servers.gi_lua.models.scene.block.SceneGroupInfo

typealias SceneGroupUserPair = Pair<SceneGroupInfo, SceneGroupUserData?>
object GroupUtils {
    @JvmStatic
    fun getReplaceableGroups(targetGroup: SceneGroupUserPair,
                             loadedGroups: Collection<SceneGroupUserPair>,
                             replacements: Map<Int, SceneGroupReplacement>): List<SceneGroupInfo> {
        val groupInfo = targetGroup.first
        val userData = targetGroup.second
        val groupID = groupInfo.id

        val replaceable = groupInfo.is_replaceable ?: return emptyList()
        val isReplaceable = userData?.isReplaceable ?: replaceable.isValue

        return replacements[groupID]?.replace_groups?.let { replaceGroups ->
            replaceGroups
                .mapNotNull { replacementId -> loadedGroups.find { it.first.id == replacementId } }
                .filter { replacementGroupPair ->
                    val replacementInfo = replacementGroupPair.first
                    val replacementUserData = replacementGroupPair.second
                    val replacementReplaceable = replacementInfo.is_replaceable ?: return@filter false
                    val replacementIsReplaceable = replacementUserData?.isReplaceable ?: replacementReplaceable.isValue
                    (replacementIsReplaceable && replacementReplaceable.version <= replaceable.version) || replacementReplaceable.isNew_bin_only
                }
                .map { it.first }
        }?: emptyList()
    }
}