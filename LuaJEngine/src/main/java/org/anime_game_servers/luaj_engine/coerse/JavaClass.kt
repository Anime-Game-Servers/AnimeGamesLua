package org.anime_game_servers.luaj_engine.coerse

import org.anime_game_servers.lua.utils.getLuaNames
import org.luaj.vm2.LuaValue
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*

class JavaClass internal constructor(c: Class<*>?) : org.luaj.vm2.lib.jse.JavaClass(c) {
    override fun addField(fi: Field, m: MutableMap<LuaValue?, Field?>) {
        if (Modifier.isPublic(fi.modifiers)) {
            val names: List<String> = fi.getLuaNames()
            for (name in names) {
                m[valueOf(name)] = fi
            }
            try {
                if (!fi.isAccessible()) fi.setAccessible(true)
            } catch (s: SecurityException) {
            }
        }
    }

    companion object {
        private val classes: MutableMap<Class<*>, JavaClass> =
            Collections.synchronizedMap<Class<*>, JavaClass>(HashMap<Class<*>, JavaClass>())

        @JvmStatic
        fun forClass(c: Class<*>): JavaClass {
            return classes.computeIfAbsent(c) { c: Class<*> -> JavaClass(c) }
        }
    }
}
