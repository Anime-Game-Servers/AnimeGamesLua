package org.anime_game_servers.gi_lua.models.scene;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.anime_game_servers.gi_lua.models.loader.ActivityMetaScriptLoadParams;
import org.anime_game_servers.gi_lua.models.loader.GIScriptLoader;
import org.anime_game_servers.gi_lua.models.scene.block.SceneBlock;
import org.anime_game_servers.gi_lua.models.scene.block.SceneGroupInfo;
import org.anime_game_servers.gi_lua.models.scene.group.SceneGroup;
import org.anime_game_servers.lua.engine.LuaScript;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter
public class ActivityMeta {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(ActivityMeta.class.getName());

    private int activityId;
    private SceneMeta parent;
    private List<Integer> blockIds;
    private Map<Integer, SceneBlock> blocks;
    private Map<Integer, SceneGroupInfo> groupsInfos;
    private Map<Integer, SceneGroup> groups;

    public static ActivityMeta of(SceneMeta parent, int sceneId, GIScriptLoader scriptLoader) {
        return new ActivityMeta(parent,sceneId)
                .load(scriptLoader);
    }

    private ActivityMeta(SceneMeta parent, int activityId) {
        this.activityId = activityId;
        this.parent = parent;
    }

    public ActivityMeta load(GIScriptLoader scriptLoader) {
        if(!loadActivityMeta(scriptLoader)){
            return null;
        }
        loadBlocks(scriptLoader);
        prepareGroups();
        return this;
    }

    private boolean loadActivityMeta(GIScriptLoader scriptLoader){
        val metaParams = new ActivityMetaScriptLoadParams(activityId);
        return scriptLoader.loadData(metaParams, (cs -> {
            // Create blocks
            this.blockIds = cs.getGlobalVariableList("blocks", Integer.class);
        }));
    }

    private void loadBlocks(GIScriptLoader scriptLoader){
        this.blocks = new HashMap<>(blockIds.size());
        this.groupsInfos = new HashMap<>();
        for(val blockId : blockIds){
            val block = SceneBlock.of(parent, this, blockId, scriptLoader);
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
