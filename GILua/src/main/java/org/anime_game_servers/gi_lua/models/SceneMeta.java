package org.anime_game_servers.gi_lua.models;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.anime_game_servers.gi_lua.models.scene.block.SceneBlock;
import org.anime_game_servers.gi_lua.models.scene.block.SceneGroupInfo;
import org.anime_game_servers.gi_lua.models.scene.group.SceneGroup;
import org.anime_game_servers.gi_lua.models.loader.GIScriptLoader;
import org.anime_game_servers.lua.engine.LuaScript;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter
public class SceneMeta {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(SceneMeta.class.getName());

    private int sceneId;
    private SceneConfig config;
    private List<Integer> blockIds;
    private Map<Integer, SceneBlock> blocks;
    private Map<Integer, SceneGroupInfo> groupsInfos;
    private Map<Integer, SceneGroup> groups;
    private Map<String, DummyPoint> dummyPoints;

    private LuaScript context;

    //private RTree<SceneBlock, Geometry> sceneBlockIndex;

    public static SceneMeta of(int sceneId, GIScriptLoader scriptLoader) {
        return new SceneMeta(sceneId).load(scriptLoader);
    }

    private SceneMeta(int sceneId) {
        this.sceneId = sceneId;
    }

    public SceneMeta load(GIScriptLoader scriptLoader) {
        if(!loadSceneMeta(scriptLoader)){
            return null;
        }
        loadDummyPoints(scriptLoader);
        loadBlocks(scriptLoader);
        prepareGroups();
        return this;
    }

    private boolean loadSceneMeta(GIScriptLoader scriptLoader){
        return scriptLoader.loadSceneMetaScript(sceneId, (cs -> {
            this.config = cs.getGlobalVariable("scene_config", SceneConfig.class);

            // TODO optimize later
            // Create blocks
            this.blockIds = cs.getGlobalVariableList("blocks", Integer.class);

            // block reckt is not really used, should we still load it?
            //val blockList = cs.getGlobalVariableList("block_rects", SceneBlock.class);
            //this.blocks = blockList.stream().collect(Collectors.toMap(b -> b.getId(), b -> b, (a, b) -> a));
            //this.sceneBlockIndex = SceneIndexManager.buildIndex(2, blockList, SceneBlock::toRectangle);
        }));
    }

    private void loadDummyPoints(GIScriptLoader scriptLoader){
        scriptLoader.loadSceneDummyPoints(sceneId, (cs -> {
            this.dummyPoints = cs.getGlobalVariableMap("dummy_points", DummyPoint.class);
        }));
    }
    private void loadBlocks(GIScriptLoader scriptLoader){
        this.blocks = new HashMap<>(blockIds.size());
        this.groupsInfos = new HashMap<>();
        for(val blockId : blockIds){
            val block = SceneBlock.of(this, 0, blockId, scriptLoader);
            this.blocks.put(blockId, block);
        }
    }

    private void prepareGroups(){
        this.groups = new HashMap<>(groupsInfos.size());
        for(val groupInfo : groupsInfos.values()){
            val group = SceneGroup.of(groupInfo);
            this.groups.put(groupInfo.getId(), group);
        }
    }

    @Nullable
    public SceneGroup getGroup(int id) {
        return groups.get(id);
    }

    @Nullable
    public SceneGroupInfo getGroupInfo(int id) {
        return groupsInfos.get(id);
    }

    @Nullable
    public SceneBlock getBlock(int id) {
        return blocks.get(id);
    }

}
