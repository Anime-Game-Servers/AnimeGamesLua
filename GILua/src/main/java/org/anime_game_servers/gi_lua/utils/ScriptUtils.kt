package org.anime_game_servers.gi_lua.utils

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.models.PositionImpl
import org.anime_game_servers.lua.engine.LuaEngine
import org.anime_game_servers.lua.engine.LuaTable

object ScriptUtils {
    @JvmStatic
    fun posToLua(position: Vector?, engine: LuaEngine): LuaTable {
        val result = engine.createTable()
        if (position != null) {
            result.set("x", position.getX())
            result.set("y", position.getY())
            result.set("z", position.getZ())
        } else {
            result.set("x", 0)
            result.set("y", 0)
            result.set("z", 0)
        }

        return result
    }

    fun LuaTable.toVector(): Vector {
        return luaToPos(this)
    }

    @JvmStatic
    fun luaToPos(position: LuaTable?): Vector {
        val result = PositionImpl()
        if (position != null) {
            result.apply {
                setX(position.optFloat("x", 0f))
                setY(position.optFloat("y", 0f))
                setZ(position.optFloat("z", 0f))
            }
        }

        return result
    }
}
