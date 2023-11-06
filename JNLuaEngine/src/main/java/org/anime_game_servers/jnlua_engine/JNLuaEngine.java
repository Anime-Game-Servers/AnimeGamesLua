package org.anime_game_servers.jnlua_engine;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.Getter;
import org.anime_game_servers.lua.engine.LuaEngine;
import org.anime_game_servers.lua.engine.LuaScript;
import org.anime_game_servers.lua.engine.LuaTable;
import org.anime_game_servers.lua.engine.ScriptConfig;
import org.anime_game_servers.lua.models.IntValueEnum;
import org.anime_game_servers.lua.models.ScriptType;
import org.jetbrains.annotations.Nullable;
import org.terasology.jnlua.JavaFunction;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JNLuaEngine implements LuaEngine {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(JNLuaEngine.class.getName());
    @Getter
    private final ScriptEngineManager manager;
    @Getter(onMethod = @__(@Override))
    private final JNLuaSerializer serializer;
    @Getter
    private final SimpleBindings bindings;
    @Getter(onMethod = @__(@Override))
    private final ScriptConfig scriptConfig;

    public JNLuaEngine(ScriptConfig scriptConfig) {
        this.scriptConfig = scriptConfig;
        this.manager = new ScriptEngineManager();
        this.bindings = new SimpleBindings();
        this.serializer = new JNLuaSerializer();

        this.bindings.put("print", (JavaFunction) luaState -> {
            logger.debug(() -> "[LUA] print " + luaState.checkString(1));
            return 1;
        });

    }

    @Override
    public <T extends Enum<T>> boolean addGlobalEnumByOrdinal(String name, T[] enumArray) {
        Map<String, Integer> table = new HashMap<>();
        Arrays.stream(enumArray).forEach(e -> {
            table.put(e.name(), e.ordinal());
            table.put(e.name().toUpperCase(), e.ordinal());
        });
        bindings.put(name, table);
        return true;
    }

    @Override
    public <T extends Enum<T> & IntValueEnum> boolean addGlobalEnumByIntValue(String name, T[] enumArray) {
        Map<String, Integer> table = new HashMap<>();
        Arrays.stream(enumArray).forEach(e -> {
            table.put(e.name(), e.getValue());
            table.put(e.name().toUpperCase(), e.getValue());
        });
        bindings.put(name, table);
        return true;
    }

    @Override
    public boolean addGlobalStaticClass(String name, Class<?> staticClass) {
        try {
            bindings.put(name, new StaticClassWrapper(staticClass));
            return true;
        } catch (Exception e) {
            logger.error("Failed to add static class to lua engine: " + name, e);
        }
        return false;
    }

    @Override
    public boolean addObject(String name, Object object) {
        bindings.put(name, object);
        return false;
    }

    @Nullable
    @Override
    public LuaScript getScript(Path scriptPath, ScriptType scriptType) {
        if (!Files.exists(scriptPath)) return null;

        try {
            return new JNLuaScript(this, scriptPath, scriptType);
        } catch (IOException | ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LuaTable getTable(Object table) {
        return new JNLuaTable((AbstractMap<?, ?>) table);
    }

    @Override
    public LuaTable createTable() {
        return new JNLuaTable(new HashMap<>());
    }
}
