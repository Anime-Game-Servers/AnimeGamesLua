package org.anime_game_servers.gi_lua.models.loader

import org.anime_game_servers.lua.engine.BaseScriptLoader
import org.anime_game_servers.lua.models.ScriptType

interface SceneScriptParams : BaseScriptLoader.ScriptLoadParams {
    val scriptSource: ScriptSource
    val targetId: Int

    override fun getBaseDirectory() = if(scriptSource == ScriptSource.ACTIVITY)
        "Activity/$targetId/"
    else "Scene/$targetId/"
}
data class SceneGroupScriptLoadParams(
    override val scriptSource: ScriptSource,
    override val targetId: Int,
    val groupId: Int
) : SceneScriptParams {
    override fun getScriptName(): String =  if(scriptSource == ScriptSource.ACTIVITY)
        "activity${targetId}_group${groupId}.lua"
    else "scene${targetId}_group${groupId}.lua"
    override fun getScriptType(): ScriptType = ScriptType.EXECUTABLE
}

data class SceneBlockScriptLoadParams(
    override val scriptSource: ScriptSource,
    override val targetId: Int,
    val blockId: Int
) : SceneScriptParams {
    override fun getScriptName(): String =  if(scriptSource == ScriptSource.ACTIVITY)
        "activity${targetId}_block${blockId}.lua"
    else "scene${targetId}_block${blockId}.lua"
}

data class SceneDummyPointScriptLoadParams(
    val sceneId: Int
) : BaseScriptLoader.ScriptLoadParams {
    override fun getBaseDirectory() = "Scene/$sceneId/"
    override fun getScriptName(): String = "scene${sceneId}_dummy_points.lua"
}

data class SceneMetaScriptLoadParams(
    val sceneId: Int
) : BaseScriptLoader.ScriptLoadParams {
    override fun getBaseDirectory() = "Scene/$sceneId/"
    override fun getScriptName(): String = "scene$sceneId.lua"
}

data class ActivityMetaScriptLoadParams(
    val activityId: Int
) : BaseScriptLoader.ScriptLoadParams {
    override fun getBaseDirectory() = "Activity/$activityId/"
    override fun getScriptName(): String = "activity$activityId.lua"
}

class SceneReplacementScriptLoadParams : BaseScriptLoader.ScriptLoadParams {
    override fun getBaseDirectory() = "Scene/"
    override fun getScriptName(): String = "groups_replacement.lua"
}

data class ShardQuestScriptLoadParams(
    val questId: Int
) : BaseScriptLoader.ScriptLoadParams {
    override fun getBaseDirectory() = "Quest/Share/"
    override fun getScriptName(): String = "Q${questId}ShareConfig.lua"
}
data class GadgetScriptLoadParams(
    val gadgetScript: String
) : BaseScriptLoader.ScriptLoadParams {
    override fun getBaseDirectory() = "Gadget/"
    override fun getScriptName(): String = gadgetScript
    override fun getScriptType(): ScriptType = ScriptType.EXECUTABLE
}
data class CommonScriptLoadParams(
    val commonPath: String,
    val commonScript: String
) : BaseScriptLoader.ScriptLoadParams {
    // TODO constructor that accepts a single common parameter from lua
    override fun getBaseDirectory() = "Common/$commonPath"
    override fun getScriptName(): String = commonScript
    override fun getScriptType(): ScriptType = ScriptType.EXECUTABLE
}