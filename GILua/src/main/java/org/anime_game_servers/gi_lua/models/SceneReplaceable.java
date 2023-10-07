package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class SceneReplaceable {
    // TODO move to modifiable value in SceneGroupInstance
    @Setter
    private boolean value;
    private int version;
    private boolean new_bin_only;
}
