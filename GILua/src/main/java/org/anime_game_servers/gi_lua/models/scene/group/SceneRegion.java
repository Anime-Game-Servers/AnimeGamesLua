package org.anime_game_servers.gi_lua.models.scene.group;

import lombok.Getter;
import org.anime_game_servers.gi_lua.models.Position;
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
    private List<PositionImpl> point_array;
    private List<String> ability_group_list;
    private List<String> team_ability_group;
    private boolean is_trigger_reload_group = false;

    public boolean contains(Position position) {
        switch (shape) {
            case ScriptRegionShape.CUBIC:
                return (Math.abs(pos.getX() - position.getX()) <= size.getX()/2f) &&
                       (Math.abs(pos.getY() - position.getY()) <= size.getY()/2f) &&
                       (Math.abs(pos.getZ() - position.getZ()) <= size.getZ()/2f);
            case ScriptRegionShape.SPHERE:
                var x = Math.pow(pos.getX() - position.getX(), 2);
                var y = Math.pow(pos.getY() - position.getY(), 2);
                var z = Math.pow(pos.getZ() - position.getZ(), 2);
                // ^ means XOR in java!
                return x + y + z <= (radius*radius);
        }
        return false;
    }

    @Override
    public EntityType getType() {
        return EntityType.REGION;
    }
}
