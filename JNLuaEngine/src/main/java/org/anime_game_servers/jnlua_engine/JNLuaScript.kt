package org.anime_game_servers.jnlua_engine

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.jnlua_engine.JNLuaRequireCommonFunction.Companion.getInstance
import org.anime_game_servers.lua.models.*
import org.anime_game_servers.lua.engine.LuaScript
import org.anime_game_servers.lua.engine.LuaValue
import org.anime_game_servers.lua.engine.RequireMode
import org.terasology.jnlua.script.CompiledLuaScript
import org.terasology.jnlua.script.LuaBindings
import org.terasology.jnlua.script.LuaScriptEngine
import java.io.BufferedReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern
import javax.script.*

private val logger = logger {}

class JNLuaScript internal constructor(
    override val engine: JNLuaEngine,
    private val scriptPath: Path,
    scriptType: ScriptType
) : LuaScript {
    private val compiledScript: CompiledLuaScript?
    private val modifiedScript: String?
    private val binding: LuaBindings
    private val scriptEngine: LuaScriptEngine = engine.manager.getEngineByName("jnlua") as LuaScriptEngine
    var context: SimpleScriptContext = SimpleScriptContext()

    init {
        if (scriptType.addDefaultGlobals) {
            context.setBindings(engine.bindings, ScriptContext.GLOBAL_SCOPE)
        }
        this.binding = scriptEngine.createBindings() as LuaBindings
        context.setBindings(binding, ScriptContext.ENGINE_SCOPE)
        val luaState = binding.luaState
        luaState.converter = JNLuaConverter
        luaState.javaReflector = JNLuaReflector

        val requireFunction = getInstance(engine.scriptConfig)
        binding[requireFunction.name] = requireFunction
        if (engine.scriptConfig.enableIncludeWorkaround == RequireMode.ENABLED_WITH_WORKAROUND &&
            (scriptType == ScriptType.EXECUTABLE || scriptType == ScriptType.STATIC_EXECUTABLE || scriptType == ScriptType.ONE_TIME_EXECUTABLE)
        ) {
            this.modifiedScript = compileScriptWithWorkaround(scriptPath)
        } else {
            this.modifiedScript = null
        }

        if (scriptType.precompile) {
            if (modifiedScript != null) {
                this.compiledScript = (scriptEngine as Compilable).compile(modifiedScript) as CompiledLuaScript?
            } else {
                this.reader.use { reader ->
                    this.compiledScript = (scriptEngine as Compilable).compile(reader) as CompiledLuaScript?
                }
            }
        } else {
            this.compiledScript = null
        }
    }

    @get:Throws(IOException::class)
    private val reader: BufferedReader
        get() = Files.newBufferedReader(scriptPath)

    // todo maybe caching?
    @Throws(IOException::class, ScriptException::class)
    private fun compileScriptWithWorkaround(path: Path?): String? {
        val requireRegex = Pattern.compile("\\s*require\\s+\"(.*)\"")
        val changed = MutableBoolean(false)
        try {
            this.reader.use { reader ->
                val script = reader.lines().map { line: String ->
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
                return if (changed.getValue()) script else null
            }
        } catch (e: Exception) {
            return null
        }
    }

    override fun hasMethod(methodName: String): Boolean {
        return binding.containsKey(methodName)
    }

    @Throws(ScriptException::class, NoSuchMethodException::class)
    override fun callMethod(methodName: String, vararg args: Any?): LuaValue? {
        val result = (scriptEngine as Invocable).invokeFunction(methodName, *args)
        if (result is Boolean) {
            return if (result) BooleanLuaValue.TRUE else BooleanLuaValue.FALSE
        }

        return JNLuaValue(engine, result)
    }

    @Throws(ScriptException::class)
    override fun evaluate() {
        if (compiledScript != null) {
            compiledScript.eval(context)
        } else {
            if (modifiedScript != null) {
                scriptEngine.eval(modifiedScript, context)
            } else {
                try {
                    this.reader.use { reader ->
                        scriptEngine.eval(reader, context)
                    }
                } catch (e: IOException) {
                    throw ScriptException(e)
                }
            }
        }
    }

    override fun <T> getGlobalVariable(name: String, type: Class<T>): T? {
        return engine.serializer.toObject<T?>(type, binding[name])
    }

    override fun <T> getGlobalVariableList(name: String, type: Class<T>): List<T> {
        return engine.serializer.toList(type, binding[name])
    }

    override fun <K,V> getGlobalVariableMap(name: String, keyType: Class<K>, valueType: Class<V>): Map<K, V> {
        return engine.serializer.toMap(keyType, valueType, binding[name])
    }
}
