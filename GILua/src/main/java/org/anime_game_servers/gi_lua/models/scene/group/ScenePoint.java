package org.anime_game_servers.gi_lua.models.scene.group;

import org.anime_game_servers.gi_lua.models.constants.EntityType;
import org.anime_game_servers.gi_lua.models.SceneObject;

public class ScenePoint extends SceneObject {
    private long tag;
    @Override
    public EntityType getType() {
        return EntityType.NONE;
    }
}
