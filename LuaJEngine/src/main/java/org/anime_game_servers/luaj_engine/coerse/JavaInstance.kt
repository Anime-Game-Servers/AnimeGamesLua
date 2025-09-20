package org.anime_game_servers.luaj_engine.coerse

import org.anime_game_servers.luaj_engine.coerse.JavaClass.Companion.forClass
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaUserdata
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.CoerceLuaToJava

class JavaInstance(instance: Any?) : LuaUserdata(instance) {
    var jclass: JavaClass? = null

    override fun get(key: LuaValue): LuaValue {
        if (jclass == null) jclass = forClass(m_instance.javaClass)
        val f = jclass?.getField(key)
        if (f != null) try {
            return CoerceJavaToLua.coerce(f.get(m_instance))
        } catch (e: Exception) {
            throw LuaError(e)
        }
        val m = jclass?.getMethod(key)
        if (m != null) return m
        val c = jclass?.getInnerClass(key)
        if (c != null) return forClass(c)
        return super.get(key)
    }

    override fun set(key: LuaValue, value: LuaValue) {
        if (jclass == null) jclass = forClass(m_instance.javaClass)
        val f = jclass?.getField(key)
        if (f != null) try {
            f.set(m_instance, CoerceLuaToJava.coerce(value, f.type))
            return
        } catch (e: Exception) {
            throw LuaError(e)
        }
        super.set(key, value)
    }
}
