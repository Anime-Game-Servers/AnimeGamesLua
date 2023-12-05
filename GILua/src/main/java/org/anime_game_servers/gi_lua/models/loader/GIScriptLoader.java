package org.anime_game_servers.gi_lua.models.loader;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.val;
import org.anime_game_servers.gi_lua.models.constants.*;
import org.anime_game_servers.gi_lua.models.constants.ExhibitionPlayType;
import org.anime_game_servers.gi_lua.models.constants.FlowSuiteOperatePolicy;
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreType;
import org.anime_game_servers.gi_lua.models.constants.temporary.GalleryProgressScoreUIType;
import org.anime_game_servers.gi_lua.script_lib.ScriptLib;
import org.anime_game_servers.lua.engine.BaseScriptLoader;
import org.anime_game_servers.lua.engine.LuaEngine;
import org.anime_game_servers.lua.engine.LuaScript;
import org.anime_game_servers.lua.engine.ScriptConfig;
import org.anime_game_servers.lua.models.ScriptType;

import javax.script.ScriptException;
import java.lang.annotation.ElementType;
import java.nio.file.Path;

public interface GIScriptLoader extends BaseScriptLoader {
    KLogger logger = KotlinLogging.INSTANCE.logger(GIScriptLoader.class.getName());
    LuaScript getScript(ScriptSource scriptSource, int typeId, String scriptName, Path scriptPath, ScriptType scriptType);
    ScriptConfig getScriptConfig();

    default LuaScript getScript(ScriptSource scriptSource, String scriptName, Path scriptPath, ScriptType scriptType){
        if (scriptSource.getNeedsId())
            throw new IllegalArgumentException("ScriptSource "+scriptSource.name()+" needs an id");
        return getScript(scriptSource, 0, scriptName, scriptPath, scriptType);
    }

    default Path getScriptPath(ScriptSource type, String scriptName){
        if (type.getNeedsId())
            throw new IllegalArgumentException("ScriptSource "+type.name()+" needs an id");
        return getScriptPath(type, 0, scriptName);
    }

    default Path getScriptPath(ScriptSource type, int typeId, String scriptName){
        val basePath = switch (type) {
            case SCENE -> "Scene/" + typeId + "/" + scriptName;
            case SCENE_REPLACEMENT -> "Scene/" + scriptName;
            case ACTIVITY-> "Activity/" + typeId + "/" + scriptName;
            case COMMON -> "Common/" + scriptName;
            case GADGET -> "Gadget/" + scriptName;
            case SHARED_QUESTS -> "Quest/Share/" + scriptName;
        };
        return getScriptPath(basePath);
    }

    default boolean loadData(ScriptSource type, int typeId, String scriptName, ScriptType scriptType, ScriptParser parser) {
        val path = getScriptPath(type, typeId, scriptName);
        val cs = getScript(type, typeId, scriptName, path, scriptType);

        if (cs == null) {
            logger.warn(() -> "No script found for "+type.name()+" " + typeId);
            return false;
        }

        // Eval script
        try {
            cs.evaluate();

            parser.parse(cs);

        } catch (ScriptException exception) {
            logger.error(exception, () -> "An error occurred while running the script "+scriptName);
            return false;
        }
        return true;
    }

    default void addDefaultsForEngine(LuaEngine luaEngine){
        luaEngine.addGlobalEnumByIntValue("QuestState", QuestState.values());
        luaEngine.addGlobalEnumByOrdinal("EntityType", EntityType.values());
        luaEngine.addGlobalEnumByOrdinal("ElementType", ElementType.values());

        luaEngine.addGlobalEnumByOrdinal("GroupKillPolicy", GroupKillPolicy.values());
        luaEngine.addGlobalEnumByOrdinal("SealBattleType", SealBattleType.values());
        luaEngine.addGlobalEnumByOrdinal("FatherChallengeProperty", FatherChallengeProperty.values());
        luaEngine.addGlobalEnumByOrdinal("ChallengeEventMarkType", ChallengeEventMarkType.values());
        luaEngine.addGlobalEnumByOrdinal("VisionLevelType", VisionLevelType.values());
        luaEngine.addGlobalEnumByOrdinal("ExhibitionPlayType", ExhibitionPlayType.values());
        luaEngine.addGlobalEnumByOrdinal("FlowSuiteOperatePolicy", FlowSuiteOperatePolicy.values());
        luaEngine.addGlobalEnumByOrdinal("GalleryProgressScoreUIType", GalleryProgressScoreUIType.values());
        luaEngine.addGlobalEnumByOrdinal("GalleryProgressScoreType", GalleryProgressScoreType.values());

        luaEngine.addGlobalStaticClass("EventType", EventType.class);
        luaEngine.addGlobalStaticClass("GadgetState", ScriptGadgetState.class);
        luaEngine.addGlobalStaticClass("RegionShape", ScriptRegionShape.class);
        luaEngine.addGlobalStaticClass("ScriptLib", ScriptLib.class);
    }

    default boolean loadSceneReplacementScript(ScriptParser parser){
        return loadData(ScriptSource.SCENE_REPLACEMENT, 0, "groups_replacement.lua", ScriptType.DATA_STORAGE, parser);
    }

    default boolean loadSharedQuestScript(ScriptParser parser, int questId){
        return loadData(ScriptSource.SHARED_QUESTS, questId, "Q"+questId+"ShareConfig.lua", ScriptType.DATA_STORAGE, parser);
    }

    default boolean loadSceneMetaScript(int sceneId, ScriptParser parser){
        return loadData(ScriptSource.SCENE, sceneId, "scene"+sceneId+".lua", ScriptType.DATA_STORAGE, parser);
    }
    default boolean loadSceneBlockScript(int sceneId, int blockId, ScriptParser parser){
        return loadData(ScriptSource.SCENE, sceneId, "scene"+sceneId+"_block"+blockId+".lua", ScriptType.DATA_STORAGE, parser);
    }
    default boolean loadSceneGroupScript(ScriptSource scriptSource, int targetId, int groupId, ScriptParser parser){
        if(scriptSource == ScriptSource.SCENE)
            return loadData(scriptSource, targetId, "scene"+targetId+"_group"+groupId+".lua", ScriptType.EXECUTABLE, parser);
        else
            return loadData(scriptSource, targetId, "activity"+targetId+"_group"+groupId+".lua", ScriptType.EXECUTABLE, parser);
    }
    default boolean loadSceneDummyPoints(int sceneId, ScriptParser parser){
        return loadData(ScriptSource.SCENE, sceneId, "scene"+sceneId+"_dummy_points.lua", ScriptType.DATA_STORAGE, parser);
    }

    default boolean loadActivityMetaScript(int activityId, ScriptParser parser){
        return loadData(ScriptSource.ACTIVITY, activityId, "activity"+activityId+".lua", ScriptType.DATA_STORAGE, parser);
    }
    default boolean loadActivityBlockScript(int activityId, int blockId, ScriptParser parser){
        return loadData(ScriptSource.ACTIVITY, activityId, "activity"+activityId+"_block"+blockId+".lua", ScriptType.DATA_STORAGE, parser);
    }


    interface ScriptParser {
        void parse(LuaScript script) throws ScriptException;
    }

}
