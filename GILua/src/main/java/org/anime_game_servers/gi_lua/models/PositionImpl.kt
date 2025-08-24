package org.anime_game_servers.gi_lua.models

import java.io.Serializable


data class PositionImpl @JvmOverloads constructor(
    @JvmField var x: Float = 0f,
    @JvmField var y: Float = 0f,
    @JvmField var z:Float = 0f)
    : Serializable, Position {

    override fun getX() = x
    override fun getY() = y
    override fun getZ() = z

    constructor(xyz: MutableList<Float?>) : this(
        xyz.getOrNull(0)?: 0f,
        xyz.getOrNull(1)?: 0f,
        xyz.getOrNull(2)?: 0f
    )
}
