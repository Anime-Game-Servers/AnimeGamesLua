package org.anime_game_servers.lua.utils

import kotlinx.io.Source
import kotlinx.io.asSource
import kotlinx.io.buffered
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.nio.file.Files
import java.nio.file.Path

fun InputStream.asSource(): Source {
    return this.asSource().buffered()
}

@Throws(IOException::class)
fun Path.asSource(): Source {
    val stream = Files.newInputStream(this)
    return stream.asSource().buffered()
}

fun Member.getLuaNames(): List<String> {
    val luaName: MutableList<String> = mutableListOf(name)
    (this as? AnnotatedElement)?.let {
        annotations
            .filterIsInstance<LuaNames>()
            .forEach { luaNames: LuaNames ->
                if (luaNames.value.isNotEmpty()) {
                    luaName.addAll(luaNames.value)
                }
            }
    }
    return luaName
}

fun Class<*>.getLuaName()= getAnnotation(LuaNames::class.java)?.let {
        it.value.firstOrNull() ?: simpleName
    } ?: simpleName