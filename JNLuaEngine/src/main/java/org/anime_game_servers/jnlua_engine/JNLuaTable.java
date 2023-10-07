package org.anime_game_servers.jnlua_engine;

import lombok.val;
import org.anime_game_servers.lua.engine.LuaTable;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JNLuaTable implements LuaTable {

    AbstractMap<Object, Object> table;

    JNLuaTable(AbstractMap<?, ?> table) {
        this.table = (AbstractMap<Object, Object>) table;
    }

    @Override
    public boolean has(String key) {
        return table.containsKey(key);
    }

    @Override
    public Object get(String key) {
        return table.get(key);
    }

    @Override
    public Object get(int key) {
        return table.get(key);
    }

    @Override
    public int getInt(String key) {
        return ((Number) table.get(key)).intValue();
    }

    @Override
    public int optInt(String key, int defaultValue) {
        val value = table.get(key);
        if (value instanceof Number)
            return ((Number) value).intValue();
        return defaultValue;
    }

    @Override
    public int getInt(int key) {
        return ((Number) table.get(key)).intValue();
    }

    @Override
    public int optInt(int key, int defaultValue) {
        val value = table.get(key);
        if (value instanceof Number)
            return ((Number) value).intValue();
        return defaultValue;
    }

    @Override
    public void set(int key, int value) {
        table.put(key, value);
    }

    @Override
    public void set(String key, int value) {
        table.put(key, value);
    }

    @Override
    public double getDouble(String key) {
        return ((Number) table.get(key)).doubleValue();
    }

    @Override
    public double optDouble(String key, double defaultValue) {
        val value = table.get(key);
        if (value instanceof Number)
            return ((Number) value).doubleValue();
        return defaultValue;
    }

    @Override
    public double getDouble(int key) {
        return ((Number) table.get(key)).doubleValue();
    }

    @Override
    public double optDouble(int key, double defaultValue) {
        val value = table.get(key);
        if (value instanceof Number)
            return ((Number) value).doubleValue();
        return defaultValue;
    }

    @Override
    public float getFloat(String key) {
        return ((Number) table.get(key)).floatValue();
    }

    @Override
    public float optFloat(String key, float defaultValue) {
        val value = table.get(key);
        if (value instanceof Number)
            return ((Number) value).floatValue();
        return defaultValue;
    }

    @Override
    public float getFloat(int key) {
        return ((Number) table.get(key)).floatValue();
    }

    @Override
    public float optFloat(int key, float defaultValue) {
        val value = table.get(key);
        if (value instanceof Number)
            return ((Number) value).floatValue();
        return defaultValue;
    }

    @Override
    public void set(int key, double value) {
        table.put(key, value);
    }

    @Override
    public void set(String key, double value) {
        table.put(key, value);
    }

    @Override
    public boolean getBoolean(String key) {
        return (Boolean) table.get(key);
    }

    @Override
    public boolean optBoolean(String key, boolean defaultValue) {
        val value = table.get(key);
        if (value instanceof Boolean)
            return (boolean) value;
        return defaultValue;
    }

    @Override
    public boolean getBoolean(int key) {
        return (Boolean) table.get(key);
    }

    @Override
    public boolean optBoolean(int key, boolean defaultValue) {
        val value = table.get(key);
        if (value instanceof Boolean)
            return (boolean) value;
        return defaultValue;
    }

    @Override
    public void set(int key, boolean value) {
        table.put(key, value);
    }

    @Override
    public void set(String key, boolean value) {
        table.put(key, value);
    }

    @Override
    public String getString(String key) {
        return (String) table.get(key);
    }

    @Override
    public String optString(String key, String defaultValue) {
        val value = table.get(key);
        if (value instanceof String)
            return (String) value;
        return defaultValue;
    }

    @Override
    public String getString(int key) {
        return (String) table.get(key);
    }

    @Override
    public String optString(int key, String defaultValue) {
        val value = table.get(key);
        if (value instanceof String)
            return (String) value;
        return defaultValue;
    }

    @Override
    public void set(int key, String value) {
        table.put(key, value);
    }

    @Override
    public void set(String key, String value) {
        table.put(key, value);
    }

    @Nullable
    @Override
    public LuaTable getTable(String key) {
        return new JNLuaTable((AbstractMap<?, ?>) table.get(key));
    }

    @Nullable
    @Override
    public LuaTable getTable(int key) {
        return new JNLuaTable((AbstractMap<?, ?>) table.get(key));
    }

    @Override
    public void set(int key, LuaTable value) {
        table.put(key, value.getRawTable());
    }

    @Override
    public void set(String key, LuaTable value) {
        table.put(key, value.getRawTable());
    }

    @Override
    public Set<String> getKeys() {
        //TODO check if this is correct
        val keys = table.keySet();
        if (keys != null) {
            return (Set<String>) (Set<?>) keys;
        }
        return IntStream.rangeClosed(1, table.size()).mapToObj(String::valueOf).collect(Collectors.toSet());
    }

    @Override
    public int[] getAsIntArray() {
        int[] result = new int[table.size()];
        for (int i = 0; i < table.size(); i++) {
            result[i] = (Integer) table.get(i + 1);
        }
        return result;
    }

    @Override
    public int getSize() {
        return table.size();
    }

    @Override
    public Object getRawTable() {
        return table;
    }
}
