package org.anime_game_servers.jnlua_engine;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.terasology.jnlua.Converter;
import org.terasology.jnlua.DefaultConverter;
import org.terasology.jnlua.LuaState;
import org.terasology.jnlua.NamedJavaFunction;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JNLuaConverter implements Converter {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(JNLuaConverter.class.getName());
    @Getter private static final JNLuaConverter INSTANCE = new JNLuaConverter();
    final Converter defaultConverter = DefaultConverter.getInstance();
    @Override
    public int getTypeDistance(LuaState luaState, int i, Class<?> aClass) {
        return defaultConverter.getTypeDistance(luaState, i, aClass);
    }

    @Override
    public <T> T convertLuaValue(LuaState luaState, int i, Class<T> aClass) {
        return defaultConverter.convertLuaValue(luaState, i, aClass);
    }

    @Override
    public void convertJavaObject(LuaState luaState, Object o) {
        if(o instanceof JNLuaTableMap<?,?> tableMap){
            luaState.newTable();
            for (var entry : tableMap.entrySet()) {
                val key = entry.getKey();
                if (!(key instanceof Integer) && !(key instanceof String)) {
                    continue;
                }
                convertJavaObject(luaState, entry.getValue());
                if(key instanceof String stringKey){
                    luaState.setField(-2, stringKey);
                } else if (key instanceof Integer intKey){
                    luaState.pushInteger(intKey+1);
                    luaState.setTable(-3);
                }
            }
            return;
        }

        if (o instanceof Map<?, ?> fields) {
            val first = fields.entrySet().stream().findFirst();
            if (first.isPresent() && first.get().getKey() instanceof String && first.get().getValue() instanceof Integer) {
                luaState.newTable();
                for (var entry : fields.entrySet()) {
                    luaState.pushInteger((Integer) entry.getValue());
                    luaState.setField(-2, (String) entry.getKey());
                }
                return;
            }
        } else if (o instanceof StaticClassWrapper staticClassWrapper) {
            luaState.newTable();
            val staticClass = staticClassWrapper.getStaticClass();
            val methods = staticClass.getMethods();
            val fields = staticClass.getFields();
            Arrays.stream(methods)
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .forEach(m -> {
                    class TempFunc implements NamedJavaFunction {
                        Method method;
                        TempFunc(Method method){
                            this.method = method;
                        }
                        @Override
                        public String getName() {
                            return m.getName();
                        }

                        @SneakyThrows
                        @Override
                        public int invoke(LuaState luaState) {
                            val argSize = luaState.getTop();
                            val args = new ArrayList<Object>();
                            val methodParameters = method.getParameters();
                            if(argSize != methodParameters.length){
                                // todo maybe check for and handle vararg?
                                throw new RuntimeException("invalid argument size");
                            }
                            for (int i = 0; i < argSize; ++i) {
                                val paramter = methodParameters[i];
                                val parameterClass = paramter.getType().isInterface() ? Object.class : paramter.getType();
                                args.add(luaState.checkJavaObject(i + 1, parameterClass));
                            }
                            try {
                                Object ret = method.invoke(null, args.toArray());
                                luaState.pushJavaObject(ret);
                            } catch (Exception e) {
                                logger.error(e, ()->"Error on invoking binding function. ");
                                throw e;
                            }
                            return 1;
                        }
                    }
                    val func = new TempFunc(m);
                    luaState.pushJavaFunction(func);
                    luaState.setField(-2, func.getName());
                }
            );
            Arrays.stream(fields)
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .forEach(field -> {
                    val type = field.getType();
                    try {
                        val value = field.get(null);
                        if(value instanceof Number){
                            luaState.pushNumber(((Number) value).doubleValue());
                        } else if(value instanceof String){
                            luaState.pushString((String) value);
                        } else if(value instanceof Boolean){
                            luaState.pushBoolean((Boolean) value);
                        } else {
                            luaState.pushJavaObject(value);
                        }
                        luaState.setField(-2, field.getName());
                    } catch (IllegalAccessException e) {
                        logger.error(e, ()->"Error on invoking binding function. ");
                        throw new RuntimeException(e);
                    }
                }
            );


            return;
        }
        defaultConverter.convertJavaObject(luaState, o);
    }
}
