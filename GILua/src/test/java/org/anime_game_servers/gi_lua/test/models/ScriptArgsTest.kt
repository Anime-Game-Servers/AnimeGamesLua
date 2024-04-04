package org.anime_game_servers.gi_lua.test.models

import kotlinx.io.Source
import org.anime_game_servers.core.base.annotations.lua.LuaStatic
import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.models.loader.GIScriptLoader
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
    fun checkScriptArgs(context:LuaContext, luaTable:Any) : Any{
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
    fun getScriptArgs(context:LuaContext) : Any{
        // retrieve the int array as lua table from the lua side
        /*val table = context.engine.getTable(luaTable)
        val x = table.optInt("x",0)
        val y = table.optInt("y",0)
        val z = table.optInt("z",0)*/

        val result = context.engine.createTable()
        /*result.set("x", x)
        result.set("y", y)
        result.set("z", z)*/

        // convert the int array back to a luaTable for lua and return it
        return result.getRawTable()
    }
}

abstract class TestScriptLoader : GIScriptLoader {
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
}

class LuaJTest : ScriptArgsTest() {

    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = LuaJEngine(ScriptConfig(this, RequireMode.DISABLED))
    }

    @Test
    fun runTest(){
        checkScriptArgs()
    }
}

class JNLuaTest : ScriptArgsTest() {

    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = JNLuaEngine(ScriptConfig(this, RequireMode.DISABLED))
    }

    @Test
    fun runTest(){
        checkScriptArgs()
    }
}

abstract class ScriptArgsTest{
    abstract val scriptLoader : TestScriptLoader

    fun getEngine(): LuaEngine {
        return scriptLoader.engine
    }

    fun checkScriptArgs() {
        val engine = getEngine()
        LuaEngine.registerNamespace(this::class.java.packageName)
        engine.addGlobals()
        val uri = ClassLoader.getSystemResource("ScriptArgsTest.lua").toURI()
        val path = Path.of(uri);
        val script = engine.getScript(path, ScriptType.EXECUTABLE)

        assert(script != null)

        script!!.evaluate()
        val context = LuaContext(engine)
        val args = ScriptArgs(7,8).apply {
            param1 = 1
            param2 = 2
            param3 = 3
            param4 = 4
            sourceEid = 5
            targetEid = 6
            uid = 9
            paramString1 = "paramString1"
            source = "source"
        }

        assert(script.hasMethod("testScriptArgs"))
        val luaValue = script.callMethod("testScriptArgs", context, args)!!
        assert(luaValue.isTable())
        val result = luaValue.asObject(ScriptArgs::class.java)
        assert(result != null)
        assert(result!!.param1 == args.param1)
        assert(result.param2 == args.param2)
        assert(result.param3 == args.param3)
        assert(result.param4 == args.param4)
        assert(result.sourceEid == args.sourceEid)
        assert(result.targetEid == args.targetEid)
        assert(result.groupId == args.groupId)
        assert(result.uid == args.uid)
        assert(result.type == args.type)
        assert(result.paramString1 == args.paramString1)
        assert(result.source == args.source)
    }
}
