package org.anime_game_servers.jnlua_engine

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.lua.utils.getLuaNames
import org.terasology.jnlua.DefaultJavaReflector
import java.beans.BeanInfo
import java.beans.IntrospectionException
import java.beans.Introspector
import java.lang.reflect.Modifier
import java.util.*
import kotlin.reflect.jvm.kotlinProperty

private val logger = logger {}

object JNLuaReflector : DefaultJavaReflector() {
    override fun createClassAccessors(clazz: Class<*>): Map<String, Accessor> {
        val result: MutableMap<String, Accessor> = HashMap<String, Accessor>()

        // Fields
        val fields = clazz.getFields()
        for (i in fields.indices) {
            val field = fields[i]
            val luaNames = field.getLuaNames()
            for (luaName in luaNames) {
                result[luaName] = FieldAccessor(field)
            }
        }

        // Methods
        val accessibleMethods: MutableMap<String, MutableMap<List<Class<*>>, Invocable>> =
            HashMap<String, MutableMap<List<Class<*>>, Invocable>>()
        val methods = clazz.getMethods()
        for (i in methods.indices) {
            // Do not overwrite fields
            var method = methods[i]
            if (result.containsKey(method.name)) {
                continue
            }

            // Attempt to find the method in a public class if the declaring
            // class is not public
            if (!Modifier.isPublic(method.declaringClass.modifiers)) {
                method = getPublicClassMethod(
                    clazz, method.name,
                    method.parameterTypes
                )
                if (method == null) {
                    continue
                }
            }

            // For each method name and parameter type list, keep
            // only the method declared by the most specific class
            var overloaded = accessibleMethods[method.name]
            if (overloaded == null) {
                overloaded = HashMap<List<Class<*>>, Invocable>()
                accessibleMethods[method.name] = overloaded
            }
            val parameterTypes = mutableListOf(*method.parameterTypes)
            val currentInvocable = overloaded[parameterTypes]
            if (currentInvocable != null
                && method.declaringClass.isAssignableFrom(
                    currentInvocable.declaringClass
                )
            ) {
                continue
            }
            overloaded[parameterTypes] = InvocableMethod(method)
        }
        for (entry in accessibleMethods.entries) {
            result[entry.key] = InvocableAccessor(clazz, entry.value.values)
        }

        // Constructors
        val constructors = clazz.getConstructors()
        val accessibleConstructors: MutableList<Invocable> = ArrayList<Invocable>(
            constructors.size
        )
        for (i in constructors.indices) {
            // Ignore constructor if the declaring class is not public
            if (!Modifier.isPublic(
                    constructors[i].getDeclaringClass().modifiers
                )
            ) {
                continue
            }
            accessibleConstructors
                .add(InvocableConstructor(constructors[i]))
        }
        if (clazz.isInterface) {
            accessibleConstructors.add(InvocableProxy(clazz))
        }
        if (!accessibleConstructors.isEmpty()) {
            result["new"] = InvocableAccessor(
                clazz,
                accessibleConstructors
            )
        }

        // Properties
        val beanInfo: BeanInfo
        try {
            beanInfo = Introspector.getBeanInfo(clazz)
        } catch (e: IntrospectionException) {
            throw RuntimeException(e)
        }
        val propertyDescriptors = beanInfo.propertyDescriptors
        for (propertyDescriptor in propertyDescriptors) {
            // Do not overwrite fields or methods
            if (result.containsKey(propertyDescriptor.name)) {
                continue
            }

            // Attempt to find the read/write methods in a public class if the
            // declaring class is not public
            var method = propertyDescriptor.getReadMethod()
            if (method != null
                && !Modifier.isPublic(
                    method.declaringClass.modifiers
                )
            ) {
                method = getPublicClassMethod(
                    clazz, method.name,
                    method.parameterTypes
                )
                try {
                    propertyDescriptor.setReadMethod(method)
                } catch (e: IntrospectionException) {
                }
            }
            method = propertyDescriptor.getWriteMethod()
            if (method != null
                && !Modifier.isPublic(
                    method.declaringClass.modifiers
                )
            ) {
                method = getPublicClassMethod(
                    clazz, method.name,
                    method.parameterTypes
                )
                try {
                    propertyDescriptor.setWriteMethod(method)
                } catch (e: IntrospectionException) {
                }
            }

            // Do not process properties without either a read or a write method
            if (propertyDescriptor.getReadMethod() == null
                && propertyDescriptor.getWriteMethod() == null
            ) {
                continue
            }
            val name = propertyDescriptor.name
            val accessor = PropertyAccessor(clazz, propertyDescriptor)
            try {
                val declaredField = clazz.getDeclaredField(name)
                val ktProperty = declaredField.kotlinProperty
                if (ktProperty != null) {
                    val luaNames: List<String> = ktProperty.annotations.getLuaNames(name)
                    for (luaName in luaNames) {
                        result[luaName] = accessor
                    }
                }
            } catch (e: NoSuchFieldException) {
                logger.warn(e) { "[createClassAccessors] " }
            }

            result[name] = accessor
        }
        return result
    }
}
