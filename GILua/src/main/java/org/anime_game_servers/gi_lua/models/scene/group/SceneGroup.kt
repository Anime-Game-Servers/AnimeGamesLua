package org.anime_game_servers.gi_lua.models.scene.group

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.gi_lua.models.constants.IOType
import org.anime_game_servers.gi_lua.models.loader.GIScriptLoader
import org.anime_game_servers.gi_lua.models.loader.SceneGroupScriptLoadParams
import org.anime_game_servers.gi_lua.models.loader.ScriptSource
import org.anime_game_servers.gi_lua.models.scene.SceneMeta
import org.anime_game_servers.gi_lua.models.scene.block.SceneGroupInfo
import org.anime_game_servers.lua.engine.LuaScript
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.jvm.optionals.getOrNull

private val logger = logger {}

class SceneGroup internal constructor(// internal
    @field:Transient val groupInfo: SceneGroupInfo
) {
    // from group script
    var monsters: Map<Int, SceneMonster>? = null // <ConfigId, Monster>
    var npcs: Map<Int, SceneNPC>? = null // <ConfigId, Npc>
    var gadgets: Map<Int, SceneGadget>? = null // <ConfigId, Gadgets>
    var triggers: Map<String, SceneTrigger>? = null // <TriggerName, Trigger>
    var regions: Map<Int, SceneRegion>? = null // <ConfigId, Region>
    var points: Map<Int, ScenePoint>? = null // <ConfigId, ScenePoint>
    var variables: List<SceneVar>? = null

    @field:LuaNames("init_config")
    var initConfig: SceneInitConfig? = null
    var suites: List<SceneSuite>? = null

    @field:LuaNames("monster_pools")
    var monsterPools: List<SceneMonsterPool>? = null

    @field:LuaNames("sight_groups")
    val sightGroups: List<List<Int>>? = null

    @field:LuaNames("suite_disk")
    var suiteDisks: Map<Int, SuiteDisk>? = null

    var garbages: SceneGarbage? = null

    @Transient
    val sceneMeta: SceneMeta = groupInfo.getSceneMeta()

    @Transient
    var loaded = false // Not an actual variable in the scripts either

    @Transient
    var script: LuaScript? = null

    fun hasGarbages(): Boolean {
        return this.garbages.isEmpty()
    }

    fun getGarbageGadgets() = this.garbages?.gadgets


    fun getSuiteByIndex(index: Int): SceneSuite? {
        if (index < 1 || index > (suites?.size ?: 0)) {
            return null
        }
        return this.suites?.get(index - 1)
    }

    fun getSuiteDiscByIndex(index: Int): SuiteDisk? {
        return this.suiteDisks?.get(index)
    }

    @Synchronized
    fun load(scriptLoader: GIScriptLoader): SceneGroup? {
        if (this.loaded) {
            return this
        }
        // Set flag here so if there is no script, we don't call this function over and over again.
        this.loaded = true
        val sceneId = sceneMeta.sceneId
        val groupId = groupInfo.id
        val blockId = groupInfo.getBlockId()
        val activityId = groupInfo.getActivityId()

        val scriptType = if (activityId == 0) ScriptSource.SCENE else ScriptSource.ACTIVITY
        val typeId = if (activityId == 0) sceneId else activityId
        val groupParams = SceneGroupScriptLoadParams(scriptType, typeId, groupId)
        if (!scriptLoader.loadData(groupParams, { cs: LuaScript ->
                this.script = cs
                // Set
                this.monsters = cs.getGlobalVariableList("monsters", SceneMonster::class.java).stream()
                    .collect(
                        Collectors.toMap(
                            Function { x: SceneMonster -> x.configId },
                            Function { y: SceneMonster -> y }
                        ) { a: SceneMonster, _: SceneMonster -> a }
                    )
                this.monsters?.values?.forEach(Consumer { m: SceneMonster ->
                    m.groupId = groupId
                    m.blockId = blockId
                    m.sceneMeta = sceneMeta
                })

                this.npcs = cs.getGlobalVariableList("npcs", SceneNPC::class.java).stream()
                    .collect(
                        Collectors.toMap(
                            Function { x: SceneNPC -> x.configId },
                            Function { y: SceneNPC -> y }
                        ) { a: SceneNPC, _: SceneNPC? -> a }
                    )
                this.npcs?.values?.forEach(Consumer { m: SceneNPC ->
                    m.groupId = groupId
                    m.blockId = blockId
                    m.sceneMeta = sceneMeta
                })

                this.gadgets = cs.getGlobalVariableList("gadgets", SceneGadget::class.java).stream()
                    .collect(
                        Collectors.toMap(
                            Function { x: SceneGadget -> x.configId },
                            Function { y: SceneGadget -> y }
                        ) { a: SceneGadget, _: SceneGadget -> a }
                    )
                this.gadgets?.values?.forEach(Consumer { m: SceneGadget ->
                    m.groupId = groupId
                    m.blockId = blockId
                    m.sceneMeta = sceneMeta
                })

                this.triggers = cs.getGlobalVariableList("triggers", SceneTrigger::class.java).stream()
                    .collect(
                        Collectors.toMap(
                            SceneTrigger::name,
                            Function { y: SceneTrigger -> y }
                        ) { a: SceneTrigger, _: SceneTrigger -> a }
                    )
                this.triggers?.values?.forEach(Consumer { t: SceneTrigger ->
                    t.groupId = groupId
                    t.blockId = blockId
                    t.sceneMeta = sceneMeta
                })

                this.suites = cs.getGlobalVariableList("suites", SceneSuite::class.java)
                this.regions = cs.getGlobalVariableList("regions", SceneRegion::class.java).stream()
                    .peek { group: SceneRegion ->
                        group.groupId = groupId
                        group.blockId = blockId
                        group.sceneMeta = sceneMeta
                    }
                    .collect(
                        Collectors.toMap(
                            Function { x: SceneRegion -> x.configId },
                            Function { y: SceneRegion -> y }
                        ) { a: SceneRegion, _: SceneRegion -> a }
                    )

                this.initConfig = cs.getGlobalVariable("init_config", SceneInitConfig::class.java)

                // Garbages
                this.garbages = cs.getGlobalVariable("garbages", SceneGarbage::class.java)

                // Add variables to suite
                this.variables = cs.getGlobalVariableList("variables", SceneVar::class.java)

                this.monsterPools =
                    cs.getGlobalVariableList("monster_pools", SceneMonsterPool::class.java)

                //this.sightGroups = cs.getGlobalVariableList("sight_groups", List<Integer>.class);
                try {
                    this.suiteDisks = cs.getGlobalVariableMap("suite_disk", Int::class.java, SuiteDisk::class.java)
                } catch (ex: Exception) {
                    this.suiteDisks = mapOf() //ArrayList<SuiteDisk>()
                    // TODO log
                }

                this.points = cs.getGlobalVariableList("points", ScenePoint::class.java).stream()
                    .peek { point: ScenePoint ->
                        point.groupId = groupId
                        point.blockId = blockId
                        point.sceneMeta = sceneMeta
                    }
                    .collect(
                        Collectors.toMap(
                            Function { x: ScenePoint -> x.configId },
                            Function { y: ScenePoint -> y }
                        ) { a: ScenePoint, _: ScenePoint -> a }
                    )

                // Add monsters and gadgets to suite
                this.suites?.forEach(Consumer { i: SceneSuite -> i.init(this) })
            })) {
            return null
        }

        logger.debug { "Successfully loaded group " + groupId + " in scene " + sceneId + "." }
        return this
    }


    fun findInitSuiteIndex(excludeIndex: Int): Int {
        return findInitSuiteIndex(0, excludeIndex)
    }

    /**
     * This returns the suite index for an initial or later group refresh, based on the initConfig
     * @param currentSuiteIndex suite index the group currently has. 0 if none is set yet or the previous one should be ignored
     * @param excludeIndex suite index to exclude from random suite selection
     * @return the suite index to move to
     */
    fun findInitSuiteIndex(currentSuiteIndex: Int, excludeIndex: Int): Int {
        if (initConfig == null) return 1
        initConfig?.let { initConfig ->
            if (initConfig.ioType == IOType.GROUP_IO_TYPE_FLOW) return initConfig.suite
            if (initConfig.randSuite) {
                if (suites.isNullOrEmpty()) {
                    // todo log
                    return 1
                }
                suites?.let { suites ->
                    if (suites.size == 1) {
                        return initConfig.suite
                    }

                    val randSuiteList = mutableListOf<Int>()
                    for (i in suites.indices) {
                        if (i == excludeIndex) continue

                        val suite = suites[i]
                        repeat(suite.randWeight) {
                            randSuiteList.add(i + 1)
                        }
                    }
                    return randSuiteList[random.nextInt(randSuiteList.size)]
                }
                return 1

            }
            val endSuite = initConfig.endSuite
            if (endSuite != 0 && endSuite == currentSuiteIndex) {
                return endSuite
            }
            return initConfig.suite
        }
        return 1
    }

    fun searchBossChestInGroup(): SceneBossChest? {
        return this.gadgets?.values?.stream()?.map<SceneBossChest> { g: SceneGadget -> g.bossChest }
            ?.filter { obj: SceneBossChest -> Objects.nonNull(obj) }
            ?.filter { bossChest: SceneBossChest -> bossChest.monsterConfigId > 0 }
            ?.findFirst()?.getOrNull()
    }

    companion object {
        private val random = Random()

        @JvmStatic
        fun of(groupInfo: SceneGroupInfo): SceneGroup {
            val group = SceneGroup(groupInfo)
            return group
        }
    }
}
