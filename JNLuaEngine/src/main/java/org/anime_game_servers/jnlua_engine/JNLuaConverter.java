package org.anime_game_servers.jnlua_engine;

import lombok.Getter;
import lombok.val;
import org.terasology.jnlua.Converter;
import org.terasology.jnlua.DefaultConverter;
import org.terasology.jnlua.LuaState;

import java.util.Map;

public class JNLuaConverter implements Converter {
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
        } /*else if (o instanceof ScriptLib lib) {
            luaState.newTable();
            val methods = lib.getClass().getMethods();
            Arrays.stream(methods).forEach(m -> {
                    val isStatic = Modifier.isStatic(m.getModifiers());
                    class TempFunc implements NamedJavaFunction {
                        @Override
                        public String getName() {
                            return m.getName();
                        }

                        @SneakyThrows
                        @Override
                        public int invoke(LuaState luaState) {
                            var argSize = luaState.getTop();
                            List<Object> args = new ArrayList<>();
                            for (int i = 0; i < argSize; ++i) {
                                args.add(luaState.checkJavaObject(i + 1, Object.class));
                            }
                            try {
                                Object caller = isStatic ? null : o;
                                Object ret = m.invoke(caller, args.toArray());
                                luaState.pushJavaObject(ret);
                            } catch (Exception e) {
                                //LuaEngine.logger.error("Error on invoking binding function. ", e);
                                throw e;
                            }
                            return 1;
                        }
                    }
                    val func = new TempFunc();
                    luaState.pushJavaFunction(func);
                    luaState.setField(-2, func.getName());
                }
            );

            return;
        }*/
        defaultConverter.convertJavaObject(luaState, o);
    }
}
