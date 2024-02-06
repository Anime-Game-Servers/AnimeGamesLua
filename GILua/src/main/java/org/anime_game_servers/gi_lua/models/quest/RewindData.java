package org.anime_game_servers.gi_lua.models.quest;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class RewindData {
    private AvatarData avatar;
    private List<QuestDataNPC> npcs;

    @ToString
    @Getter
    public static class AvatarData {
        private String pos;
    }
}
