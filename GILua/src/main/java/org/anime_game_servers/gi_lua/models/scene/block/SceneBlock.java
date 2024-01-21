package org.anime_game_servers.gi_lua.models.scene.block;

import com.github.davidmoten.rtreemulti.geometry.Rectangle;
import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.*;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.gi_lua.models.PositionImpl;
import org.anime_game_servers.gi_lua.models.loader.SceneBlockScriptLoadParams;
import org.anime_game_servers.gi_lua.models.loader.ScriptSource;
import org.anime_game_servers.gi_lua.models.scene.ActivityMeta;
import org.anime_game_servers.gi_lua.models.scene.SceneMeta;
import org.anime_game_servers.gi_lua.models.loader.GIScriptLoader;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Getter
public class SceneBlock {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(SceneBlock.class.getName());

    private PositionImpl max;
    private PositionImpl min;

    @LuaNames("groups")
    private Map<Integer, SceneGroupInfo> groupInfo;
    //private RTree<SceneGroupInfo, Geometry> sceneGroupIndex;

    // internal only
    private transient boolean loaded; // Not an actual variable in the scripts either
    private transient SceneMeta meta; // Not an actual variable in the scripts either
    private transient ActivityMeta activityMeta; // Not an actual variable in the scripts either
    private int sceneId;
    private int activityId;
    private int id;

    public static SceneBlock of(SceneMeta sceneMeta, @Nullable ActivityMeta activityMeta, int blockId, GIScriptLoader scriptLoader) {
        val block = new SceneBlock(sceneMeta.getSceneId(), activityMeta != null ? activityMeta.getActivityId() : 0, blockId, sceneMeta, activityMeta);
        block.load(scriptLoader);
        return block;
    }
    private SceneBlock(int sceneId, int activityId, int blockId, SceneMeta meta, ActivityMeta activityMeta) {
        this.id = blockId;
        this.sceneId = sceneId;
        this.activityId = activityId;
        this.meta = meta;
        this.activityMeta = activityMeta;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public SceneBlock load(GIScriptLoader scriptLoader) {
        if (this.loaded) {
            return this;
        }
        this.setLoaded(true);

        val scriptType = activityId == 0 ? ScriptSource.SCENE : ScriptSource.ACTIVITY;
        val typeId = activityId == 0 ? sceneId : activityId;
        val sceneBlockParams = new SceneBlockScriptLoadParams(scriptType, typeId, id);
        if( !scriptLoader.loadData(sceneBlockParams, (cs -> {
            // Set groups
            this.groupInfo = cs.getGlobalVariableList("groups", SceneGroupInfo.class).stream()
                    .collect(Collectors.toMap(x -> x.getId(), y -> y, (a, b) -> a));

            this.groupInfo.values().forEach(g -> {
                g.blockId = this.id;
                g.sceneMeta = meta;
                g.activityId = activityId;
            });
            //this.sceneGroupIndex = SceneIndexManager.buildIndex(3, this.groupInfo.values(), g -> g.getPos().toPoint());
        }))){
            return null;
        }
        if(activityMeta!=null) {
            activityMeta.getGroupsInfos().putAll(this.groupInfo);
        } else {
            meta.getGroupsInfos().putAll(this.groupInfo);
        }

        logger.debug(() -> "Successfully loaded block " + this.id + " in scene "+sceneId+".");
        return this;
    }

    public Rectangle toRectangle() {
        return Rectangle.create(this.min.toXZDoubleArray(), this.max.toXZDoubleArray());
    }
}
