package org.anime_game_servers.jnlua_engine;

import org.anime_game_servers.lua.serialize.BaseSerializer;
import org.terasology.jnlua.LuaValueProxy;
import org.terasology.jnlua.util.AbstractTableMap;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class JNLuaSerializer extends BaseSerializer {

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
        return serializeMap(type, (LuaValueProxy) obj);
    }

    private <T> T objectToClass(Class<T> type, Object value) {
        T object = null;

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
        return object;
    }

    public <T> List<T> serializeList(Class<T> type, LuaValueProxy table) {
        List<T> list = new ArrayList<>();

        if (table == null) {
            return list;
        }

        var tableObj = (Map<String, Object>) table;
        try {
            for (var k : tableObj.entrySet()) {
                try {
                    var keyValue = k.getValue();

                    T object = objectToClass(type, keyValue);

                    if (object != null) {
                        list.add(object);
                    }
                } catch (Exception ex) {

                }
            }
        } catch (Exception e) {
            //LuaEngine.logger.error("Exception serializing list", e);
        }

        return list;
    }

    private Class<?> getListType(Class<?> type, @Nullable Field field){
        if(field == null){
            return type.getTypeParameters()[0].getClass();
        }
        Type fieldType = field.getGenericType();
        if(fieldType instanceof ParameterizedType){
            return (Class<?>) ((ParameterizedType) fieldType).getActualTypeArguments()[0];
        }

        return null;
    }

    public <T> T serialize(Class<T> type, @Nullable Field field, LuaValueProxy table) {
        T object = null;

        if (type == List.class) {
            try {
                Class<?> listType = getListType(type, field);
                return (T) serializeList(listType, table);
            } catch (Exception e) {
                //LuaEngine.logger.error("Exception serializing", e);
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
                        Class<?> listType = getListType(type, fieldMeta.getField());
                        List<?> listObj = serializeList(listType, objTable);
                        set(object, fieldMeta, methodAccess, listObj);
                    } else {
                        set(object, fieldMeta, methodAccess, serialize(fieldMeta.getType(), fieldMeta.getField(), (LuaValueProxy) keyValue));
                        //methodAccess.invoke(object, fieldMeta.index, keyValue);
                    }
                } catch (Exception ex) {
                   // LuaEngine.logger.error("Exception serializing", ex);
                }
            }
        } catch (Exception e) {
            //LuaEngine.logger.error("Exception serializing", e);
        }

        return object;
    }

    public <T> Map<String, T> serializeMap(Class<T> type, LuaValueProxy table) {
        Map<String, T> map = new HashMap<>();

        if (table == null) {
            return map;
        }

        var tableObj = (Map<String, Object>) table;

        try {
            for (var k : tableObj.entrySet()) {
                try {
                    var keyValue = k.getValue();

                    T object = objectToClass(type, keyValue);

                    if (object != null) {
                        map.put(k.getKey(), object);
                    }
                } catch (Exception ex) {

                }
            }
        } catch (Exception e) {
            //LuaEngine.logger.error("Exception serializing map", e);
        }

        return map;
    }
}
