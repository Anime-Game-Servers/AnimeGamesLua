package org.anime_game_servers.jnlua_engine

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import lombok.SneakyThrows
import org.terasology.jnlua.Converter
import org.terasology.jnlua.DefaultConverter
import org.terasology.jnlua.LuaState
import org.terasology.jnlua.NamedJavaFunction
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.Parameter
import java.util.*

private val logger = logger {}

object JNLuaConverter : Converter {
    val defaultConverter: Converter = DefaultConverter.getInstance()
    override fun getTypeDistance(luaState: LuaState?, i: Int, aClass: Class<*>?): Int {
        return defaultConverter.getTypeDistance(luaState, i, aClass)
    }

    override fun <T> convertLuaValue(luaState: LuaState?, i: Int, aClass: Class<T?>?): T? {
        return defaultConverter.convertLuaValue<T?>(luaState, i, aClass)
    }

    override fun convertJavaObject(luaState: LuaState, o: Any?) {
        if (o is JNLuaTableMap<*, *>) {
            luaState.newTable()
            for (entry in o.entries) {
                val key: Any? = entry.key
                if (key !is Int && key !is String) {
                    continue
                }
                convertJavaObject(luaState, entry.value)
                if (key is String) {
                    luaState.setField(-2, key)
                } else if (key is Int) {
                    luaState.rawSet(-2, key)
                }
            }
            return
        }

        if (o is Map<*, *>) {
            val first = o.entries.stream().findFirst()
            if (first.isPresent && first.get().key is String && first.get().value is Int) {
                luaState.newTable()
                for (entry in o.entries) {
                    luaState.pushInteger((entry.value as Int).toLong())
                    luaState.setField(-2, entry.key as String)
                }
                return
            }
        } else if (o is StaticClassWrapper) {
            luaState.newTable()
            val staticClass = o.staticClass
            val methods = staticClass.getMethods()
            val fields = staticClass.getFields()
            Arrays.stream(methods)
                .filter { method: Method -> Modifier.isStatic(method.modifiers) }
                .forEach { m: Method ->
                    class TempFunc(var method: Method) : NamedJavaFunction {
                        var overloads: MutableList<Method> = Arrays.stream(methods)
                            .filter { it: Method -> m.name == it.name && it !== method }
                            .toList()

                        override fun getName(): String {
                            return m.name
                        }

                        @SneakyThrows
                        override fun invoke(luaState: LuaState): Int {
                            val argSize = luaState.top
                            val args = ArrayList<Any?>()
                            var methodToCall: Method = method
                            var methodParameters = method.parameters
                            if (argSize != methodParameters.size) {
                                if (overloads.isEmpty()) {
                                    // todo maybe check for and handle vararg?
                                    throw RuntimeException("invalid argument size")
                                }
                                // TODO compare types for overloads with the same number of arguments
                                methodToCall = overloads.stream()
                                    .filter { it: Method -> it.parameterCount == argSize }
                                    .findFirst()
                                    .orElseThrow()
                                methodParameters = methodToCall.parameters
                            }
                            for (i in 0..<argSize) {
                                val paramter: Parameter = methodParameters[i]
                                val parameterClass =
                                    if (paramter.getType().isInterface) Any::class.java else paramter.getType()
                                args.add(luaState.checkJavaObject(i + 1, parameterClass))
                            }
                            try {
                                val ret = methodToCall.invoke(null, *args.toTypedArray())
                                luaState.pushJavaObject(ret)
                            } catch (e: Exception) {
                                logger.error(e) { "Error on invoking binding function. " }
                                throw e
                            }
                            return 1
                        }
                    }

                    val func = TempFunc(m)
                    luaState.pushJavaFunction(func)
                    luaState.setField(-2, func.name)
                }
            Arrays.stream(fields)
                .filter { field: Field -> Modifier.isStatic(field.modifiers) }
                .forEach { field: Field ->
                    val type = field.type
                    try {
                        val value = field.get(null)
                        if (value is Number) {
                            luaState.pushNumber(value.toDouble())
                        } else if (value is String) {
                            luaState.pushString(value)
                        } else if (value is Boolean) {
                            luaState.pushBoolean(value)
                        } else {
                            luaState.pushJavaObject(value)
                        }
                        luaState.setField(-2, field.name)
                    } catch (e: IllegalAccessException) {
                        logger.error(e) { "Error on invoking binding function. " }
                        throw RuntimeException(e)
                    }
                }


            return
        }
        defaultConverter.convertJavaObject(luaState, o)
    }
}
