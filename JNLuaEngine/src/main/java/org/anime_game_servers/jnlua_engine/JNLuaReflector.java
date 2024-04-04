package org.anime_game_servers.jnlua_engine;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.Getter;
import org.anime_game_servers.lua.utils.LuaHelpersJvmKt;
import org.terasology.jnlua.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class JNLuaReflector extends DefaultJavaReflector {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(JNLuaReflector.class.getName());
    @Getter
    private static final JNLuaReflector INSTANCE = new JNLuaReflector();

    @Override
    protected Map<String, Accessor> createClassAccessors(Class<?> clazz) {
        Map<String, Accessor> result = new HashMap<String, Accessor>();

        // Fields
        Field[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++) {
            var field = fields[i];
            var luaNames = LuaHelpersJvmKt.getLuaNames(field);
            for (var luaName : luaNames) {
                result.put(luaName, new FieldAccessor(field));
            }
        }

        // Methods
        Map<String, Map<List<Class<?>>, Invocable>> accessibleMethods = new HashMap<String, Map<List<Class<?>>, Invocable>>();
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            // Do not overwrite fields
            Method method = methods[i];
            if (result.containsKey(method.getName())) {
                continue;
            }

            // Attempt to find the method in a public class if the declaring
            // class is not public
            if (!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
                method = getPublicClassMethod(clazz, method.getName(),
                        method.getParameterTypes());
                if (method == null) {
                    continue;
                }
            }

            // For each method name and parameter type list, keep
            // only the method declared by the most specific class
            Map<List<Class<?>>, Invocable> overloaded = accessibleMethods
                    .get(method.getName());
            if (overloaded == null) {
                overloaded = new HashMap<List<Class<?>>, Invocable>();
                accessibleMethods.put(method.getName(), overloaded);
            }
            List<Class<?>> parameterTypes = Arrays.asList(method
                    .getParameterTypes());
            Invocable currentInvocable = overloaded.get(parameterTypes);
            if (currentInvocable != null
                    && method.getDeclaringClass().isAssignableFrom(
                    currentInvocable.getDeclaringClass())) {
                continue;
            }
            overloaded.put(parameterTypes, new InvocableMethod(method));
        }
        for (Map.Entry<String, Map<List<Class<?>>, Invocable>> entry : accessibleMethods
                .entrySet()) {
            result.put(entry.getKey(), new InvocableAccessor(clazz, entry
                    .getValue().values()));
        }

        // Constructors
        Constructor<?>[] constructors = clazz.getConstructors();
        List<Invocable> accessibleConstructors = new ArrayList<Invocable>(
                constructors.length);
        for (int i = 0; i < constructors.length; i++) {
            // Ignore constructor if the declaring class is not public
            if (!Modifier.isPublic(constructors[i].getDeclaringClass()
                    .getModifiers())) {
                continue;
            }
            accessibleConstructors
                    .add(new InvocableConstructor(constructors[i]));
        }
        if (clazz.isInterface()) {
            accessibleConstructors.add(new InvocableProxy(clazz));
        }
        if (!accessibleConstructors.isEmpty()) {
            result.put("new", new InvocableAccessor(clazz,
                    accessibleConstructors));
        }

        // Properties
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo
                .getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            // Do not overwrite fields or methods
            if (result.containsKey(propertyDescriptors[i].getName())) {
                continue;
            }

            // Attempt to find the read/write methods in a public class if the
            // declaring class is not public
            Method method = propertyDescriptors[i].getReadMethod();
            if (method != null
                    && !Modifier.isPublic(method.getDeclaringClass()
                    .getModifiers())) {
                method = getPublicClassMethod(clazz, method.getName(),
                        method.getParameterTypes());
                try {
                    propertyDescriptors[i].setReadMethod(method);
                } catch (IntrospectionException e) {
                }
            }
            method = propertyDescriptors[i].getWriteMethod();
            if (method != null
                    && !Modifier.isPublic(method.getDeclaringClass()
                    .getModifiers())) {
                method = getPublicClassMethod(clazz, method.getName(),
                        method.getParameterTypes());
                try {
                    propertyDescriptors[i].setWriteMethod(method);
                } catch (IntrospectionException e) {
                }
            }

            // Do not process properties without a read and a write method
            if (propertyDescriptors[i].getReadMethod() == null
                    && propertyDescriptors[i].getWriteMethod() == null) {
                continue;
            }
            result.put(propertyDescriptors[i].getName(), new PropertyAccessor(
                    clazz, propertyDescriptors[i]));
        }
        return result;
    }
}
