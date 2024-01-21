package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.gi_lua.models.constants.EntityType;

@ToString
@Getter
public class SceneNPC extends SceneObject{
    @LuaNames("npc_id")
	private int npc_id;

    @Override
    public EntityType getType() {
        return EntityType.NPC;
    }
}
