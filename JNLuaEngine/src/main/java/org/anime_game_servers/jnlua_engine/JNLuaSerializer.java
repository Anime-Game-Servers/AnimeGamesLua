package org.anime_game_servers.jnlua_engine;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.val;
import org.anime_game_servers.core.base.interfaces.IntValueEnum;
import org.anime_game_servers.lua.serialize.BaseSerializer;
import org.terasology.jnlua.LuaValueProxy;
import org.terasology.jnlua.util.AbstractTableMap;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class JNLuaSerializer extends BaseSerializer {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(JNLuaSerializer.class.getName());

    private final ReentrantLock lock = new ReentrantLock();

    public static Number getNumber(Object value) {
        if (value instanceof Number l) {
            return l;
        } else if (value instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                logger.error(e, () -> "Exception parsing double "+s);
            }
        }
        return 0;
    }

    public static Integer getInt(Object value) {
        if (value instanceof Number l) {
            return l.intValue();
        } else if (value instanceof String s) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                logger.error(e, () -> "Exception parsing int "+s);
            }
        }
        return 0;
    }
    @Nullable
    public static <T extends Enum<T>> T getEnum(Object value, Class<?> type) {
        try {
            if(value instanceof String){
                return  (T) Enum.valueOf((Class<? extends Enum>) type, (String) value);
            } else if(value instanceof Number){
                val intVal = getInt(value);
                val enumConstants = type.getEnumConstants();
                if(IntValueEnum.class.isAssignableFrom(type)){
                    for (val enumConstant : enumConstants) {
                        if(((IntValueEnum) enumConstant).getValue() == intVal){
                            return (T) enumConstant;
                        }
                    }
                }
                return (T) enumConstants[intVal];
            }
        } catch (Exception e) {
            logger.error(e, () -> "Exception serializing enum "+value);
        }
        return null;
    }

    public static Long getLong(Object value) {
        if (value instanceof Number l) {
            return l.longValue();
        } else if (value instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (Exception e) {
                logger.warn(e, () -> "Exception parsing long "+s);
            }
        }
        return 0L;
    }

    public static Float getFloat(Object value) {
        if (value instanceof Number l) {
            return l.floatValue();
        } else if (value instanceof String s) {
            try {
                return Float.parseFloat(s);
            } catch (Exception e) {
                logger.warn(e, () -> "Exception parsing float "+s);
            }
        }
        return 0f;
    }
    public static Double getDouble(Object value) {
        if (value instanceof Number l) {
            return l.doubleValue();
        } else if (value instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                logger.warn(e, () -> "Exception parsing double "+s);
            }
        }
        return 0.0;
    }
    public static Boolean getBoolean(Object value) {
        if (value instanceof Number l) {
            return l.doubleValue()!=0.0;
        } else if (value instanceof String s) {
            try {
                return Boolean.parseBoolean(s);
            } catch (Exception e) {
                logger.warn(e, () -> "Exception parsing double "+s);
            }
        }
        return false;
    }

    // ...
    @Override
    public <T> List<T> toList(Class<T> type, Object obj) {
        return serializeList(type, (LuaValueProxy) obj);
    }

    @Override
    public <T> T toObject(Class<T> type, Object obj) {
        return serialize(type, null, (LuaValueProxy) obj);
    }

    @Override
    public <T> Map<String, T> toMap(Class<T> type, Object obj) {
        return serializeMap(String.class, type, (LuaValueProxy) obj);
    }

    private <T> T objectToClass(Class<T> type, Object value) {
        Object object = null;

        if(type.equals(int.class) || type.equals(Integer.class)){
            object = (T) getInt(value);
        } else if(type.equals(long.class) || type.equals(Long.class)){
            object = (T) getLong(value);
        } else if(type.equals(float.class) || type.equals(Float.class)){
            object = (T) getFloat(value);
        } else if(type.equals(double.class) || type.equals(Double.class)){
            object = (T) getDouble(value);
        } else if(type.equals(String.class)){
            object = (T) String.valueOf(value);
        } else if(type.equals(boolean.class) || type.equals(Boolean.class)){
            object = (T) getBoolean(value);
        } else if(Enum.class.isAssignableFrom(type)){
            getEnum(value, type);
        } else {
            object = serialize(type, null, (LuaValueProxy) value);
            if(String.class.isAssignableFrom(type)){
                return (T) String.valueOf(object);
            }
        }

        return (T) object;
    }

    private <T> List<T> serializeList(Class<T> type, LuaValueProxy table) {
        return serializeCollection(type, new ArrayList<>(), table);
    }
    private <T> Set<T> serializeSet(Class<T> type, LuaValueProxy table) {
        return serializeCollection(type, new HashSet<>(), table);
    }

    public <T, Y extends Collection<T>> Y serializeCollection(Class<T> type, Y target, LuaValueProxy table) {

        if (table == null) {
            return target;
        }

        var tableObj = (Map<String, Object>) table;
        try {
            for (var k : tableObj.entrySet()) {
                try {
                    var keyValue = k.getValue();

                    T object = objectToClass(type, keyValue);

                    if (object != null) {
                        target.add(object);
                    }
                } catch (Exception ex) {

                }
            }
        } catch (Exception e) {
            logger.error(e, () -> "Exception serializing list");
        }

        return target;
    }

    public <T> T serialize(Class<T> type, @Nullable Field field, LuaValueProxy table) {
        T object = null;

        if (type == List.class) {
            try {
                Class<?> listType = getCollectionType(type, field);
                return (T) serializeList(listType, table);
            } catch (Exception e) {
                logger.error(e, ()->"Exception serializing");
                return null;
            }
        }
        if (type == Set.class) {
            try {
                Class<?> listType = getCollectionType(type, field);
                return (T) serializeSet(listType, table);
            } catch (Exception e) {
                logger.error("Exception while serializing {}", type.getName(), e);
                return null;
            }
        }
        if (type == Map.class) {
            try {
                val mapTypes = getMapTypes(type, field);
                if(mapTypes == null){
                    return null;
                }
                return (T) serializeMap(mapTypes.component1(), mapTypes.component2(), table);
            } catch (Exception e) {
                logger.error("Exception while serializing {}", type.getName(), e);
                return null;
            }
        }

        try {
            if (!fieldMetaCache.containsKey(type)) {
                cacheType(type);
            }
            var methodAccess = methodAccessCache.get(type);
            var fieldMetaMap = fieldMetaCache.get(type);

            object = (T) constructorCache.get(type).newInstance();

            if (table == null) {
                return object;
            }

            var tableObj = (AbstractTableMap<String>) table;
            for (var k : tableObj.entrySet()) {
                try {
                    var keyName = k.getKey();
                    if (!fieldMetaMap.containsKey(keyName)) {
                        continue;
                    }

                    var fieldMeta = fieldMetaMap.get(keyName);
                    var keyValue = k.getValue();
                    if (fieldMeta.getType().equals(float.class)) {
                        set(object, fieldMeta, methodAccess, getFloat(keyValue));
                    } else if (fieldMeta.getType().equals(double.class)) {
                        set(object, fieldMeta, methodAccess, getDouble(keyValue));
                    } else if (fieldMeta.getType().equals(int.class)) {
                        set(object, fieldMeta, methodAccess, getInt(keyValue));
                    }  else if (fieldMeta.getType().equals(long.class)) {
                        set(object, fieldMeta, methodAccess, getLong(keyValue));
                    } else if (fieldMeta.getType().equals(String.class)) {
                        set(object, fieldMeta, methodAccess, keyValue);
                    } else if (fieldMeta.getType().equals(boolean.class)) {
                        set(object, fieldMeta, methodAccess, (boolean) keyValue);
                    } else if (fieldMeta.getType().equals(List.class)) {
                        LuaValueProxy objTable = (LuaValueProxy) tableObj.get(k.getKey());
                        Class<?> listType = getCollectionType(type, fieldMeta.getField());
                        List<?> listObj = serializeList(listType, objTable);
                        set(object, fieldMeta, methodAccess, listObj);
                    } else if (fieldMeta.getType().equals(Set.class)) {
                        LuaValueProxy objTable = (LuaValueProxy) tableObj.get(k.getKey());
                        Class<?> listType = getCollectionType(type, fieldMeta.getField());
                        Set<?> listObj = serializeSet(listType, objTable);
                        set(object, fieldMeta, methodAccess, listObj);
                    } else if(Enum.class.isAssignableFrom(fieldMeta.getType())){
                        val enumValue = getEnum(keyValue, fieldMeta.getType());
                        if(enumValue != null){
                            set(object, fieldMeta, methodAccess, enumValue);
                        }
                    } else {
                        set(object, fieldMeta, methodAccess, serialize(fieldMeta.getType(), fieldMeta.getField(), (LuaValueProxy) keyValue));
                    }
                } catch (Exception ex) {
                   logger.error(ex, () -> "Exception serializing");
                }
            }
        } catch (Exception e) {
            logger.error(e, ()->"Exception serializing");
        }

        return object;
    }

    public <K,V> Map<K, V> serializeMap(Class<K> typeKey, Class<V> typeValue, LuaValueProxy table) {
        Map<K, V> map = new HashMap<>();

        if (table == null) {
            return map;
        }

        var tableObj = (Map<Object, Object>) table;

        try {
            for (var k : tableObj.entrySet()) {
                try {
                    K key = objectToClass(typeKey, k.getKey());
                    if(key == null){
                        logger.warn(() -> "Can't serialize key: "+key+" to type: "+typeKey);
                        continue;
                    }

                    V value = objectToClass(typeValue, k.getValue());
                    if(value == null){
                        logger.warn(() -> "Can't serialize value: "+value+" to type: "+typeValue);
                        continue;
                    }

                    map.put(key, value);
                } catch (Exception ex) {

                }
            }
        } catch (Exception e) {
            logger.error(e, ()->"Exception serializing map");
        }

        return map;
    }
}
