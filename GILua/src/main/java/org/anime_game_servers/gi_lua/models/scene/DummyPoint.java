package org.anime_game_servers.gi_lua.models.scene;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.gi_lua.models.PositionImpl;

@Getter @EqualsAndHashCode @ToString
public class DummyPoint {
    private PositionImpl pos;
    private PositionImpl rot;
}
