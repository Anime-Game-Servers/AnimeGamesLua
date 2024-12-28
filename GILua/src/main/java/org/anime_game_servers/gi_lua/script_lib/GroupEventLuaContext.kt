package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.models.scene.group.SceneGroup
import org.anime_game_servers.gi_lua.script_lib.handler.activity.*
import org.anime_game_servers.gi_lua.script_lib.handler.entites.AbilityScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupEntityHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupGadgetHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupMonsterHandler
import org.anime_game_servers.gi_lua.script_lib.handler.scene.ChallengeScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.scene.DungeonScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.scene.SceneStateScriptHandler

interface GroupEventLuaContext : LuaContext {
    fun getGroupInstance(): SceneGroup
    fun getArgs(): ScriptArgs

    fun <T: GroupEventLuaContext> getScriptLibHandler(): ScriptLibHandler<T>?
    fun <T: GroupEventLuaContext> getScriptLibHandlerProvider(): ScriptLibGroupHandlerProvider<T>

    /* callHelpers */

    fun <T>onScriptLibHandler(block: ScriptLibHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandler<GroupEventLuaContext>()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }

    /* entities */
    fun <T> onGroupAbilityHandler(block: AbilityScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGroupAbilityHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }

    fun <T> onGroupMonsterHandler(block: GroupMonsterHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGroupMonsterHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }

    fun <T> onGroupEntityHandler(block: GroupEntityHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGroupEntityHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }

    fun <T> onGroupGadgetHandler(block: GroupGadgetHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGroupGadgetHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }

    /* scene */
    fun <T> onDungeonHandler(block: DungeonScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getDungeonHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onSceneStateHandler(block: SceneStateScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getSceneStateHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onChallengeHandler(block: ChallengeScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getChallengeHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }



    fun <T> onGalleryHandler(block: GalleryScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGalleryScriptHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }

    /* activity */
    fun <T> onGroupSummerTimeHandler(block: SummerTimeScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getSummerTimeScriptHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onEffigyScriptHandler(block: EffigyScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getEffigyScriptHandlerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onFleurFairScriptHandler(block: FleurFairScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getFleurFairScriptHandlerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onFungusFighterScriptHandler(block: FungusFighterScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getFungusFighterScriptHandlerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onCharAmusementScriptHandler(block: CharAmusementScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getCharAmusementScriptHandlerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onTreasureSeelieScriptHandler(block: TreasureSeelieScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getTreasureSeelieScriptHandlerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onWinterCampScriptHandler(block: WinterCampScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getWinterCampScriptHandlerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onLanternRiteScriptHandler(block: LanternRiteScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getLanternRiteScriptHandlerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onLunaRiteScriptHandler(block: LunaRiteScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getLunaRiteScriptHandlerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }

}