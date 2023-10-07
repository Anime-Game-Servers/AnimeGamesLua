package org.anime_game_servers.gi_lua.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// todo find way to deserialize from lua with final fields, maybe with the help of Builder?
public class SceneTrigger {
    /**
     * When the trigger count is set to this, it only gets unregistered on a return of != 0 from the trigger function.
     */
    public static final int INF_TRIGGERS = 0;

    private String name;
    private int config_id;
    private int event;
    private int trigger_count = 1;
    private String source;
    private String condition;
    private String action;
    private String tag;
    private String tlog_tag;
    private boolean forbid_guest;

    private transient SceneGroup currentGroup;
}
