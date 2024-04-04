package org.anime_game_servers.luaj_engine.coerse;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.JavaArray;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to coerce values from Java to lua within the luajava library.
 * <p>
 * This class is primarily used by the {@link org.luaj.vm2.lib.jse.LuajavaLib},
 * but can also be used directly when working with Java/lua bindings.
 * <p>
 * To coerce scalar types, the various, generally the {@code valueOf(type)} methods
 * on {@link LuaValue} may be used:
 * <ul>
 * <li>{@link LuaValue#valueOf(boolean)}</li>
 * <li>{@link LuaValue#valueOf(byte[])}</li>
 * <li>{@link LuaValue#valueOf(double)}</li>
 * <li>{@link LuaValue#valueOf(int)}</li>
 * <li>{@link LuaValue#valueOf(String)}</li>
 * </ul>
 * <p>
 * To coerce arrays of objects and lists, the {@code listOf(..)} and {@code tableOf(...)} methods
 * on {@link LuaValue} may be used:
 * <ul>
 * <li>{@link LuaValue#listOf(LuaValue[])}</li>
 * <li>{@link LuaValue#listOf(LuaValue[], org.luaj.vm2.Varargs)}</li>
 * <li>{@link LuaValue#tableOf(LuaValue[])}</li>
 * <li>{@link LuaValue#tableOf(LuaValue[], LuaValue[], org.luaj.vm2.Varargs)}</li>
 * </ul>
 * The method {@link org.luaj.vm2.lib.jse.CoerceJavaToLua#coerce(Object)} looks as the type and dimesioning
 * of the argument and tries to guess the best fit for corrsponding lua scalar,
 * table, or table of tables.
 *
 * @see org.luaj.vm2.lib.jse.CoerceJavaToLua#coerce(Object)
 * @see org.luaj.vm2.lib.jse.LuajavaLib
 */
public class CoerceJavaToLua {

    static final Map<Class<?>, Coercion> COERCIONS = Collections.synchronizedMap(new HashMap<>());

    static final Coercion instanceCoercion = new InstanceCoercion();
    static final Coercion arrayCoercion = new ArrayCoercion();
    static final Coercion luaCoercion = new LuaCoercion();

    static {
        Coercion boolCoercion = new BoolCoercion();
        Coercion intCoercion = new IntCoercion();
        Coercion charCoercion = new CharCoercion();
        Coercion doubleCoercion = new DoubleCoercion();
        Coercion stringCoercion = new StringCoercion();
        Coercion bytesCoercion = new BytesCoercion();
        Coercion classCoercion = new ClassCoercion();
        COERCIONS.put(Boolean.class, boolCoercion);
        COERCIONS.put(Byte.class, intCoercion);
        COERCIONS.put(Character.class, charCoercion);
        COERCIONS.put(Short.class, intCoercion);
        COERCIONS.put(Integer.class, intCoercion);
        COERCIONS.put(Long.class, doubleCoercion);
        COERCIONS.put(Float.class, doubleCoercion);
        COERCIONS.put(Double.class, doubleCoercion);
        COERCIONS.put(String.class, stringCoercion);
        COERCIONS.put(byte[].class, bytesCoercion);
        COERCIONS.put(Class.class, classCoercion);
    }

    /**
     * Coerse a Java object to a corresponding lua value.
     * <p>
     * Integral types {@code boolean}, {@code byte},  {@code char}, and {@code int}
     * will become {@link LuaInteger};
     * {@code long}, {@code float}, and {@code double} will become {@link LuaDouble};
     * {@code String} and {@code byte[]} will become {@link LuaString};
     * types inheriting from {@link LuaValue} will be returned without coercion;
     * other types will become {@link LuaUserdata}.
     *
     * @param o Java object needing conversion
     * @return {@link LuaValue} corresponding to the supplied Java value.
     * @see LuaValue
     * @see LuaInteger
     * @see LuaDouble
     * @see LuaString
     * @see LuaUserdata
     */
    public static LuaValue coerce(Object o) {
        if (o == null)
            return LuaValue.NIL;
        Class<?> clazz = o.getClass();
        Coercion c = COERCIONS.get(clazz);
        if (c == null) {
            c = clazz.isArray() ? arrayCoercion :
                    o instanceof LuaValue ? luaCoercion :
                            instanceCoercion;
            COERCIONS.put(clazz, c);
        }
        return c.coerce(o);
    }

    interface Coercion {
        LuaValue coerce(Object javaValue);
    }

    private static final class BoolCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            boolean b = (Boolean) javaValue;
            return b ? LuaValue.TRUE : LuaValue.FALSE;
        }
    }

    private static final class IntCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            Number n = (Number) javaValue;
            return LuaInteger.valueOf(n.intValue());
        }
    }

    private static final class CharCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            Character c = (Character) javaValue;
            return LuaInteger.valueOf(c);
        }
    }

    private static final class DoubleCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            Number n = (Number) javaValue;
            return LuaDouble.valueOf(n.doubleValue());
        }
    }

    private static final class StringCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            return LuaString.valueOf(javaValue.toString());
        }
    }

    private static final class BytesCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            return LuaValue.valueOf((byte[]) javaValue);
        }
    }

    private static final class ClassCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            return JavaClass.forClass((Class<?>) javaValue);
        }
    }

    private static final class InstanceCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            return new JavaInstance(javaValue);
        }
    }

    private static final class ArrayCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            // should be userdata?
            return new JavaArray(javaValue);
        }
    }

    private static final class LuaCoercion implements Coercion {
        public LuaValue coerce(Object javaValue) {
            return (LuaValue) javaValue;
        }
    }
}
