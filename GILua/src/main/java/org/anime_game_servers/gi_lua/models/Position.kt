package org.anime_game_servers.gi_lua.models

import com.github.davidmoten.rtreemulti.geometry.Point
import org.anime_game_servers.core.gi.models.Vector

interface Position : Vector {
    override fun getX(): Float
    override fun getY(): Float
    override fun getZ(): Float


    fun toPoint(): Point {
        return Point.create(getX().toDouble(), getY().toDouble(), getZ().toDouble())
    }

    /**
     * To XYZ array for Spatial Index
     */
    override fun toDoubleArray(): DoubleArray {
        return doubleArrayOf(getX().toDouble(), getY().toDouble(), getZ().toDouble())
    }

    /**
     * To XZ array for Spatial Index (Blocks)
     */
    override fun toXZDoubleArray(): DoubleArray {
        return doubleArrayOf(getX().toDouble(), getZ().toDouble())
    }
}
