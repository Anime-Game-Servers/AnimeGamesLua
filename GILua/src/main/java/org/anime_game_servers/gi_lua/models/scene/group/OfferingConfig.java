package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

@Getter
public class OfferingConfig {
    @LuaNames("offering_id")
    private int offeringId;
}
