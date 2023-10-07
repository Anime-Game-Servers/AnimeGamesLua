package org.anime_game_servers.lua.engine;

import javax.annotation.Nullable;
import java.util.Set;

public interface LuaTable {
    boolean has(String key);

    @Nullable
    Object get(String key);

    @Nullable
    Object get(int key);

    int getInt(String key);

    int optInt(String key, int defaultValue);

    int getInt(int key);

    int optInt(int key, int defaultValue);

    void set(int key, int value);

    void set(String key, int value);

    double getDouble(String key);

    double optDouble(String key, double defaultValue);

    double getDouble(int key);

    double optDouble(int key, double defaultValue);

    float getFloat(String key);

    float optFloat(String key, float defaultValue);

    float getFloat(int key);

    float optFloat(int key, float defaultValue);

    void set(int key, double value);

    void set(String key, double value);

    boolean getBoolean(String key);

    boolean optBoolean(String key, boolean defaultValue);

    boolean getBoolean(int key);

    boolean optBoolean(int key, boolean defaultValue);

    void set(int key, boolean value);

    void set(String key, boolean value);

    String getString(String key);

    String optString(String key, String defaultValue);

    String getString(int key);

    String optString(int key, String defaultValue);

    void set(int key, String value);

    void set(String key, String value);

    @Nullable
    LuaTable getTable(String key);

    @Nullable
    LuaTable getTable(int key);

    void set(int key, LuaTable value);

    void set(String key, LuaTable value);

    Set<String> getKeys();

    int[] getAsIntArray();

    int getSize();

    Object getRawTable();
}
