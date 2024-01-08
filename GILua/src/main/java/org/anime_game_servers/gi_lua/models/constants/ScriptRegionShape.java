package org.anime_game_servers.gi_lua.models.constants;

import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.core.base.annotations.lua.LuaStatic;

@LuaStatic
@LuaNames(names = {"RegionShape"})
public class ScriptRegionShape {
	public static final int NONE = 0;
	public static final int SPHERE = 1;
	public static final int CUBIC = 2;
	public static final int CYLINDER = 3;
	public static final int POLYGON = 4;
}
