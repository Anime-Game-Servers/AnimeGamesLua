package org.anime_game_servers.luaj_engine;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import kotlin.Pair;
import lombok.val;
import org.anime_game_servers.core.base.interfaces.IntValueEnum;
import org.anime_game_servers.lua.serialize.BaseSerializer;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.ast.Str;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class LuaJSerializer extends BaseSerializer {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(LuaJEngine.class.getName());

    @Nullable
    public static <T extends Enum<T>> T getEnum(Object value, Class<?> type) {
        val originalValue = value;
        try {
            if(value instanceof LuaNumber){
                value = ((LuaNumber) value).toint();
            } else if(value instanceof LuaString){
                value = ((LuaValue) value).tojstring();
            }
            if(value instanceof String){
                return  (T) Enum.valueOf((Class<? extends Enum>) type, (String) value);
            } else if(value instanceof Number){
                val enumConstants = type.getEnumConstants();
                if(IntValueEnum.class.isAssignableFrom(type)){
                    for (val enumConstant : enumConstants) {
                        if(((IntValueEnum) enumConstant).getValue() == ((Number) value).intValue()){
                            return (T) enumConstant;
                        }
                    }
                }
                return (T) enumConstants[((Number) value).intValue()];
            }
        } catch (Exception e) {
            logger.error(e, () -> "Exception serializing enum "+originalValue);
        }
        return null;
    }

    @Override
    public <T> List<T> toList(Class<T> type, Object obj) {
        return serializeList(type, (LuaTable) obj);
    }

    @Override
    public <T> T toObject(Class<T> type, Object obj) {
        return serialize(type, null, (LuaTable) obj);
    }

    @Override
    public <T> Map<String, T> toMap(Class<T> type, Object obj) {
        return serializeMap(String.class, type, (LuaTable) obj);
    }

    @Nullable
    private <T> T valueToType(Class<T> type, LuaValue keyValue) {
        Object object = null;

        if (keyValue.istable()) {
            object = serialize(type, null, keyValue.checktable());
        } else if (keyValue.isint()) {
            object = (Integer) keyValue.toint();
        } else if (keyValue.isnumber()) {
            object = (Float) keyValue.tofloat(); // terrible...
        } else if (keyValue.isstring()) {
            object = keyValue.tojstring();
        } else if (keyValue.isboolean()) {
            object = (Boolean) keyValue.toboolean();
        } else {
            object = keyValue;
        }
        if (object == null) {
            logger.warn(() -> "Can't serialize value: "+keyValue+" to type: "+type);
        }

        if(String.class.isAssignableFrom(type)){
            return (T) String.valueOf(object);
        } else {
            return (T) object;
        }
    }

    private <K,V> Map<K, V> serializeMap(Class<K> typeKey, Class<V> typeValue, LuaTable table) {
        Map<K, V> map = new HashMap<>();

        if (table == null) {
            return map;
        }

        try {
            LuaValue[] keys = table.keys();
            for (LuaValue luaKey : keys) {
                try {
                    K key = valueToType(typeKey, luaKey);
                    if(key == null){
                        logger.warn(() -> "Can't serialize key: "+luaKey+" to type: "+typeKey);
                        continue;
                    }

                    LuaValue luaValue = table.get(luaKey);
                    V value = valueToType(typeValue, luaValue);
                    if(value == null){
                        logger.warn(() -> "Can't serialize value: "+value+" to type: "+typeValue);
                        continue;
                    }

                    map.put(key, value);
                } catch (Exception ex) {
                    logger.debug(ex, () -> "Exception serializing map");
                }
            }
        } catch (Exception e) {
            logger.error(e, () -> "Exception while serializing map");
        }

        return map;
    }

    private <T> List<T> serializeList(Class<T> type, LuaTable table) {
        val startSize = table != null ? table.length() : 0;
        return serializeCollection(type, new ArrayList<>(startSize), table);
    }
    private <T> Set<T> serializeSet(Class<T> type, LuaTable table) {
        val startSize = table != null ? table.length() : 0;
        return serializeCollection(type, new HashSet<>(startSize), table);
    }

    public <T, Y extends Collection<T>> Y serializeCollection(Class<T> type, Y target, LuaTable table) {

        if (table == null) {
            return target;
        }

        try {
            LuaValue[] keys = table.keys();
            for (LuaValue k : keys) {
                try {
                    LuaValue keyValue = table.get(k);

                    T object = valueToType(type, keyValue);

                    if (object != null) {
                        target.add(object);
                    }
                } catch (Exception ex) {

                }
            }
        } catch (Exception e) {
            logger.error(e, () -> "Exception while serializing list");
        }

        return target;
    }

    public <T> T serialize(Class<T> type, @Nullable Field field, LuaTable table) {
        T object = null;

        if (type == List.class) {
            try {
                Class<?> listType = getCollectionType(type, field);
                return (T) serializeList(listType, table);
            } catch (Exception e) {
                logger.error("Exception while serializing {}", type.getName(), e);
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

            LuaValue[] keys = table.keys();
            for (LuaValue k : keys) {
                try {
                    var keyName = k.checkjstring();
                    if (!fieldMetaMap.containsKey(keyName)) {
                        continue;
                    }
                    var fieldMeta = fieldMetaMap.get(keyName);
                    LuaValue keyValue = table.get(k);

                    if (keyValue.istable()) {
                        set(object, fieldMeta, methodAccess, serialize(fieldMeta.getType(), fieldMeta.getField(), keyValue.checktable()));
                    } else if (fieldMeta.getType().equals(float.class)) {
                        set(object, fieldMeta, methodAccess, keyValue.tofloat());
                    } else if (fieldMeta.getType().equals(double.class)) {
                        set(object, fieldMeta, methodAccess, keyValue.todouble());
                    } else if (fieldMeta.getType().equals(int.class)) {
                        set(object, fieldMeta, methodAccess, keyValue.toint());
                    } else if (fieldMeta.getType().equals(long.class)) {
                        set(object, fieldMeta, methodAccess, keyValue.tolong());
                    } else if (fieldMeta.getType().equals(String.class)) {
                        set(object, fieldMeta, methodAccess, keyValue.tojstring());
                    } else if (fieldMeta.getType().equals(boolean.class)) {
                        set(object, fieldMeta, methodAccess, keyValue.toboolean());
                    } else if(Enum.class.isAssignableFrom(fieldMeta.getType())){
                        val enumValue = getEnum(keyValue, fieldMeta.getType());
                        if(enumValue != null){
                            set(object, fieldMeta, methodAccess, enumValue);
                        }
                    } else {
                        set(object, fieldMeta, methodAccess, keyValue.tojstring());
                    }
                } catch (Exception ex) {
                    logger.error(ex, () -> "Exception serializing");
                }
            }
        } catch (Exception e) {
            //logger.info(ScriptUtils.toMap(table).toString());
            logger.error(e, () -> "Exception while serializing "+ type.getName());
        }

        return object;
    }
}
