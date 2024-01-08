package org.anime_game_servers.gi_lua.models;

import com.github.davidmoten.rtreemulti.geometry.Point;
import org.anime_game_servers.core.gi.models.Vector;

public interface Position extends Vector {
    float getX();
    float getY();
    float getZ();


    default Point toPoint() {
        return Point.create(getX(),getY(),getZ());
    }

    /**
     * To XYZ array for Spatial Index
     */
    default double[] toDoubleArray() {
        return new double[]{ getX(),getY(),getZ()};
    }
    /**
     * To XZ array for Spatial Index (Blocks)
     */
    default double[] toXZDoubleArray() {
        return new double[]{getX(),getZ()};
    }
}
