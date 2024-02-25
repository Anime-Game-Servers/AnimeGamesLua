package org.anime_game_servers.lua.test.execution

import kotlinx.io.Source
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.annotations.lua.LuaStatic
import org.anime_game_servers.jnlua_engine.JNLuaEngine
import org.anime_game_servers.lua.engine.*
import org.anime_game_servers.lua.models.ScriptType
import org.anime_game_servers.lua.utils.asSource
import org.anime_game_servers.luaj_engine.LuaJEngine
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path


data class LuaContext(val engine: LuaEngine)

@LuaStatic
object KotlinFunctions{
    @JvmStatic
    fun expectIntArray(context:LuaContext, luaTable:Any) : Any{
        // retrieve the int array as lua table from the lua side
        val table = context.engine.getTable(luaTable)
        val array = table.getAsIntArray()
        val result = context.engine.createTable()

        assert(array.size == 5)
        array.forEachIndexed { index, value ->
            assert(index+1 == value)
            result.set(index, value)
        }
        // convert the int array back to a luaTable for lua and return it
        return result.getRawTable()
    }

    @JvmStatic
    fun expectObjectTable(context:LuaContext, luaTable:Any) : Any{
        // retrieve the int array as lua table from the lua side
        val table = context.engine.getTable(luaTable)
        val x = table.optFloat("x",0.0f)
        val y = table.optFloat("y",0.0f)
        val z = table.optFloat("z",0.0f)

        val result = context.engine.createTable()
        result.set("x", x)
        result.set("y", y)
        result.set("z", z)

        // convert the int array back to a luaTable for lua and return it
        return result.getRawTable()
    }
}


abstract class TestScriptLoader : BaseScriptLoader{
    abstract val engine: LuaEngine
    override fun getScriptPath(scriptName: String): Path? {
        val uri = ClassLoader.getSystemResource(scriptName).toURI()
        return Path.of(uri)
    }

    override fun openScript(params: BaseScriptLoader.ScriptLoadParams): Source? {
        val basePath: String = params.getBasePath()
        val scriptPath = getScriptPath(basePath) ?: return null
        if(Files.exists(scriptPath)){
            return scriptPath.asSource()
        }
        return null
    }

    override fun getScript(scriptLoadParams: BaseScriptLoader.ScriptLoadParams): LuaScript? {
        val basePath: String = scriptLoadParams.getBasePath()
        val scriptPath = getScriptPath(basePath) ?: return null
        return engine.getScript(scriptPath, scriptLoadParams.getScriptType());
    }

    override fun getRequireScriptParams(scriptName: String): BaseScriptLoader.ScriptLoadParams {
        return object : BaseScriptLoader.ScriptLoadParams {
            override fun getBaseDirectory(): String {
                return ""
            }

            override fun getScriptName(): String {
                return scriptName
            }
        }
    }
}

class LuaJTest : ParsingTest() {

    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = LuaJEngine(ScriptConfig(this, RequireMode.DISABLED))
    }

    @Test
    fun runTest(){
        callMethodTest()
    }
}

class JNLuaTest : ParsingTest() {

    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = JNLuaEngine(ScriptConfig(this, RequireMode.DISABLED))
    }

    @Test
    fun runTest(){
        callMethodTest()
    }
}

abstract class ParsingTest{
    abstract val scriptLoader : TestScriptLoader

    fun getEngine(): LuaEngine {
        return scriptLoader.engine
    }

    fun callMethodTest() {
        val engine = getEngine()
        LuaEngine.registerNamespace(this::class.java.packageName)
        engine.addGlobals()
        val uri = ClassLoader.getSystemResource("CallingJavaFunctionsTest.lua").toURI()
        val path = Path.of(uri);
        val script = engine.getScript(path, ScriptType.EXECUTABLE)

        assert(script != null)

        script!!.evaluate()
        val context = LuaContext(engine)

        assert(script.hasMethod("expectIntArray"))
        val luaValue = script.callMethod("expectIntArray", context)
        luaValue?.isLong()
    }
}
