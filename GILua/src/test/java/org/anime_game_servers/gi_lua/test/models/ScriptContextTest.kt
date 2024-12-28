package org.anime_game_servers.gi_lua.test.models

import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.models.scene.group.SceneGroup
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.script_lib.LuaContextWrapper
import org.anime_game_servers.gi_lua.script_lib.ScriptLibGroupHandlerProvider
import org.anime_game_servers.gi_lua.script_lib.ScriptLibHandler
import org.anime_game_servers.jnlua_engine.JNLuaEngine
import org.anime_game_servers.lua.engine.*
import org.anime_game_servers.lua.models.ScriptType
import org.anime_game_servers.luaj_engine.LuaJEngine
import org.junit.jupiter.api.Test
import java.nio.file.Path


data class LuaGroupContextImpl(val myEngine: LuaEngine, val scriptArgs: ScriptArgs, val ouid: Int = 0
) : GroupEventLuaContext{
    override fun getGroupInstance(): SceneGroup {
        TODO("Not yet implemented")
    }

    override fun getArgs() = scriptArgs

    override fun <T : GroupEventLuaContext> getScriptLibHandler(): ScriptLibHandler<T>? {
        TODO("Not yet implemented")
    }

    override fun <T : GroupEventLuaContext> getScriptLibHandlerProvider(): ScriptLibGroupHandlerProvider<T> {
        TODO("Not yet implemented")
    }

    override fun getEngine() = myEngine

    override fun uid(): Int = scriptArgs.uid
    override fun sourceEntityId(): Int = scriptArgs.sourceEid
    override fun targetEntityId(): Int = scriptArgs.targetEid
    override fun ownerUid(): Int = ouid
}

class LuaJContextTest : ScriptContextTest() {

    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = LuaJEngine(ScriptConfig(this, RequireMode.DISABLED))
    }

    @Test
    fun runTest(){
        checkScriptContext()
    }
}

class JNLuaContextTest : ScriptContextTest() {

    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = JNLuaEngine(ScriptConfig(this, RequireMode.DISABLED))
    }

    @Test
    fun runTest(){
        checkScriptContext()
    }
}

abstract class ScriptContextTest{
    abstract val scriptLoader : TestScriptLoader

    fun getEngine(): LuaEngine {
        return scriptLoader.engine
    }

    fun checkScriptContext() {
        val engine = getEngine()
        LuaEngine.registerNamespace(this::class.java.packageName)
        engine.addGlobals()
        val uri = ClassLoader.getSystemResource("ScriptContextTest.lua").toURI()
        val path = Path.of(uri);
        val script = engine.getScript(path, ScriptType.EXECUTABLE)

        assert(script != null)

        script!!.evaluate()
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
        val context = LuaGroupContextImpl(engine, args, ouid = 55)
        val contextWrapper = LuaContextWrapper(context)

        assert(script.hasMethod("testScriptContext"))
        val luaValue = script.callMethod("testScriptContext", contextWrapper, args)!!
        assert(luaValue.isTable())
        val result = luaValue.asObject(ScriptArgs::class.java)
        assert(result != null)
        /*assert(result!!.param1 == args.param1)
        assert(result.param2 == args.param2)
        assert(result.param3 == args.param3)
        assert(result.param4 == args.param4)
        assert(result.sourceEid == args.sourceEid)
        assert(result.targetEid == args.targetEid)
        assert(result.groupId == args.groupId)
        assert(result.uid == args.uid)
        assert(result.type == args.type)
        assert(result.paramString1 == args.paramString1)
        assert(result.source == args.source)*/
    }
}
