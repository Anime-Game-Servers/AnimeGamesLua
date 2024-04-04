package org.anime_game_servers.luaj_engine.coerse;

import org.anime_game_servers.lua.utils.LuaHelpersJvmKt;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JavaClass extends org.luaj.vm2.lib.jse.JavaClass {

    protected static final Map<Class<?>, JavaClass> classes = Collections.synchronizedMap(new HashMap<>());
    JavaClass(Class c) {
        super(c);
    }

    public static JavaClass forClass(Class<?> c) {
        return classes.computeIfAbsent(c, JavaClass::new);
    }

    @Override
    protected void addField(Field fi, Map<LuaValue, Field> m) {
        if (Modifier.isPublic(fi.getModifiers())) {
            var names = LuaHelpersJvmKt.getLuaNames(fi);
            for (var name : names) {
                m.put(LuaValue.valueOf(name), fi);
            }
            try {
                if (!fi.isAccessible())
                    fi.setAccessible(true);
            } catch (SecurityException s) {
            }
        }
    }
}
