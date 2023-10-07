package org.anime_game_servers.lua.models;

public class IntLuaValue extends MockLuaValue {
    public static final IntLuaValue ZERO = new IntLuaValue(0);
    public static final IntLuaValue ONE = new IntLuaValue(1);
    public static final IntLuaValue N_ONE = new IntLuaValue(-1);


    private final int value;

    public IntLuaValue(int value) {
        this.value = value;
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public boolean asBoolean() {
        return value != 0;
    }

    @Override
    public int asInteger() {
        return value;
    }

    @Override
    public long asLong() {
        return value;
    }

    @Override
    public double asDouble() {
        return value;
    }

    @Override
    public float asFloat() {
        return value;
    }

    @Override
    public String asString() {
        return Integer.toString(value);
    }

    @Override
    public <T> T asObject(Class<T> type) {
        if(type == Number.class)
            return type.cast(value);
        return null;
    }
}
