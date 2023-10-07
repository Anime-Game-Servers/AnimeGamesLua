package org.anime_game_servers.gi_lua.models;

import com.github.davidmoten.rtreemulti.geometry.Point;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

public class Position implements Serializable {
    @Getter @Setter private float x;
    @Getter @Setter private float y;
    @Getter @Setter private float z;

    public Position() {}

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Position(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(List<Float> xyz) {
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

    public Point toPoint() {
        return Point.create(x,y,z);
    }

    /**
     * To XYZ array for Spatial Index
     */
    public double[] toDoubleArray() {
        return new double[]{ x, y, z};
    }
    /**
     * To XZ array for Spatial Index (Blocks)
     */
    public double[] toXZDoubleArray() {
        return new double[]{x, z};
    }
}
