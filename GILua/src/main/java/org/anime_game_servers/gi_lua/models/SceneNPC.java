package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.gi_lua.models.constants.EntityType;

@ToString
@Getter
public class SceneNPC extends SceneObject{
	private int npc_id;
    int area_id;

    @Override
    public EntityType getType() {
        return EntityType.NPC;
    }
}
