package org.anime_game_servers.gi_lua.models.scene.block;

import com.github.davidmoten.rtreemulti.RTree;
import com.github.davidmoten.rtreemulti.geometry.Geometry;
import com.github.davidmoten.rtreemulti.geometry.Rectangle;
import lombok.*;
import org.anime_game_servers.gi_lua.SceneIndexManager;
import org.anime_game_servers.gi_lua.models.Position;
import org.anime_game_servers.gi_lua.models.scene.group.SceneGroup;
import org.anime_game_servers.gi_lua.utils.GIScriptLoader;
import org.anime_game_servers.lua.models.ScriptType;

import javax.script.ScriptException;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Getter
public class SceneBlock {
    @Setter(AccessLevel.PUBLIC)
    private int id;
    private Position max;
    private Position min;

    private int sceneId;
    private Map<Integer, SceneGroup> groups;
    private RTree<SceneGroup, Geometry> sceneGroupIndex;

    private transient boolean loaded; // Not an actual variable in the scripts either

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public SceneBlock load(int sceneId, GIScriptLoader scriptLoader) {
        if (this.loaded) {
            return this;
        }
        this.sceneId = sceneId;
        this.setLoaded(true);

        val cs = scriptLoader.getSceneScript(sceneId, "scene" + sceneId + "_block" + this.id + ".lua", ScriptType.DATA_STORAGE);

        if (cs == null) {
            return null;
        }

        // Eval script
        try {
            cs.evaluate();

            // Set groups
            this.groups = cs.getGlobalVariableList("groups", SceneGroup.class).stream()
                    .collect(Collectors.toMap(x -> x.getId(), y -> y, (a, b) -> a));

            this.groups.values().forEach(g -> g.block_id = this.id);
            this.sceneGroupIndex = SceneIndexManager.buildIndex(3, this.groups.values(), g -> g.getPos().toPoint());
        } catch (ScriptException exception) {
            //Grasscutter.getLogger().error("An error occurred while loading block " + this.id + " in scene " + sceneId, exception);
        }
        //Grasscutter.getLogger().debug("Successfully loaded block {} in scene {}.", this.id, sceneId);
        return this;
    }

    public Rectangle toRectangle() {
        return Rectangle.create(this.min.toXZDoubleArray(), this.max.toXZDoubleArray());
    }
}
