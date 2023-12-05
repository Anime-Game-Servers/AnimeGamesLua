package org.anime_game_servers.gi_lua.models.constants;

import kotlin.collections.MapsKt;
import org.anime_game_servers.lua.models.IntValueEnum;

import java.util.Arrays;

public enum QuestState implements IntValueEnum {
    NONE(0),
    UNSTARTED(1),
    UNFINISHED(2),
    FINISHED(3),
    FAILED(4);
    private final int value;

    private QuestState(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
