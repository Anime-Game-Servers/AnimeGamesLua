package org.anime_game_servers.jnlua_engine;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.val;
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

    public static Integer getInt(Object value) {
        if (value instanceof Integer l) {
            return l.intValue();
        } else if (value instanceof Double d) {
            return d.intValue();
        }
        return 0;
    }

    public static Float getFloat(Object value) {
        if (value instanceof Double l) {
            return l.floatValue();
        } else if (value instanceof Integer l) {
            return l.floatValue();
        }
        return 0f;
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

        if (value instanceof Integer) {
            object = (T) getInt(value);
        } else if (value instanceof Double) {
            object = (T) getFloat(value);
        } else if (value instanceof String) {
            object = (T) value;
        } else if (value instanceof Boolean) {
            object = (T) value;
        } else {
            object = serialize(type, null, (LuaValueProxy) value);
        }
        if(String.class.isAssignableFrom(type)){
            return (T) String.valueOf(object);
        } else {
            return (T) object;
        }
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
                        set(object, fieldMeta, methodAccess, (double) keyValue);
                    } else if (fieldMeta.getType().equals(int.class)) {
                        set(object, fieldMeta, methodAccess, getInt(keyValue));
                    } else if (fieldMeta.getType().equals(String.class)) {
                        set(object, fieldMeta, methodAccess, keyValue);
                    } else if (fieldMeta.getType().equals(boolean.class)) {
                        set(object, fieldMeta, methodAccess, (boolean) keyValue);
                    } else if (fieldMeta.getType().equals(List.class)) {
                        LuaValueProxy objTable = (LuaValueProxy) tableObj.get(k.getKey());
                        Class<?> listType = getCollectionType(type, fieldMeta.getField());
                        List<?> listObj = serializeList(listType, objTable);
                        set(object, fieldMeta, methodAccess, listObj);
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
