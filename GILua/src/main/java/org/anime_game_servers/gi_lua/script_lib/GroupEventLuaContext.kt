package org.anime_game_servers.gi_lua.script_lib

import org.anime_game_servers.gi_lua.models.ScriptArgs
import org.anime_game_servers.gi_lua.models.scene.group.SceneGroup
import org.anime_game_servers.gi_lua.script_lib.handler.activity.*
import org.anime_game_servers.gi_lua.script_lib.handler.entites.AbilityScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupEntityHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupGadgetHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupMonsterHandler
import org.anime_game_servers.gi_lua.script_lib.handler.entites.GroupRegionScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.gadget.GadgetGivingScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.gadget.GadgetPlayScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.gadget.PlatformScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.other.AranaraScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.other.TowerScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.player.ExhibitionScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.player.QuestScriptHandler
import org.anime_game_servers.gi_lua.script_lib.handler.scene.*

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

    fun <T> onGroupRegionHandler(block: GroupRegionScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGroupRegionHandler()?.run {
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

    /* Gadgets*/
    fun <T> onGadgetGivingHandler(block: GadgetGivingScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGadgetGivingHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onGadgetPlayHandler(block: GadgetPlayScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGadgetPlayHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onPlatformHandler(block: PlatformScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getPlatformHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }


    /* player */
    fun <T> onQuestHandler(block: QuestScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getQuestHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onExhibitionHandler(block: ExhibitionScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getExhibitionHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }

    /* scene */
    fun <T> onChallengeHandler(block: ChallengeScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getChallengeHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onDeathZoneHandler(block: DeathZoneScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getDeathZoneHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onDungeonHandler(block: DungeonScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getDungeonHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onGalleryHandler(block: GalleryScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGalleryHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onGroupManagementHandler(block: GroupManagementScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGroupManagementHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onMonsterTideHandler(block: MonsterTideScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getMonsterTideHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onScenePlayHandler(block: ScenePlayScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getScenePlayHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onSceneStateHandler(block: SceneStateScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getSceneStateHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onSealBattleHandler(block: SealBattleScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getSealBattleHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }



    /* other */
    fun <T> onAranaraHandler(block: AranaraScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getAranaraHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onTowerHandler(block: TowerScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getTowerHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }


    /* activity */
    fun <T> onActivityHandler(block: GeneralActivityScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getActivityHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onAsterHandler(block: AsterScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getAsterHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onChannelerSlapHandler(block: ChannelerSlabScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getChannelerSlabHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onCharAmusementHandler(block: CharAmusementScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getCharAmusementHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onChessHandler(block: ChessScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getChessHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onCoinCollectHandler(block: CoinCollectScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getCoinCollectHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onCrystalLinkHandler(block: CrystalLinkScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getCrystalLinkHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onDigHandler(block: DigScriptHandler<GroupEventLuaContext>.() -> T): T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getDigHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onEffigyHandler(block: EffigyScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getEffigyHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onExpeditionHandler(block: ExpeditionScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getExpeditionHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onFleurFairHandler(block: FleurFairScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getFleurFairHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onFungusFighterHandler(block: FungusFighterScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getFungusFighterHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onGravenInnocenceHandler(block: GravenInnocenceScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getGravenInnocenceHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onHideAndSeekHandler(block: HideAndSeekScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getHideAndSeekHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onInstableSprayHandler(block: InstableSprayScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getInstableSprayHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onIrodoriChessHandler(block: IrodoriChessScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getIrodoriChessHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onLanternRiteHandler(block: LanternRiteScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getLanternRiteHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onLuminanceStoneChallengeHandler(block: LuminanceStoneChallengeScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getLuminanceStoneChallengeHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onLunaRiteHandler(block: LunaRiteScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getLunaRiteHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onMechanicusHandler(block: MechanicusScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getMechanicusHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onMichiaeMatsuriHandler(block: MichiaeMatsuriScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getMichiaeMatsuriHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onMistTrialHandler(block: MistTrialScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getMistTrialHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onPotionHandler(block: PotionScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getPotionHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onRogueDiaryHandler(block: RogueDiaryScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getRogueDiaryHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onRoguelikeHandler(block: RoguelikeScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getRoguelikeHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onSeaLampHandler(block: SeaLampScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getSeaLampHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onSummerTimeHandler(block: SummerTimeScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getSummerTimeHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onTreasureMapHandler(block: TreasureMapScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getTreasureMapHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onTreasureSeelieHandler(block: TreasureSeelieScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getTreasureSeelieHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onUgcDungeonHandler(block: UgcDungeonScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getUgcDungeonHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onVintageHandler(block: VintageScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getVintageHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }
    fun <T> onWinterCampHandler(block: WinterCampScriptHandler<GroupEventLuaContext>.() -> T) : T {
        return getScriptLibHandlerProvider<GroupEventLuaContext>().getWinterCampHandler()?.run {
            return block()
        } ?: ScriptLibErrors.NOT_IMPLEMENTED.getValue() as T
    }

}