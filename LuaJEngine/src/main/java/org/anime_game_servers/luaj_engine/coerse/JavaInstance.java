package org.anime_game_servers.luaj_engine.coerse;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import java.lang.reflect.Field;

public class JavaInstance extends LuaUserdata {
    JavaClass jclass;

    public JavaInstance(Object instance) {
        super(instance);
    }

    public LuaValue get(LuaValue key) {
        if (jclass == null)
            jclass = JavaClass.forClass(m_instance.getClass());
        Field f = jclass.getField(key);
        if (f != null)
            try {
                return CoerceJavaToLua.coerce(f.get(m_instance));
            } catch (Exception e) {
                throw new LuaError(e);
            }
        LuaValue m = jclass.getMethod(key);
        if (m != null)
            return m;
        Class<?> c = jclass.getInnerClass(key);
        if (c != null)
            return JavaClass.forClass(c);
        return super.get(key);
    }

    public void set(LuaValue key, LuaValue value) {
        if (jclass == null)
            jclass = JavaClass.forClass(m_instance.getClass());
        Field f = jclass.getField(key);
        if (f != null)
            try {
                f.set(m_instance, CoerceLuaToJava.coerce(value, f.getType()));
                return;
            } catch (Exception e) {
                throw new LuaError(e);
            }
        super.set(key, value);
    }
}
