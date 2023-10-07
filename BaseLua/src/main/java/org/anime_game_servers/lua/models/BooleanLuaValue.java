package org.anime_game_servers.lua.models;

public class BooleanLuaValue extends MockLuaValue {
    public static final BooleanLuaValue TRUE = new BooleanLuaValue(true);
    public static final BooleanLuaValue FALSE = new BooleanLuaValue(false);
    private final boolean value;
    public BooleanLuaValue(boolean value){
        this.value = value;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean asBoolean() {
        return value;
    }

    @Override
    public int asInteger() {
        return value ? 1 : 0;
    }

    @Override
    public long asLong() {
        return value ? 1 : 0;
    }

    @Override
    public double asDouble() {
        return value ? 1 : 0;
    }

    @Override
    public float asFloat() {
        return value ? 1 : 0;
    }

    @Override
    public String asString() {
        return value ? "true" : "false";
    }

    @Override
    public <T> T asObject(Class<T> type) {
        if(type == Boolean.class)
            return type.cast(value);
        return null;
    }
}
