package org.anime_game_servers.lua.serialize;


import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseSerializer implements Serializer {

    protected static final Map<Class<?>, MethodAccess> methodAccessCache = new ConcurrentHashMap<>();
    protected static final Map<Class<?>, ConstructorAccess<?>> constructorCache = new ConcurrentHashMap<>();
    protected static final Map<Class<?>, Map<String, FieldMeta>> fieldMetaCache = new ConcurrentHashMap<>();
    public abstract <T> List<T> toList(Class<T> type, Object obj);

    public abstract <T> T toObject(Class<T> type, Object obj);

    public abstract <T> Map<String,T> toMap(Class<T> type, Object obj);


    protected String getSetterName(String fieldName) {
        if (fieldName == null || fieldName.length() == 0) {
            return null;
        }
        if (fieldName.length() == 1) {
            return "set" + fieldName.toUpperCase();
        }
        return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    protected <T> Map<String, FieldMeta> cacheType(Class<T> type) {
        if (fieldMetaCache.containsKey(type)) {
            return fieldMetaCache.get(type);
        }
        if (!constructorCache.containsKey(type)) {
            constructorCache.putIfAbsent(type, ConstructorAccess.get(type));
        }
        var methodAccess = Optional.ofNullable(methodAccessCache.get(type)).orElse(MethodAccess.get(type));
        methodAccessCache.putIfAbsent(type, methodAccess);

        var fieldMetaMap = new HashMap<String, FieldMeta>();
        var methodNameSet = new HashSet<>(Arrays.stream(methodAccess.getMethodNames()).toList());

        Class<?> classtype = type;
        while(classtype!=null){
            Arrays.stream(classtype.getDeclaredFields())
                .forEach(field -> {
                    if(methodNameSet.contains(getSetterName(field.getName()))) {
                        var setter = getSetterName(field.getName());
                        var index = methodAccess.getIndex(setter);
                        fieldMetaMap.put(field.getName(), new FieldMeta(field.getName(), setter, index, field.getType(), field));
                    } else {
                        field.setAccessible(true);
                        fieldMetaMap.put(field.getName(), new FieldMeta(field.getName(), null, -1, field.getType(), field));
                    }
                });
            classtype = classtype.getSuperclass();
        }

        Arrays.stream(type.getFields())
            .filter(field -> !fieldMetaMap.containsKey(field.getName()))
            .filter(field -> methodNameSet.contains(getSetterName(field.getName())))
            .forEach(field -> {
                var setter = getSetterName(field.getName());
                var index = methodAccess.getIndex(setter);
                fieldMetaMap.put(field.getName(), new FieldMeta(field.getName(), setter, index, field.getType(), field));
            });

        fieldMetaCache.put(type, fieldMetaMap);
        return fieldMetaMap;
    }

    protected void set(Object object, @Nonnull FieldMeta fieldMeta, @Nullable MethodAccess methodAccess, int value){
        try {
            if (methodAccess != null && fieldMeta.getSetter() != null) {
                methodAccess.invoke(object, fieldMeta.getIndex(), value);
            } else if(fieldMeta.getField() != null){
                fieldMeta.getField().setInt(object, value);
            }
        } catch (Exception ex){
            //Grasscutter.getLogger().warn("Failed to set field {} of type {} to value {}", fieldMeta.getName(), fieldMeta.getType(), value, ex);
        }
    }
    protected void set(Object object, @Nonnull FieldMeta fieldMeta, @Nullable MethodAccess methodAccess, double value){
        try {
            if (methodAccess != null && fieldMeta.getSetter() != null) {
                methodAccess.invoke(object, fieldMeta.getIndex(), value);
            } else if(fieldMeta.getField() != null) {
                fieldMeta.getField().setDouble(object, value);
            }
        } catch (Exception ex){
            //Grasscutter.getLogger().warn("Failed to set field {} of type {} to value {}", fieldMeta.getName(), fieldMeta.getType(), value, ex);
        }
    }
    protected void set(Object object, @Nonnull FieldMeta fieldMeta, @Nullable MethodAccess methodAccess, float value){
        try {
            if (methodAccess != null && fieldMeta.getSetter() != null) {
                methodAccess.invoke(object, fieldMeta.getIndex(), value);
            } else if(fieldMeta.getField() != null) {
                fieldMeta.getField().setFloat(object, value);
            }
        } catch (Exception ex){
            //Grasscutter.getLogger().warn("Failed to set field {} of type {} to value {}", fieldMeta.getName(), fieldMeta.getType(), value, ex);
        }
    }
    protected void set(Object object, @Nonnull FieldMeta fieldMeta, @Nullable MethodAccess methodAccess, long value){
        try {
            if (methodAccess != null && fieldMeta.getSetter() != null) {
                methodAccess.invoke(object, fieldMeta.getIndex(), value);
            } else if(fieldMeta.getField() != null) {
                fieldMeta.getField().setLong(object, value);
            }
        } catch (Exception ex){
            //Grasscutter.getLogger().warn("Failed to set field {} of type {} to value {}", fieldMeta.getName(), fieldMeta.getType(), value, ex);
        }
    }
    protected void set(Object object, @Nonnull FieldMeta fieldMeta, @Nullable MethodAccess methodAccess, boolean value){
        try {
            if (methodAccess != null && fieldMeta.getSetter() != null) {
                methodAccess.invoke(object, fieldMeta.getIndex(), value);
            } else if(fieldMeta.getField() != null) {
                fieldMeta.getField().setBoolean(object, value);
            }
        } catch (Exception ex){
            //Grasscutter.getLogger().warn("Failed to set field {} of type {} to value {}", fieldMeta.getName(), fieldMeta.getType(), value, ex);
        }
    }
    protected void set(Object object, @Nonnull FieldMeta fieldMeta, @Nullable MethodAccess methodAccess, Object value){
        try {
            if (methodAccess != null && fieldMeta.getSetter() != null) {
                methodAccess.invoke(object, fieldMeta.getIndex(), value);
            } else if(fieldMeta.getField() != null) {
                fieldMeta.getField().set(object, value);
            }
        } catch (Exception ex){
            //Grasscutter.getLogger().warn("Failed to set field {} of type {} to value {}", fieldMeta.getName(), fieldMeta.getType(), value, ex);
        }
    }
}
