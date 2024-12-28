package org.anime_game_servers.gi_lua

import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.script_lib.ControllerLuaContext
import org.anime_game_servers.gi_lua.script_lib.GroupEventLuaContext
import org.anime_game_servers.gi_lua.script_lib.LuaContextWrapper
import org.anime_game_servers.lua.engine.LuaScript
import org.anime_game_servers.lua.engine.LuaValue
import javax.script.ScriptException
import kotlin.jvm.Throws

object GIScriptHandler {
    // Gadgets
    @JvmStatic
    @Throws(NoSuchMethodException::class, ScriptException::class, RuntimeException::class)
    fun callControllerFunction(gadgetScript: LuaScript, functionName: String, context: ControllerLuaContext<*>, vararg args: Any): LuaValue?{
        val wrappedContext = LuaContextWrapper(context)
        return baseCallScriptFunction(gadgetScript, functionName, wrappedContext, *args)
    }


    // SceneGroups
    @JvmStatic
    @Throws(NoSuchMethodException::class, ScriptException::class, RuntimeException::class)
    fun callGroupFunction(groupScript: LuaScript, functionName: String, context: GroupEventLuaContext, args: ScriptArgs): LuaValue?{
        val wrappedContext = LuaContextWrapper(context)
        return baseCallScriptFunction(groupScript, functionName, wrappedContext, args)
    }

    @JvmStatic
    @Throws(NoSuchMethodException::class, ScriptException::class, RuntimeException::class)
    fun callGroupFunction(groupScript: LuaScript, functionName: String, context: GroupEventLuaContext, vararg args: Any): LuaValue?{
        val wrappedContext = LuaContextWrapper(context)
        return baseCallScriptFunction(groupScript, functionName, wrappedContext, *args)
    }

    @Throws(NoSuchMethodException::class, ScriptException::class, RuntimeException::class)
    private fun baseCallScriptFunction(script: LuaScript, functionName: String, wrappedContext: LuaContextWrapper, vararg args: Any): LuaValue?{
        if(functionName.isBlank()){
            throw NoSuchMethodException("Blank functionName passed to script")
        }
        if(!script.hasMethod(functionName)){
            throw NoSuchMethodException("Method $functionName not found in script")
        }
        val newArgs = arrayOfNulls<Any>(args.size + 1)
        newArgs[0] = wrappedContext
        System.arraycopy(args, 0, newArgs, 1, args.size)
        return script.callMethod(functionName, *newArgs)
    }
}