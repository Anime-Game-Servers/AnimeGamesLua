package org.anime_game_servers.lua.utils

import kotlinx.io.Source
import kotlinx.io.asSource
import kotlinx.io.buffered
import java.io.IOException
import java.io.InputStream
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