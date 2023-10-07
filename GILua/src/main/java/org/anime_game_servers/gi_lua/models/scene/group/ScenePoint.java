package org.anime_game_servers.gi_lua.models.scene.group;

import org.anime_game_servers.gi_lua.models.constants.EntityType;

public class ScenePoint extends SceneObject {
    private long tag;
    @Override
    public EntityType getType() {
        return EntityType.NONE;
    }
}
