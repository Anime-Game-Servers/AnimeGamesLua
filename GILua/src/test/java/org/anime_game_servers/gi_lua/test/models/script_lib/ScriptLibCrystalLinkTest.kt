package org.anime_game_servers.gi_lua.test.models.script_lib

import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.script_lib.LuaContextWrapper
import org.anime_game_servers.gi_lua.script_lib.ScriptLib
import org.anime_game_servers.gi_lua.script_lib.ScriptLibErrors
import org.anime_game_servers.gi_lua.script_lib.handler.activity.CrystalLinkScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.activity.CrystalLinkTeamSetupParams
import org.anime_game_servers.gi_lua.test.models.LuaGroupContextImpl
import org.anime_game_servers.gi_lua.test.models.TestScriptLibHandlerProvider
import org.anime_game_servers.gi_lua.test.models.TestScriptLoader
import org.anime_game_servers.jnlua_engine.JNLuaEngine
import org.anime_game_servers.lua.engine.*
import org.anime_game_servers.luaj_engine.LuaJEngine
import org.junit.jupiter.api.Test

class LuaJScriptLibCrystalLinkTest : ScriptLibCrystalLinkTest() {
    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = LuaJEngine(ScriptConfig(this, RequireMode.DISABLED))
    }
}

class JNLuaScriptLibCrystalLinkTest : ScriptLibCrystalLinkTest() {
    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = JNLuaEngine(ScriptConfig(this, RequireMode.DISABLED))
    }
}

/**
 * TODO verify also that its properly called from lua
 */
abstract class ScriptLibCrystalLinkTest : BaseScriptLibTest(){

    @Test
    fun runCreateCrystalLinkMidGeneralRewardGadgetTest(){
        val context = LuaGroupContextImpl(scriptLoader.engine, ScriptArgs(7,8))
        val handlerProvider = context.getScriptLibHandlerProvider<LuaGroupContextImpl>() as TestScriptLibHandlerProvider
        val wrappedContext = LuaContextWrapper(context)
        val validCfgId = 5
        val validInitProgress = 7

        val table = scriptLoader.engine.createTable()
        // verify not implemented check
        ScriptLib.CrystalLinkDungeonTeamSetUp(wrappedContext, 2 ,table.getRawTable()).let {
            assert(it == ScriptLibErrors.NOT_IMPLEMENTED.getValue())
        }

        // setup handler to check calls
        var passedArgs : MutableList<Any>? = null
        handlerProvider.crystalLinkScriptHandler = object : CrystalLinkScriptHandler<LuaGroupContextImpl>{
            override fun crystalLinkDungeonTeamSetUp(
                context: LuaGroupContextImpl,
                configId: Int,
                setupParams: CrystalLinkTeamSetupParams
            ): Int {
                passedArgs = mutableListOf<Any>().apply {
                    add(context)
                    add(configId)
                    add(setupParams.initGalleryProgress)
                }
                return 0
            }
        }

        // verify invalid cfgId
        table.apply {
            set("init_gallery_progress", -1)
        }
        ScriptLib.CrystalLinkDungeonTeamSetUp(wrappedContext, -1,table.getRawTable()).let {
            assert(it == ScriptLibErrors.INVALID_PARAMETER.getValue())
        }
        assert(passedArgs == null)

        // verify valid cfgId and missing initProgress
        table.apply {}
        passedArgs?.let {
            assert(it.size == 3)
            assert(it[0] == context)
            assert(it[1] == validCfgId)
            assert(it[2] == 0)
        }
        passedArgs?.clear()
        assert(passedArgs == null)

        // verify handler called with valid parameters
        table.apply {
            set("init_gallery_progress", validInitProgress)
        }
        ScriptLib.CrystalLinkDungeonTeamSetUp(wrappedContext, validCfgId,table.getRawTable()).let {
            assert(it == 0)
        }
        assert(passedArgs != null)
        passedArgs?.let {
            assert(it.size == 3)
            assert(it[0] == context)
            assert(it[1] == validCfgId)
            assert(it[2] == validInitProgress)
        }
    }

    @Test
    fun callFromLuaTest(){
        val cfgId = 200
        val initGalleryProgress = 500

        val args = ScriptArgs(7,8).apply {
            param1 = cfgId
            param2 = initGalleryProgress
        }
        val context = LuaGroupContextImpl(scriptLoader.engine, args)
        val handlerProvider = context.getScriptLibHandlerProvider<LuaGroupContextImpl>() as TestScriptLibHandlerProvider
        val wrappedContext = LuaContextWrapper(context)
        val script = loadScript("scriptlib/ScriptLibCrystalLinkTest.lua")


        // setup handler to check calls
        var passedArgs : MutableList<Any>? = null
        handlerProvider.crystalLinkScriptHandler = object : CrystalLinkScriptHandler<LuaGroupContextImpl>{
            override fun crystalLinkDungeonTeamSetUp(
                context: LuaGroupContextImpl,
                configId: Int,
                setupParams: CrystalLinkTeamSetupParams
            ): Int {
                passedArgs = mutableListOf<Any>().apply {
                    add(context)
                    add(configId)
                    add(setupParams.initGalleryProgress)
                }
                return 15
            }
        }

        assert(script.hasMethod("testScriptLibCrystalLink"))
        val callResult = script.callMethod("testScriptLibCrystalLink", wrappedContext, args)!!
        assert(callResult.asInteger() == 15)

        passedArgs?.let {
            assert(it.size == 3)
            assert(it[0] == context)
            assert(it[1] == cfgId)
            assert(it[2] == initGalleryProgress)
        }
    }
}
