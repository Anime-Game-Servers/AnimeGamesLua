package org.anime_game_servers.gi_lua.models.scene.group

import lombok.Getter
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.models.PositionImpl
import org.anime_game_servers.gi_lua.models.constants.EntityType
import org.anime_game_servers.gi_lua.models.constants.ScriptRegionShape
import kotlin.math.abs

@Getter
data class SceneRegion(
    val shape: Int = 0,

    // for CUBIC
    val size: PositionImpl? = null,

    // for SPHERE AND CYLINDER
    val radius: Int = 0,

    // for CYLINDER and POLYGON
    val height: Float = 0f,

    @field:LuaNames("point_array")
    val pointArray: List<PositionImpl>? = null,

    @field:LuaNames("ability_group_list")
    val abilityGroupList: List<String>? = null,

    @field:LuaNames("team_ability_group")
    val teamAbilityGroup: List<String>? = null,

    @field:LuaNames("is_trigger_reload_group")
    val isTriggerReloadGroup: Boolean = false,

    /**
     * seems to be linked to SceneGroupInfo.visionType
     */
    @field:LuaNames("vision_type_list")
    val visionTypeList: List<Int>? = null,
): SceneObject() {
    override val type: EntityType = REGION
    fun contains(position: Vector): Boolean {
        if(pos == null) return false
        when (shape) {
            ScriptRegionShape.SPHERE -> {
                val x = pos.getX() - position.getX()
                val y = pos.getY() - position.getY()
                val z = pos.getZ() - position.getZ()
                // x^2 + y^2 + z^2 = radius^2
                return x * x + y * y + z * z <= (radius * radius)
            }

            ScriptRegionShape.CUBIC -> {
                return (abs(pos.getX() - position.getX()) <= size!!.getX() / 2f) &&
                        (abs(pos.getY() - position.getY()) <= size.getY() / 2f) &&
                        (abs(pos.getZ() - position.getZ()) <= size.getZ() / 2f)
            }

            ScriptRegionShape.POLYGON -> {
                // algorithm is "ray casting": https://www.youtube.com/watch?v=RSXM9bgqxJM
                if (abs(pos.getY() - position.getY()) > height / 2f) return false
                var count = 0
                for (i in pointArray!!.indices) {
                    val j = (i + 1) % pointArray.size

                    val yp = position.getZ()
                    val y1 = pointArray[i].getY()
                    val y2 = pointArray[j].getY()

                    val xp = position.getX()
                    val x1 = pointArray[i].getX()
                    val x2 = pointArray[j].getX()

                    if ((yp < y1) != (yp < y2)
                        && xp < x1 + ((yp - y1) / (y2 - y1)) * (x2 - x1)
                    ) {
                        ++count
                    }
                }
                return count % 2 == 1
            }

            ScriptRegionShape.CYLINDER -> {
                if (abs(pos.getY() - position.getY()) > height / 2f) return false
                val x = pos.getX() - position.getX()
                val z = pos.getZ() - position.getZ()
                // x^2 + z^2 = radius^2
                return x * x + z * z <= radius * radius
            }
        }
        return false
    }
}
