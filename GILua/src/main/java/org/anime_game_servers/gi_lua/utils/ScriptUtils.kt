package org.anime_game_servers.gi_lua.utils

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.models.PositionImpl
import org.anime_game_servers.lua.engine.LuaEngine
import org.anime_game_servers.lua.engine.LuaTable

object ScriptUtils {
    @JvmStatic
    fun posToLua(position: Vector?, engine: LuaEngine): LuaTable {
        val result = engine.createTable()
        result["x"] = position?.getX() ?: 0f
        result["y"] = position?.getY() ?: 0f
        result["z"] = position?.getZ() ?: 0f

        return result
    }

    fun Vector?.toLuaTable(engine: LuaEngine): LuaTable {
        return posToLua(this, engine)
    }
    fun LuaTable.toVector(): Vector {
        return luaToPos(this)
    }

    @JvmStatic
    fun luaToPos(position: LuaTable?): Vector {
        val result = PositionImpl()
        if (position != null) {
            result.apply {
                x = position.optFloat("x", 0f)
                y = position.optFloat("y", 0f)
                z = position.optFloat("z", 0f)
            }
        }

        return result
    }
}
