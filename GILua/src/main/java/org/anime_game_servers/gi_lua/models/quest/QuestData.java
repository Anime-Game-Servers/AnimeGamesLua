package org.anime_game_servers.gi_lua.models.quest;

import lombok.Getter;
import lombok.ToString;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

import java.util.List;

@Getter @ToString
public class QuestData {
    @LuaNames("transmit_points")
    private List<TransmitPoint> transmitPoints;
    private List<QuestDataNPC> npcs;
    private List<Gadget> gadgets;

    @Getter @ToString
    public static class TransmitPoint {
        @LuaNames("point_id")
        private int pointId;
        @LuaNames("scene_id")
        private int sceneId;
        private String pos;
    }

    @Getter @ToString
    public static class Gadget {
        private int id;
        private String pos;
    }
}
