package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupGadgetHandler
import org.anime_game_servers.lua.engine.LuaEngine

class LuaContextWrapper(val luaContext: LuaContext) {
    val engine: LuaEngine = luaContext.engine

    // fields used by some scripts
    @JvmField
    val uid: Int = luaContext.uid()
    @JvmField
    @field:LuaNames("source_entity_id")
    val sourceEntityId: Int = luaContext.sourceEntityId()
    @JvmField
    @field:LuaNames("target_entity_id")
    val targetEntityId: Int = luaContext.targetEntityId()
    @JvmField
    @field:LuaNames("owner_uid")
    val ownerUid: Int = luaContext.ownerUid()

    fun <T> onGroupContext(block: GroupEventLuaContext.() -> T) : T {
        return (luaContext as? GroupEventLuaContext)?.run {
            return block()
        } ?: ScriptLibErrors.INVALID_CONTEXT_TYPE.getValue() as T
    }
    fun <T> onControllerContext(block: ControllerLuaContext<Any?>.() -> T) : T {
        return (luaContext as? ControllerLuaContext<Any?>)?.run {
            return block()
        } ?: ScriptLibErrors.INVALID_CONTEXT_TYPE.getValue() as T
    }

    fun <T> onTypedContext(groupBlock: GroupEventLuaContext.() -> T, controllerBlock: ControllerLuaContext<Any?>.() -> T, errorBlock: (LuaContext.() -> Unit)? = null) : T {
        if (luaContext is GroupEventLuaContext) {
            luaContext.run {
                return groupBlock()
            }
        } else if (luaContext is ControllerLuaContext<*>) {
            (luaContext as ControllerLuaContext<Any?>)?.run {
                return controllerBlock()
            }
        } else {
            errorBlock?.invoke(luaContext)
        }
        return ScriptLibErrors.INVALID_CONTEXT_TYPE.getValue() as T
    }
}