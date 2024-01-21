package org.anime_game_servers.gi_lua.models.scene.block;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

@ToString
@Getter
public class SceneReplaceable {
    private boolean value;
    private int version;
    @LuaNames("new_bin_only")
    private boolean newBinOnly;
}
