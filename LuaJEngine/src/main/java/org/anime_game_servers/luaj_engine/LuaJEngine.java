package org.anime_game_servers.luaj_engine;

import lombok.Getter;
import lombok.val;
import org.anime_game_servers.lua.engine.LuaEngine;
import org.anime_game_servers.lua.engine.LuaScript;
import org.anime_game_servers.lua.models.IntValueEnum;
import org.anime_game_servers.lua.models.ScriptType;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.script.LuajContext;

import javax.annotation.Nullable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class LuaJEngine implements LuaEngine {
    private final ScriptEngineManager manager;
    private final LuajContext context;

    @Getter
    private final ScriptEngine engine;
    @Getter(onMethod = @__(@Override))
    private final LuaJSerializer serializer;

    public LuaJEngine() {
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("luaj");

        serializer = new LuaJSerializer();
        context = (LuajContext) engine.getContext();

        // Set engine to replace require as a temporary fix to missing scripts
        context.globals.finder = new ResourceFinder() {
            @Override
            public InputStream findResource(String filename) {
                val stream = scriptFinder.openScript(filename);
                return stream!=null ? stream : new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public boolean useRawParamString() {
                return true;
            }
        };
    }

    @Override
    public <T extends Enum<T>> boolean addGlobalEnumByOrdinal(String name, T[] enumArray) {
        LuaTable table = new LuaTable();
        Arrays.stream(enumArray).forEach(e -> {
            table.set(e.name(), e.ordinal());
            table.set(e.name().toUpperCase(), e.ordinal());
        });
        context.globals.set(name, table);
        return true;
    }

    @Override
    public <T extends Enum<T> & IntValueEnum> boolean addGlobalEnumByIntValue(String name, T[] enumArray) {
        LuaTable table = new LuaTable();
        Arrays.stream(enumArray).forEach(e -> {
            table.set(e.name(), e.getValue());
            table.set(e.name().toUpperCase(), e.getValue());
        });
        context.globals.set(name, table);
        return true;
    }

    @Override
    public boolean addGlobalStaticClass(String name, Class<?> staticClass) {
        try {
            context.globals.set(name, CoerceJavaToLua.coerce(staticClass.getConstructor().newInstance()));
            return true;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            //logger.error("Failed to add static class to lua engine: " + name, e);
        }
        return false;
    }

    @Override
    public boolean addObject(String name, Object object) {
        context.globals.set(name, CoerceJavaToLua.coerce(object));
        return true;
    }

    @Nullable
    @Override
    public LuaScript getScript(String scriptName, ScriptType scriptType) {
        final Path scriptPath = scriptFinder.getScriptPath(scriptName);
        if (!Files.exists(scriptPath)) return null;

        try {
            return new LuaJScript(this, scriptPath);
        } catch (IOException | ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public org.anime_game_servers.lua.engine.LuaTable getTable(Object table) {
        if (table instanceof LuaTable)
            return new LuaJTable(this, (LuaTable) table);
        throw new IllegalArgumentException("Table must be a LuaTable");
    }

    @Override
    public org.anime_game_servers.lua.engine.LuaTable createTable() {
        return new LuaJTable(this, new LuaTable());
    }
}
