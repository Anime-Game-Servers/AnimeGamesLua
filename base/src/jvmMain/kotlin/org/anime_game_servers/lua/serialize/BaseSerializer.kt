package org.anime_game_servers.lua.serialize

import com.esotericsoftware.reflectasm.ConstructorAccess
import com.esotericsoftware.reflectasm.MethodAccess
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.TypeVariable
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.Nonnull


abstract class BaseSerializer : Serializer {
    abstract fun <T> toList(type: Class<T>?, obj: Any?): List<T>?

    abstract fun <T> toObject(type: Class<T>?, obj: Any?): T

    abstract fun <T> toMap(type: Class<T>?, obj: Any?): Map<String?, T>?

    protected fun getSetterName(fieldName: String?): String? {
        if (fieldName == null || fieldName.length == 0) {
            return null
        }
        if (fieldName.length == 1) {
            return "set" + fieldName.uppercase(Locale.getDefault())
        }
        return "set" + fieldName[0].uppercaseChar() + fieldName.substring(1)
    }

    protected fun <T> cacheType(type: Class<T>): Map<String, FieldMeta> {
        if (fieldMetaCache.containsKey(type)) {
            return fieldMetaCache[type]!!
        }
        if (!constructorCache.containsKey(type)) {
            constructorCache.putIfAbsent(type, ConstructorAccess.get(type))
        }
        val methodAccess = Optional.ofNullable(methodAccessCache[type]).orElse(MethodAccess.get(type))
        methodAccessCache.putIfAbsent(type, methodAccess)

        val fieldMetaMap = HashMap<String, FieldMeta>()
        val methodNameSet = HashSet(Arrays.stream(methodAccess.methodNames).toList())

        var classtype: Class<*>? = type
        while (classtype != null) {
            Arrays.stream(classtype.declaredFields)
                .forEach { field: Field ->
                    val name = field.name
                    val luaNames = getLuaNames(field)
                    val fieldMeta = if (methodNameSet.contains(getSetterName(name))) {
                        val setter = getSetterName(name)
                        val index = methodAccess.getIndex(setter)
                        FieldMeta(name, luaNames, setter, index, field.type, field)
                    } else {
                        field.isAccessible = true
                        FieldMeta(name, luaNames, null, -1, field.type, field)
                    }
                    luaNames.forEach {
                        fieldMetaMap[it] = fieldMeta
                    }
                }
            classtype = classtype.superclass
        }

        Arrays.stream(type.fields)
            .filter { field: Field -> !fieldMetaMap.containsKey(field.name) }
            .filter { field: Field -> methodNameSet.contains(getSetterName(field.name)) }
            .forEach { field: Field ->
                val name = field.name
                val luaNames = getLuaNames(field)
                val setter = getSetterName(name)
                val index = methodAccess.getIndex(setter)
                val fieldMeta = FieldMeta(name, luaNames, setter, index, field.type, field)
                luaNames.forEach {
                    fieldMetaMap[it] = fieldMeta
                }
            }

        fieldMetaCache[type] = fieldMetaMap
        return fieldMetaMap
    }

    protected fun getLuaNames(field: Field): List<String> {
        val luaName: MutableList<String> = mutableListOf(field.name)
        field.annotations
            .filterIsInstance<LuaNames>()
            .forEach { luaNames: LuaNames ->
                if (luaNames.value.isNotEmpty()) {
                    luaName.addAll(luaNames.value)
                }
            }
        return luaName
    }

    protected fun set(`object`: Any?, @Nonnull fieldMeta: FieldMeta, methodAccess: MethodAccess?, value: Int) {
        try {
            if (methodAccess != null && fieldMeta.setter != null) {
                methodAccess.invoke(`object`, fieldMeta.index, value)
            } else if (fieldMeta.field != null) {
                fieldMeta.field.setInt(`object`, value)
            }
        } catch (ex: Exception) {
            logger.warn(ex) { "Failed to set field " + fieldMeta.name + " of type " + fieldMeta.type + " to value " + value }
        }
    }

    protected fun set(`object`: Any?, @Nonnull fieldMeta: FieldMeta, methodAccess: MethodAccess?, value: Double) {
        try {
            if (methodAccess != null && fieldMeta.setter != null) {
                methodAccess.invoke(`object`, fieldMeta.index, value)
            } else if (fieldMeta.field != null) {
                fieldMeta.field.setDouble(`object`, value)
            }
        } catch (ex: Exception) {
            logger.warn(ex) { "Failed to set field " + fieldMeta.name + " of type " + fieldMeta.type + " to value " + value }
        }
    }

    protected fun set(`object`: Any?, @Nonnull fieldMeta: FieldMeta, methodAccess: MethodAccess?, value: Float) {
        try {
            if (methodAccess != null && fieldMeta.setter != null) {
                methodAccess.invoke(`object`, fieldMeta.index, value)
            } else if (fieldMeta.field != null) {
                fieldMeta.field.setFloat(`object`, value)
            }
        } catch (ex: Exception) {
            logger.warn(ex) { "Failed to set field " + fieldMeta.name + " of type " + fieldMeta.type + " to value " + value }
        }
    }

    protected fun set(`object`: Any?, @Nonnull fieldMeta: FieldMeta, methodAccess: MethodAccess?, value: Long) {
        try {
            if (methodAccess != null && fieldMeta.setter != null) {
                methodAccess.invoke(`object`, fieldMeta.index, value)
            } else if (fieldMeta.field != null) {
                fieldMeta.field.setLong(`object`, value)
            }
        } catch (ex: Exception) {
            logger.warn(ex) { "Failed to set field " + fieldMeta.name + " of type " + fieldMeta.type + " to value " + value }
        }
    }

    protected fun set(`object`: Any?, @Nonnull fieldMeta: FieldMeta, methodAccess: MethodAccess?, value: Boolean) {
        try {
            if (methodAccess != null && fieldMeta.setter != null) {
                methodAccess.invoke(`object`, fieldMeta.index, value)
            } else if (fieldMeta.field != null) {
                fieldMeta.field.setBoolean(`object`, value)
            }
        } catch (ex: Exception) {
            logger.warn(ex) { "Failed to set field " + fieldMeta.name + " of type " + fieldMeta.type + " to value " + value }
        }
    }

    protected fun set(`object`: Any?, @Nonnull fieldMeta: FieldMeta, methodAccess: MethodAccess?, value: Any) {
        try {
            if (methodAccess != null && fieldMeta.setter != null) {
                methodAccess.invoke(`object`, fieldMeta.index, value)
            } else if (fieldMeta.field != null) {
                fieldMeta.field[`object`] = value
            }
        } catch (ex: Exception) {
            logger.warn(ex) { "Failed to set field " + fieldMeta.name + " of type " + fieldMeta.type + " to value " + value }
        }
    }

    protected fun getCollectionType(type: Class<*>, field: Field?): Class<*>? {
        if (field == null) {
            return type.typeParameters[0].javaClass
        }
        val fieldType = field.genericType
        if (fieldType is ParameterizedType) {
            return (fieldType as ParameterizedType).actualTypeArguments[0] as Class<*>
        }

        return null
    }

    protected fun getMapTypes(type: Class<*>, field: Field?): Pair<Class<*>, Class<*>>? {
        var key: Class<*>? = null
        var value: Class<*>? = null
        if (field == null) {
            val types = type.getTypeParameters()
            if (types.size < 2) {
                return null
            }
            key = types.javaClass
            value = types.javaClass
        } else {
            val fieldType = field.genericType
            if (fieldType is ParameterizedType) {
                val types = fieldType.actualTypeArguments
                if (types.size < 2) {
                    return null
                }
                key = types[0] as Class<*>
                value = types[1] as Class<*>
            }
        }

        if (key != null && value != null) {
            return Pair(key, value)
        }

        return null
    }

    companion object {
        private val logger = logger(BaseSerializer::class.java.name)

        @JvmField
        protected val methodAccessCache: MutableMap<Class<*>, MethodAccess> = ConcurrentHashMap()
        @JvmField
        protected val constructorCache: MutableMap<Class<*>, ConstructorAccess<*>> = ConcurrentHashMap()
        @JvmField
        protected val fieldMetaCache: MutableMap<Class<*>, Map<String, FieldMeta>> = ConcurrentHashMap()
    }
}
