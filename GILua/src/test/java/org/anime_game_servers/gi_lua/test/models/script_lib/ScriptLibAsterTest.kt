package org.anime_game_servers.gi_lua.test.models.script_lib

import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.script_lib.LuaContext
import org.anime_game_servers.gi_lua.script_lib.LuaContextWrapper
import org.anime_game_servers.gi_lua.script_lib.ScriptLib
import org.anime_game_servers.gi_lua.script_lib.ScriptLibErrors
import org.anime_game_servers.gi_lua.script_lib.handler.ScriptLibStaticHandler
import org.anime_game_servers.gi_lua.script_lib.handler.activity.AsterScriptHandler
import org.anime_game_servers.gi_lua.test.models.LuaGroupContextImpl
import org.anime_game_servers.gi_lua.test.models.TestScriptLibHandlerProvider
import org.anime_game_servers.gi_lua.test.models.TestScriptLoader
import org.anime_game_servers.jnlua_engine.JNLuaEngine
import org.anime_game_servers.lua.engine.*
import org.anime_game_servers.luaj_engine.LuaJEngine
import org.junit.jupiter.api.Test

class LuaJScriptLibAsterTest : ScriptLibAsterTest() {
    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = LuaJEngine(ScriptConfig(this, RequireMode.DISABLED))
    }
}

class JNLuaScriptLibAsterTest : ScriptLibAsterTest() {
    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = JNLuaEngine(ScriptConfig(this, RequireMode.DISABLED))
    }
}

/**
 * TODO verify also that its properly called from lua
 */
abstract class ScriptLibAsterTest : BaseScriptLibTest(){

    @Test
    fun runCreateAsterMidGeneralRewardGadgetTest(){
        val context = LuaGroupContextImpl(scriptLoader.engine, ScriptArgs(7,8))
        val handlerProvider = context.getScriptLibHandlerProvider<LuaGroupContextImpl>() as TestScriptLibHandlerProvider
        val wrappedContext = LuaContextWrapper(context)
        val validCfgId = 5
        val validDiffId = 7

        val table = scriptLoader.engine.createTable()
        // verify not implemented check
        ScriptLib.CreateAsterMidGeneralRewardGadget(wrappedContext, table.getRawTable()).let {
            assert(it == ScriptLibErrors.NOT_IMPLEMENTED.getValue())
        }

        // setup handler to check calls
        var passedArgs : MutableList<Any>? = null
        handlerProvider.asterScriptHandler = object : AsterScriptHandler<LuaGroupContextImpl>{
            override fun createAsterMidGeneralRewardGadget(
                context: LuaGroupContextImpl,
                configId: Int,
                difficultyId: Int
            ): Int {
                passedArgs = mutableListOf<Any>().apply {
                    add(context)
                    add(configId)
                    add(difficultyId)
                }
                return 0
            }
        }

        // verify invalid cfgId
        table.apply {
            set("config_id", -1)
        }
        ScriptLib.CreateAsterMidGeneralRewardGadget(wrappedContext, table.getRawTable()).let {
            assert(it == ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue())
        }
        assert(passedArgs == null)

        // verify valid cfgId and missing difficulty_id
        table.apply {
            set("config_id", validCfgId)
        }
        ScriptLib.CreateAsterMidGeneralRewardGadget(wrappedContext, table.getRawTable()).let {
            assert(it == ScriptLibErrors.INVALID_PARAMETER_TABLE_CONTENT.getValue())
        }
        assert(passedArgs == null)

        // verify handler called with valid parameters
        table.apply {
            set("difficulty_id", validDiffId)
        }
        ScriptLib.CreateAsterMidGeneralRewardGadget(wrappedContext, table.getRawTable()).let {
            assert(it == 0)
        }
        assert(passedArgs != null)
        passedArgs?.let {
            assert(it.size == 3)
            assert(it[0] == context)
            assert(it[1] == validCfgId)
            assert(it[2] == validDiffId)
        }
    }

    @Test
    fun callFromLuaTest(){
        val cfgId = 200
        val difficultyId = 500

        val args = ScriptArgs(7,8).apply {
            param1 = cfgId
            param2 = difficultyId
        }
        val context = LuaGroupContextImpl(scriptLoader.engine, args)
        val handlerProvider = context.getScriptLibHandlerProvider<LuaGroupContextImpl>() as TestScriptLibHandlerProvider
        val wrappedContext = LuaContextWrapper(context)
        val script = loadScript("scriptlib/ScriptLibAsterTest.lua")


        // setup handler to check calls
        var passedArgs : MutableList<Any>? = null
        handlerProvider.asterScriptHandler = object : AsterScriptHandler<LuaGroupContextImpl>{
            override fun createAsterMidGeneralRewardGadget(
                context: LuaGroupContextImpl,
                configId: Int,
                difficultyId: Int
            ): Int {
                passedArgs = mutableListOf<Any>().apply {
                    add(context)
                    add(configId)
                    add(difficultyId)
                }
                return 15
            }
        }

        assert(script.hasMethod("testScriptLibAster"))
        val callResult = script.callMethod("testScriptLibAster", wrappedContext, args)!!
        assert(callResult.asInteger() == 15)

        passedArgs?.let {
            assert(it.size == 3)
            assert(it[0] == context)
            assert(it[1] == cfgId)
            assert(it[2] == difficultyId)
        }
    }
}
