package org.anime_game_servers.luaj_engine;

import lombok.val;
import org.anime_game_servers.lua.engine.LuaTable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;

public class LuaJTable implements LuaTable {
    private final org.luaj.vm2.LuaTable table;
    private final LuaJEngine engine;

    LuaJTable(LuaJEngine engine, org.luaj.vm2.LuaTable table) {
        this.table = table;
        this.engine = engine;
    }

    @Override
    public boolean has(String key) {
        return !table.get(key).isnil();
    }

    @Override
    public Object get(String key) {
        return new LuaJValue(engine, table.get(key));
    }

    @Override
    public Object get(int key) {
        return new LuaJValue(engine, table.get(key));
    }

    @Override
    public int getInt(String key) {
        return table.get(key).checkint();
    }

    @Override
    public int optInt(String key, int defaultValue) {
        return table.get(key).optint(defaultValue);
    }

    @Override
    public int getInt(int key) {
        return table.get(key).checkint();
    }

    @Override
    public int optInt(int key, int defaultValue) {
        return table.get(key).optint(defaultValue);
    }

    @Override
    public void set(int key, int value) {
        table.set(key, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public void set(String key, int value) {
        table.set(key, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public double getDouble(String key) {
        return table.get(key).checkdouble();
    }

    @Override
    public double optDouble(String key, double defaultValue) {
        return table.get(key).optdouble(defaultValue);
    }

    @Override
    public double getDouble(int key) {
        return table.get(key).checkdouble();
    }

    @Override
    public double optDouble(int key, double defaultValue) {
        return table.get(key).optdouble(defaultValue);
    }

    @Override
    public float getFloat(String key) {
        return table.get(key).tofloat();
    }

    @Override
    public float optFloat(String key, float defaultValue) {
        return (float) table.get(key).optdouble(defaultValue);
    }

    @Override
    public float getFloat(int key) {
        return table.get(key).tofloat();
    }

    @Override
    public float optFloat(int key, float defaultValue) {
        return (float) table.get(key).optdouble(defaultValue);
    }

    @Override
    public void set(int key, double value) {
        table.set(key, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public void set(String key, double value) {
        table.set(key, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public void set(int index, float value) {
        table.set(index, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public void set(@NotNull String key, float value) {
        table.set(key, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public boolean getBoolean(String key) {
        return table.get(key).toboolean();
    }

    @Override
    public boolean optBoolean(String key, boolean defaultValue) {
        return table.get(key).optboolean(defaultValue);
    }

    @Override
    public boolean getBoolean(int key) {
        return table.get(key).toboolean();
    }

    @Override
    public boolean optBoolean(int key, boolean defaultValue) {
        return table.get(key).optboolean(defaultValue);
    }

    @Override
    public void set(int key, boolean value) {
        table.set(key, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public void set(String key, boolean value) {
        table.set(key, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public String getString(String key) {
        return table.get(key).tojstring();
    }

    @Override
    public String optString(String key, String defaultValue) {
        return table.get(key).optjstring(defaultValue);
    }

    @Override
    public String getString(int key) {
        return table.get(key).toString();
    }

    @Override
    public String optString(int key, String defaultValue) {
        return table.get(key).optjstring(defaultValue);
    }

    @Override
    public void set(int key, String value) {
        table.set(key, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public void set(String key, String value) {
        table.set(key, org.luaj.vm2.LuaValue.valueOf(value));
    }

    @Override
    public LuaTable getTable(String key) {
        val variable = table.get(key);
        if (!variable.istable()) {
            return null;
        }
        return new LuaJTable(engine, variable.checktable());
    }

    @Override
    public LuaTable getTable(int key) {
        val variable = table.get(key);
        if (!variable.istable()) {
            return null;
        }
        return new LuaJTable(engine, variable.checktable());
    }

    @Override
    public void set(int key, LuaTable value) {
        val rawTable = value.getRawTable();
        if (rawTable instanceof org.luaj.vm2.LuaTable)
            table.set(key, (org.luaj.vm2.LuaTable) rawTable);
    }

    @Override
    public void set(String key, LuaTable value) {
        val rawTable = value.getRawTable();
        if (rawTable instanceof org.luaj.vm2.LuaTable)
            table.set(key, (org.luaj.vm2.LuaTable) rawTable);
    }

    @Override
    public Set<String> getKeys() {
        return Arrays.stream(table.keys()).map(org.luaj.vm2.LuaValue::tojstring).collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public int[] getAsIntArray() {
        val array = new int[table.length()];
        for (int i = 0; i < array.length; i++) {
            array[i] = table.get(i + 1).optint(0);
        }
        return array;
    }

    @Override
    public int getSize() {
        return table.length();
    }

    @Override
    public Object getRawTable() {
        return table;
    }
}
