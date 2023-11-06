package org.anime_game_servers.gi_lua.models.scene.block;

import com.github.davidmoten.rtreemulti.geometry.Rectangle;
import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.*;
import org.anime_game_servers.gi_lua.models.PositionImpl;
import org.anime_game_servers.gi_lua.models.scene.SceneMeta;
import org.anime_game_servers.gi_lua.models.loader.GIScriptLoader;

import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Getter
public class SceneBlock {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(SceneBlock.class.getName());

    private PositionImpl max;
    private PositionImpl min;

    private Map<Integer, SceneGroupInfo> groupInfo;
    //private RTree<SceneGroupInfo, Geometry> sceneGroupIndex;

    // internal only
    private transient boolean loaded; // Not an actual variable in the scripts either
    private transient SceneMeta meta; // Not an actual variable in the scripts either
    private int sceneId;
    private int activityId;
    private int id;

    public static SceneBlock of(SceneMeta sceneMeta, int activityId, int blockId, GIScriptLoader scriptLoader) {
        val block = new SceneBlock(sceneMeta.getSceneId(), activityId, blockId, sceneMeta);
        block.load(scriptLoader);
        return block;
    }
    private SceneBlock(int sceneId, int activityId, int blockId, SceneMeta meta) {
        this.id = blockId;
        this.sceneId = sceneId;
        this.activityId = activityId;
        this.meta = meta;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public SceneBlock load(GIScriptLoader scriptLoader) {
        if (this.loaded) {
            return this;
        }
        this.setLoaded(true);
        if( !scriptLoader.loadSceneBlockScript(sceneId, id, (cs -> {
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
        meta.getGroupsInfos().putAll(this.groupInfo);
        logger.debug(() -> "Successfully loaded block " + this.id + " in scene "+sceneId+".");
        return this;
    }

    public Rectangle toRectangle() {
        return Rectangle.create(this.min.toXZDoubleArray(), this.max.toXZDoubleArray());
    }
}
