package org.anime_game_servers.gi_lua.test.models.script_lib

import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.script_lib.LuaContext
import org.anime_game_servers.gi_lua.script_lib.LuaContextWrapper
import org.anime_game_servers.gi_lua.script_lib.ScriptLib
import org.anime_game_servers.gi_lua.script_lib.ScriptLibErrors
import org.anime_game_servers.gi_lua.script_lib.handler.ScriptLibStaticHandler
import org.anime_game_servers.gi_lua.test.models.LuaGroupContextImpl
import org.anime_game_servers.gi_lua.test.models.TestScriptLoader
import org.anime_game_servers.jnlua_engine.JNLuaEngine
import org.anime_game_servers.lua.engine.*
import org.anime_game_servers.luaj_engine.LuaJEngine
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class LuaJScriptLibStaticTest : ScriptLibStaticTest() {
    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = LuaJEngine(ScriptConfig(this, RequireMode.DISABLED))
    }
}

class JNLuaScriptLibStaticTest : ScriptLibStaticTest() {
    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = JNLuaEngine(ScriptConfig(this, RequireMode.DISABLED))
    }
}

@Suppress("TestFunctionName")
abstract class ScriptLibStaticTest : BaseScriptLibTest(){

    @AfterEach
    fun cleanup(){
        ScriptLib.staticHandler = null
    }

    @Suppress("DEPRECATION")
    @Test
    fun runGetEntityType(){
        val context = LuaGroupContextImpl(scriptLoader.engine, ScriptArgs(7,8))
        val wrappedContext = LuaContextWrapper(context)

        // verify not implemented check
        val testEid = 500
        ScriptLib.GetEntityType(testEid).let {
            assert(it == ScriptLibErrors.NOT_IMPLEMENTED.getValue())
        }
        // verify not implemented check for compat function
        ScriptLib.GetEntityType(wrappedContext, testEid).let {
            assert(it == ScriptLibErrors.NOT_IMPLEMENTED.getValue())
        }

        // setup handler to check calls
        var passedArgs : MutableList<Any?>? = null
        ScriptLib.staticHandler = object : ScriptLibStaticHandler {
            override fun printLog(msg: String?) {
                TODO("Not yet implemented")
            }

            override fun printContextLog(context: LuaContext, msg: String) {
                TODO("Not yet implemented")
            }

            override fun getEntityType(entityId: Int): Int {
                passedArgs = mutableListOf<Any?>().apply {
                    add(entityId)
                }
                return 0
            }
        }

        // verify handler called with valid parameters
        ScriptLib.GetEntityType(testEid).let {
            assert(it == 0)
        }
        assert(passedArgs != null)
        passedArgs?.let {
            assert(it.size == 1)
            assert(it[0] == testEid)
        }


        // verify for compat function
        passedArgs = null
        ScriptLib.GetEntityType(wrappedContext, testEid).let {
            assert(it == 0)
        }
        assert(passedArgs != null)
        passedArgs?.let {
            assert(it.size == 1)
            assert(it[0] == testEid)
        }
    }

    @Suppress("DEPRECATION")
    @Test
    fun runPrintLogTest(){
        val context = LuaGroupContextImpl(scriptLoader.engine, ScriptArgs(7,8))
        val wrappedContext = LuaContextWrapper(context)

        // verify not implemented check
        val testMsg = "test"
        ScriptLib.PrintLog(testMsg).let {
            println(it)
            assert(it == ScriptLibErrors.NOT_IMPLEMENTED.getValue())
        }
        // verify not implemented check for compat function
        ScriptLib.PrintLog(wrappedContext, testMsg).let {
            assert(it == ScriptLibErrors.NOT_IMPLEMENTED.getValue())
        }

        // setup handler to check calls
        var passedArgs : MutableList<Any?>? = null
        ScriptLib.staticHandler = object : ScriptLibStaticHandler {
            override fun printLog(msg: String?) {
                passedArgs = mutableListOf<Any?>().apply {
                    add(msg)
                }
            }

            override fun printContextLog(context: LuaContext, msg: String) {
                TODO("Not yet implemented")
            }

            override fun getEntityType(entityId: Int): Int {
                TODO("Not yet implemented")
            }
        }

        // verify handler called with valid parameters
        ScriptLib.PrintLog(testMsg).let {
            assert(it == 0)
        }
        assert(passedArgs != null)
        passedArgs?.let {
            assert(it.size == 1)
            assert(it[0] == testMsg)
        }


        // verify for compat function
        passedArgs = null
        ScriptLib.PrintLog(wrappedContext, testMsg).let {
            assert(it == 0)
        }
        assert(passedArgs != null)
        passedArgs?.let {
            assert(it.size == 1)
            assert(it[0] == testMsg)
        }
    }

    @Test
    fun runPrintContextLog(){
        val context = LuaGroupContextImpl(scriptLoader.engine, ScriptArgs(7,8))
        val wrappedContext = LuaContextWrapper(context)

        // verify not implemented check
        val testMsg = "test"
        ScriptLib.PrintContextLog(wrappedContext, testMsg).let {
            println(it)
            assert(it == ScriptLibErrors.NOT_IMPLEMENTED.getValue())
        }

        // setup handler to check calls
        var passedArgs : MutableList<Any?>? = null
        ScriptLib.staticHandler = object : ScriptLibStaticHandler {
            override fun printLog(msg: String?) {
                TODO("Not yet implemented")
            }

            override fun printContextLog(context: LuaContext, msg: String) {
                passedArgs = mutableListOf<Any?>().apply {
                    add(context)
                    add(msg)
                }
            }

            override fun getEntityType(entityId: Int): Int {
                TODO("Not yet implemented")
            }
        }

        // verify handler called with valid parameters
        ScriptLib.PrintContextLog(wrappedContext, testMsg).let {
            assert(it == 0)
        }
        assert(passedArgs != null)
        passedArgs?.let {
            assert(it.size == 2)
            assert(it[0] == context)
            assert(it[1] == testMsg)
        }
    }



    @Test
    fun callFromLuaTest(){
        val testMsg = "test"
        val testEid = 500

        val args = ScriptArgs(7,8).apply {
            param1 = testEid
            paramString1 = testMsg
        }
        val context = LuaGroupContextImpl(scriptLoader.engine, args)
        val wrappedContext = LuaContextWrapper(context)
        val script = loadScript("scriptlib/ScriptLibStaticTest.lua")


        // setup handler to check calls
        var passedPrintArgs : MutableList<Any?>? = null
        var passedPrintContextArgs : MutableList<Any?>? = null
        var passedGetEntityIdArgs : MutableList<Any?>? = null
        ScriptLib.staticHandler = object : ScriptLibStaticHandler {
            override fun printLog(msg: String?) {
                passedPrintArgs = mutableListOf<Any?>().apply {
                    add(msg)
                }
            }

            override fun printContextLog(context: LuaContext, msg: String) {
                passedPrintContextArgs = mutableListOf<Any?>().apply {
                    add(context)
                    add(msg)
                }
            }

            override fun getEntityType(entityId: Int): Int {
                passedGetEntityIdArgs = mutableListOf<Any?>().apply {
                    add(entityId)
                }
                return 0
            }
        }

        assert(script.hasMethod("testScriptLibStatic"))
        val callResult = script.callMethod("testScriptLibStatic", wrappedContext, args)!!
        assert(callResult.isTable())
        val result = callResult.asMap(String::class.java, Int::class.java)
        assert(result != null)
        assert(result!!.size == 3)
        result.forEach { (_, ret) -> assert(ret == 0) }

        passedPrintArgs?.let {
            assert(it.size == 1)
            assert(it[0] == testMsg)
        }
        passedPrintContextArgs?.let {
            assert(it.size == 2)
            assert(it[0] == context)
            assert(it[1] == testMsg)
        }
        passedGetEntityIdArgs?.let {
            assert(it.size == 1)
            assert(it[0] == testEid)
        }
        passedPrintArgs = null
        passedPrintContextArgs = null
        passedGetEntityIdArgs = null

        // verify compat functions
        assert(script.hasMethod("testScriptLibStaticCompat"))
        val compatCallResult = script.callMethod("testScriptLibStaticCompat", wrappedContext, args)!!
        assert(compatCallResult.isTable())
        val compatResult = compatCallResult.asMap(String::class.java, Int::class.java)
        assert(compatResult != null)
        assert(compatResult!!.size == 2)
        compatResult.forEach { (_, ret) -> assert(ret == 0) }
        passedPrintArgs?.let {
            assert(it.size == 1)
            assert(it[0] == testMsg)
        }
        passedGetEntityIdArgs?.let {
            assert(it.size == 1)
            assert(it[0] == testEid)
        }
    }
}
