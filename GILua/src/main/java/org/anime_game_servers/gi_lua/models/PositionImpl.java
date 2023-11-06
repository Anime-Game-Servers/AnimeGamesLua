package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
public class PositionImpl implements Serializable, Position {
    @Setter private float x;
    @Setter private float y;
    @Setter private float z;

    public PositionImpl() {}

    public PositionImpl(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PositionImpl(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PositionImpl(List<Float> xyz) {
        switch (xyz.size()) {
            default:  // Might want to error on excess elements, but maybe we want to extend to 3+3 representation later.
            case 3:
                this.z = xyz.get(2);  // Fall-through
            case 2:
                this.y = xyz.get(1);  // Fall-through
            case 1:
                this.x = xyz.get(0);  // pointless fall-through
            case 0:
                break;
        }
    }
}
