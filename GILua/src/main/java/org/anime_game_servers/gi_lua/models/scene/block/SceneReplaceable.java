package org.anime_game_servers.gi_lua.models.scene.block;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class SceneReplaceable {
    private boolean value;
    private int version;
    private boolean new_bin_only;
}
