package org.anime_game_servers.gi_lua.models.quest;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

/**
 * TODO pos, alias and id can contain a placeholder in the format {var_name}
 */
@ToString
@Getter
public class QuestDataNPC {
    private int id;
    private String alias;
    @LuaNames("data_index")
    private int dataIndex;
    @LuaNames("scene_id")
    private int sceneId;
    @LuaNames("room_id")
    private int roomId;
    private String pos;
    private String script;
}
