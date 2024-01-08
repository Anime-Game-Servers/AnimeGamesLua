package org.anime_game_servers.gi_lua.models.constants;

import org.anime_game_servers.core.base.interfaces.IntValueEnum;

public enum GroupLoadStrategy implements IntValueEnum {
    GROUP_LOAD_NONE(0),
    GROUP_LOAD_SINGLE(1),
    GROUP_LOAD_MULTI(2),
    GROUP_LOAD_ALL(3);
    private final int value;

    private GroupLoadStrategy(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
