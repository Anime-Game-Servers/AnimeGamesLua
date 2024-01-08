package org.anime_game_servers.gi_lua.models.constants;

import org.anime_game_servers.core.base.annotations.lua.LuaStatic;

@LuaStatic
public enum GroupKillPolicy {
    GROUP_KILL_NONE,
    GROUP_KILL_ALL,
    GROUP_KILL_MONSTER,
    GROUP_KILL_GADGET,
    GROUP_KILL_NPC
}
