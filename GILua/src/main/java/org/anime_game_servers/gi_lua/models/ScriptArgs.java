package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;

@Accessors(chain = true)
public class ScriptArgs {
    @Getter public int param1;
    @Getter public int param2;
    @Getter public int param3;
    @Getter public int param4;
    
    @LuaNames("param_str1")
    @Getter public String paramString1;
    @LuaNames("source_eid")
    public int sourceEid; // Source entity
    @LuaNames("target_eid")
    public int targetEid;
    @LuaNames("group_id")
    public int groupId;
    @LuaNames("source_name")
    public String source; // source string, used for timers
    public int type; // lua event type, used by scripts and the ScriptManager
    @Setter @Getter public int uid; // uid of the player triggering the event

    public ScriptArgs(int groupId, int eventType) {
        this(groupId, eventType, 0,0);
    }

    public ScriptArgs(int groupId, int eventType, int param1) {
        this(groupId, eventType, param1,0);
    }

    public ScriptArgs(int groupId, int eventType, int param1, int param2) {
        this.type = eventType;
        this.param1 = param1;
        this.param2 = param2;
        this.groupId = groupId;
    }

    public ScriptArgs setParam1(int param1) {
        this.param1 = param1;
        return this;
    }

    public ScriptArgs setParam2(int param2) {
        this.param2 = param2;
        return this;
    }

    public ScriptArgs setParam3(int param3) {
        this.param3 = param3;
        return this;
    }

    public ScriptArgs setParam4(int param4) {
        this.param4 = param4;
        return this;
    }

    public int getSourceEntityId() {
        return sourceEid;
    }

    public ScriptArgs setSourceEntityId(int sourceEId) {
        this.sourceEid = sourceEId;
        return this;
    }

    public int getTargetEntityId() {
        return targetEid;
    }

    public ScriptArgs setTargetEntityId(int targetEId) {
        this.targetEid = targetEId;
        return this;
    }

    public String getEventSource() {
        return source;
    }

    public ScriptArgs setEventSource(String source) {
        this.source = source;
        return this;
    }

    public ScriptArgs setEventSource(int source) {
        return setEventSource(Integer.toString(source));
    }

    public int getGroupId() {
        return groupId;
    }

    public ScriptArgs setGroupId(int groupId) {
        this.groupId = groupId;
        return this;
    }
}
