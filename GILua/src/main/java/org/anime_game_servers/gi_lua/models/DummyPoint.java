package org.anime_game_servers.gi_lua.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter @EqualsAndHashCode @ToString
public class DummyPoint {
    private PositionImpl pos;
    private PositionImpl rot;
}
