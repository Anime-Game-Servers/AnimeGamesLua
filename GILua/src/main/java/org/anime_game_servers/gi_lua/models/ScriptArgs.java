package org.anime_game_servers.gi_lua.models;

import lombok.Getter;

public class ScriptArgs {
    @Getter public int param1;
    @Getter int param2;
    @Getter int param3;
    public int source_eid; // Source entity
    public int target_eid;
    public int group_id;
    public String source; // source string, used for timers
    public int type; // lua event type, used by scripts and the ScriptManager
    @Getter public int uid; // uid of the player triggering the event

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
        this.group_id = groupId;
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

    public int getSourceEntityId() {
        return source_eid;
    }

    public ScriptArgs setSourceEntityId(int sourceEId) {
        this.source_eid = sourceEId;
        return this;
    }

    public int getTargetEntityId() {
        return target_eid;
    }

    public ScriptArgs setTargetEntityId(int targetEId) {
        this.target_eid = targetEId;
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
        return group_id;
    }

    public ScriptArgs setGroupId(int groupId) {
        this.group_id = groupId;
        return this;
    }

    public ScriptArgs setUid(int uid) {
        this.uid = uid;
        return this;
    }
}
