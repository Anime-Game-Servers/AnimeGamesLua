package org.anime_game_servers.luaj_engine

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.lua.engine.LuaEngine
import org.anime_game_servers.lua.engine.LuaScript
import org.anime_game_servers.lua.engine.RequireMode
import org.anime_game_servers.lua.models.BooleanLuaValue
import org.anime_game_servers.lua.models.MutableBoolean
import org.anime_game_servers.lua.models.ScriptType
import org.anime_game_servers.luaj_engine.coerse.CoerceJavaToLua
import org.luaj.vm2.LuaValue
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern
import javax.annotation.Nonnull
import javax.script.Bindings
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptException

class LuaJScript(override val engine: LuaJEngine, scriptPath: Path, scriptType: ScriptType?) : LuaScript {
    private val compiledScript: CompiledScript
    private val modifiedScript: String?
    private val binding: Bindings

    init {
        if (engine.scriptConfig.enableIncludeWorkaround == RequireMode.ENABLED_WITH_WORKAROUND && (scriptType == ScriptType.EXECUTABLE || scriptType == ScriptType.STATIC_EXECUTABLE || scriptType == ScriptType.ONE_TIME_EXECUTABLE)) {
            val result = compileScriptWithWorkaround(scriptPath)
            this.compiledScript = result.first
            this.modifiedScript = result.second
        } else {
            this.compiledScript = (engine.engine as Compilable).compile(Files.newBufferedReader(scriptPath))
            this.modifiedScript = null
        }
        this.binding = engine.engine.createBindings()
    }

    // todo maybe caching?
    @Throws(IOException::class, ScriptException::class)
    private fun compileScriptWithWorkaround(path: Path): Pair<CompiledScript, String?> {
        val requireRegex = Pattern.compile("\\s*require\\s+\"(.*)\"")
        val changed = MutableBoolean(false)
        Files.newBufferedReader(path).use { reader ->
            val script = reader.lines().map<String> { line: String? ->
                val result = requireRegex.matcher(line)
                if (result.matches()) {
                    val requireBasePath =
                        engine.scriptConfig.scriptLoader.getRequireScriptParams(result.group(1)).getBasePath()
                    val requirePath = engine.scriptConfig.scriptLoader.getScriptPath(requireBasePath)
                    if (requirePath == null) {
                        logger.warn { "Could not find require script " + result.group(1) + " for script " + path }
                        return@map line
                    }
                    try {
                        Files.newBufferedReader(requirePath).use { requireReader ->
                            val requireScript =
                                requireReader.lines().reduce { a: String?, b: String? -> a + "\n" + b }.orElse(line)
                            changed.setValue(true)
                            return@map requireScript
                        }
                    } catch (e: IOException) {
                        return@map line
                    }
                } else {
                    return@map line
                }
            }.reduce { a: String?, b: String? -> a + "\n" + b }.orElse("")
            val modifiedScript = if (changed.getValue()) script else null
            return Pair<CompiledScript, String?>((engine.engine as Compilable).compile(script), modifiedScript)
        }
    }

    override fun hasMethod(@Nonnull methodName: String): Boolean {
        return binding.containsKey(methodName) && (binding.get(methodName) as LuaValue).isfunction()
    }

    override fun callMethod(
        @Nonnull methodName: String,
        vararg args: Any?
    ): org.anime_game_servers.lua.engine.LuaValue? {
        val function = binding[methodName] as LuaValue?
        if (function == null || !function.isfunction()) {
            logger.warn { "Attempted to call method " + methodName + " on script " + this + " but it does not exist or is not a function" }
            return BooleanLuaValue.FALSE
        }

        val luaArgs: Array<LuaValue?>?

        luaArgs = arrayOfNulls<LuaValue>(args.size)
        for (i in args.indices) luaArgs[i] = CoerceJavaToLua.coerce(args[i])

        return LuaJValue(engine, function.invoke(luaArgs).arg1())
    }

    @Throws(ScriptException::class)
    override fun evaluate() {
        this.compiledScript.eval(this.binding)
    }

    override fun <T> getGlobalVariable(name: String, type: Class<T>): T? {
        return engine.serializer.toObject<T>(type, binding[name])
    }

    override fun <T> getGlobalVariableList(name: String, type: Class<T>): List<T> {
        return engine.serializer.toList<T>(type, binding[name])
    }

    override fun <K,V> getGlobalVariableMap(name: String, keyType: Class<K>, valueType: Class<V>): Map<K, V> {
        return engine.serializer.toMap(keyType, valueType, binding[name])
    }

    companion object {
        private val logger = logger(LuaJScript::class.java.getName())
    }
}
