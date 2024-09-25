package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.core.gi.models.Vector;
import org.anime_game_servers.gi_lua.models.PositionImpl;
import org.anime_game_servers.gi_lua.models.constants.EntityType;
import org.anime_game_servers.gi_lua.models.constants.ScriptRegionShape;

import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Getter
public class SceneRegion extends SceneObject{
    private int shape;
    // for CUBIC
    private PositionImpl size;
    // for SPHERE AND CYLINDER
    private int radius;
    // for CYLINDER and POLYGON
    private float height;
    @LuaNames("point_array")
    private List<PositionImpl> pointArray;
    @LuaNames("ability_group_list")
    private List<String> abilityGroupList;
    @LuaNames("team_ability_group")
    private List<String> teamAbilityGroup;
    @LuaNames("is_trigger_reload_group")
    private boolean isTriggerReloadGroup = false;
    /**
     * seems to be linked to SceneGroupInfo.visionType
     */
    @LuaNames("vision_type_list")
    private List<Integer> visionTypeList;

    public boolean contains(Vector position) {
        switch (shape) {
            case ScriptRegionShape.SPHERE -> {
                var x = pos.getX() - position.getX();
                var y = pos.getY() - position.getY();
                var z = pos.getZ() - position.getZ();
                // x^2 + y^2 + z^2 = radius^2
                return x * x + y * y + z * z <= (radius * radius);
            }
            case ScriptRegionShape.CUBIC -> {
                return (Math.abs(pos.getX() - position.getX()) <= size.getX() / 2f) &&
                       (Math.abs(pos.getY() - position.getY()) <= size.getY() / 2f) &&
                       (Math.abs(pos.getZ() - position.getZ()) <= size.getZ() / 2f);
            }
            case ScriptRegionShape.POLYGON -> {
                // algorithm is "ray casting": https://www.youtube.com/watch?v=RSXM9bgqxJM
                if (Math.abs(pos.getY() - position.getY()) > height / 2f) return false;
                var count = 0;
                for (var i = 0; i < pointArray.size(); ++i) {
                    var j = (i + 1) % pointArray.size();

                    var yp = position.getZ();
                    var y1 = pointArray.get(i).getY();
                    var y2 = pointArray.get(j).getY();

                    var xp = position.getX();
                    var x1 = pointArray.get(i).getX();
                    var x2 = pointArray.get(j).getX();

                    if ((yp < y1) != (yp < y2)
                            && xp < x1 + ((yp - y1) / (y2 - y1)) * (x2 - x1)) {
                        ++count;
                    }
                }
                return count % 2 == 1;
            }
            case ScriptRegionShape.CYLINDER -> {
                if (Math.abs(pos.getY() - position.getY()) > height / 2f) return false;
                var x = pos.getX() - position.getX();
                var z = pos.getZ() - position.getZ();
                // x^2 + z^2 = radius^2
                return x * x + z * z <= radius * radius;
            }
        }
        return false;
    }

    @Override
    public EntityType getType() {
        return EntityType.REGION;
    }
}
