package org.anime_game_servers.gi_lua.models.scene.block;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

@ToString
@Getter
public class SceneBusiness {
	private int type;
	@LuaNames("sub_type")
	private int subType;
}
