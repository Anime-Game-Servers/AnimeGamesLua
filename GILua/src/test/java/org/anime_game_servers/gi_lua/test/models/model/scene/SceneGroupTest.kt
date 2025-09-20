package org.anime_game_servers.gi_lua.test.models.model.scene

import org.anime_game_servers.core.gi.models.Vector
import org.anime_game_servers.gi_lua.models.PositionImpl
import org.anime_game_servers.gi_lua.models.constants.EventType
import org.anime_game_servers.gi_lua.models.constants.FlowGroupSubType.GROUP_SUB_FLOW_TYPE_DEFAULT
import org.anime_game_servers.gi_lua.models.constants.IOType.GROUP_IO_TYPE_DEFAULT
import org.anime_game_servers.gi_lua.models.constants.IOType.GROUP_IO_TYPE_FLOW
import org.anime_game_servers.gi_lua.models.constants.ScriptGadgetState
import org.anime_game_servers.gi_lua.models.constants.ScriptRegionShape
import org.anime_game_servers.gi_lua.models.constants.VisionLevelType
import org.anime_game_servers.gi_lua.models.constants.VisionLevelType.VISION_LEVEL_NORMAL
import org.anime_game_servers.gi_lua.models.constants.VisionLevelType.VISION_LEVEL_REMOTE
import org.anime_game_servers.gi_lua.models.loader.SceneGroupScriptLoadParams
import org.anime_game_servers.gi_lua.models.scene.block.SceneGroupInfo
import org.anime_game_servers.gi_lua.models.scene.group.*
import org.anime_game_servers.gi_lua.test.models.TestScriptLoader
import org.anime_game_servers.jnlua_engine.JNLuaEngine
import org.anime_game_servers.lua.engine.*
import org.anime_game_servers.luaj_engine.LuaJEngine
import org.junit.jupiter.api.Test

class LuaJSceneGroupTest : SceneGroupTest() {
    override val scriptLoader = object : TestScriptLoader() {
        override val engine: LuaEngine = LuaJEngine(ScriptConfig(this, RequireMode.DISABLED))
        override fun getScript(scriptLoadParams: BaseScriptLoader.ScriptLoadParams): LuaScript? {
            return engine.getScript(getScriptPath(scriptLoadParams), scriptLoadParams.getScriptType())
        }

        init {
            this.addDefaultsForEngine(engine)
        }
    }
}

class JNLuaSceneGroupTest : SceneGroupTest() {
    override val scriptLoader = object : TestScriptLoader() {
        override val engine: LuaEngine = JNLuaEngine(ScriptConfig(this, RequireMode.DISABLED))
        override fun getScript(scriptLoadParams: BaseScriptLoader.ScriptLoadParams): LuaScript? {
            return engine.getScript(getScriptPath(scriptLoadParams), scriptLoadParams.getScriptType())
        }

        init {
            this.addDefaultsForEngine(engine)
        }
    }
}

/**
 * TODO verify also that its properly called from lua
 */
abstract class SceneGroupTest : BaseSceneModelTest() {

    fun getScriptPath(scriptLoadParams: BaseScriptLoader.ScriptLoadParams): String {
        return "models/scene/" + when (scriptLoadParams) {
            is SceneGroupScriptLoadParams -> {
                when (scriptLoadParams.groupId) {
                    1 -> "GroupSuiteEmpty.lua"
                    2 -> "GroupSuiteFilled.lua"
                    3 -> "GroupSuiteDiskEmpty.lua"
                    4 -> "GroupSuiteDiskFilled.lua"
                    5 -> "GroupModels.lua"
                    else -> ""
                }
            }

            else -> ""
        }
    }

    fun getBaseGroup(id: Int): SceneGroup {
        return SceneGroup(SceneGroupInfo.of(-1, id, -1, 0))
    }

    @Test
    fun parseEmptySuiteGroup() {
        LuaEngine.registerNamespace(this::class.java.packageName)
        val group = getBaseGroup(1)
        group.load(scriptLoader)

        group.run {
            assert(monsters.isNullOrEmpty())
            assert(npcs.isNullOrEmpty())
            assert(gadgets.isNullOrEmpty())
            assert(triggers.isNullOrEmpty())
            assert(regions.isNullOrEmpty())
            assert(points.isNullOrEmpty())
            assert(variables.isNullOrEmpty())
            assert(suites != null)
            checkSuiteContent(suites!![0], randWeight = 100)
            assert(initConfig != null)
            initConfig?.run {
                assert(suite == 1)
                assert(endSuite == 0)
                assert(ioType == GROUP_IO_TYPE_DEFAULT)
                assert(subFlowType == subFlowType)
                assert(secureSuiteIndex == 0)
                assert(!randSuite)
            }
            assert(monsterPools.isNullOrEmpty())
            assert(sightGroups == null)
            assert(suiteDisks.isNullOrEmpty())
            assert(garbages != null)
        }
    }

    @Test
    fun parseFilledSuiteGroup() {
        LuaEngine.registerNamespace(this::class.java.packageName)
        val group = getBaseGroup(2)
        group.load(scriptLoader)

        group.run {
            assert(suites != null)
            assert(suites!!.size == 4)
            checkSuiteContent(
                suites!![0], randWeight = 90,
                monsters = listOf(1001),
                npcs = listOf(2001),
                gadgets = listOf(3001),
                regions = listOf(4001),
                triggers = listOf("ENTER_REGION_5001")
            )
            checkSuiteContent(
                suites!![1], randWeight = 50,
                monsters = listOf(1002, 1003),
                npcs = listOf(2002, 2003),
                gadgets = listOf(3002, 3003),
                regions = listOf(4002, 4003),
                triggers = listOf("ENTER_REGION_5002", "AVATAR_NEAR_PLATFORM_5003")
            )
            checkSuiteContent(
                suites!![2], isBanRefresh = true, randWeight = 10,
                monsters = listOf(1001, 1002),
                npcs = listOf(2001, 2002),
                gadgets = listOf(3001, 3002),
                regions = listOf(4001, 4002),
                triggers = listOf("ENTER_REGION_5001", "ENTER_REGION_5002")
            )
            checkSuiteContent(suites!![3], randWeight = 1)
        }
    }

    fun checkSuiteContent(
        suite: SceneSuite, isBanRefresh: Boolean = false, randWeight: Int = 0, monsters: List<Int> = emptyList(),
        npcs: List<Int> = emptyList(), gadgets: List<Int> = emptyList(), regions: List<Int> = emptyList(),
        triggers: List<String> = emptyList()
    ) {
        assert(suite.banRefresh == isBanRefresh)
        assert(suite.randWeight == randWeight)

        assert(suite.monsters.size == monsters.size)
        assert(suite.sceneMonsters.size == monsters.size)
        assert(suite.gadgets.size == npcs.size)
        assert(suite.sceneGadgets.size == npcs.size)
        assert(suite.triggers.size == triggers.size)
        assert(suite.sceneTriggers.size == triggers.size)
        assert(suite.regions.size == regions.size)
        assert(suite.sceneRegions.size == regions.size)
        assert(suite.npcs.size == npcs.size)
        assert(suite.sceneNPCs.size == npcs.size)

        monsters.forEach {
            assert(suite.monsters.contains(it))
            assert(suite.sceneMonsters.find { sceneMonster -> sceneMonster.configId == it } != null)
        }
        gadgets.forEach {
            assert(suite.gadgets.contains(it))
            assert(suite.sceneGadgets.find { sceneGadget -> sceneGadget.configId == it } != null)
        }
        triggers.forEach {
            assert(suite.triggers.contains(it))
            assert(suite.sceneTriggers.find { sceneTrigger -> sceneTrigger.name == it } != null)
        }
        regions.forEach {
            assert(suite.regions.contains(it))
            assert(suite.sceneRegions.find { sceneRegion -> sceneRegion.configId == it } != null)
        }
        npcs.forEach {
            assert(suite.npcs.contains(it))
            assert(suite.sceneNPCs.find { sceneNpc -> sceneNpc.configId == it } != null)
        }
    }

    @Test
    fun parseEmptySuiteDiskGroup() {
        LuaEngine.registerNamespace(this::class.java.packageName)
        val group = getBaseGroup(3)
        group.load(scriptLoader)

        group.run {
            assert(monsters.isNullOrEmpty())
            assert(npcs.isNullOrEmpty())
            assert(gadgets.isNullOrEmpty())
            assert(triggers.isNullOrEmpty())
            assert(regions.isNullOrEmpty())
            assert(points.isNullOrEmpty())
            assert(variables.isNullOrEmpty())
            assert(suites != null)
            assert(suites!!.isEmpty())
            assert(suiteDisks != null)
            assert(suiteDisks!!.size == 1)
            suiteDisks!![1]!!.run {
                assert(monsters.isEmpty())
                assert(gadgets.isEmpty())
                assert(triggers.isEmpty())
                assert(regions.isEmpty())
                assert(npcs.isEmpty())
                assert(variables.isEmpty())
            }
            assert(initConfig != null)
            initConfig?.run {
                assert(suite == 1)
                assert(endSuite == 0)
                assert(ioType == GROUP_IO_TYPE_FLOW)
                assert(subFlowType == GROUP_SUB_FLOW_TYPE_DEFAULT)
                assert(secureSuiteIndex == 0)
                assert(!randSuite)
            }
            assert(monsterPools.isNullOrEmpty())
            assert(sightGroups == null)
            assert(garbages != null)
        }
    }

    @Test
    fun parseFilledSuiteDiskGroup() {
        LuaEngine.registerNamespace(this::class.java.packageName)
        val group = getBaseGroup(4)
        group.load(scriptLoader)

        group.run {
            assert(initConfig != null)
            initConfig?.run {
                assert(suite == 1)
                assert(endSuite == 0)
                assert(ioType == GROUP_IO_TYPE_FLOW)
                assert(subFlowType == GROUP_SUB_FLOW_TYPE_DEFAULT)
                assert(secureSuiteIndex == 0)
                assert(!randSuite)
            }

            assert(suiteDisks != null)
            checkSuiteDiskContent(
                suiteDisks!![1]!!,
                monsters = listOf(SuiteDiskMonster(1001)),
                gadgets = listOf(SuiteDiskGadget(3001)),
                regions = listOf(4001),
                npcs = listOf(2001),
                triggers = listOf("ENTER_REGION_5001"),
                variables = listOf(SuiteDiskVariable(6001, "first", 0))
            )
            checkSuiteDiskContent(
                suiteDisks!![2]!!,
                monsters = listOf(SuiteDiskMonster(1002), SuiteDiskMonster(1003)),
                gadgets = listOf(SuiteDiskGadget(3002, state = 100), SuiteDiskGadget(3003)),
                regions = listOf(4002, 4003),
                npcs = listOf(2002, 2003),
                triggers = listOf("ENTER_REGION_5002", "AVATAR_NEAR_PLATFORM_5003"),
                variables = listOf(
                    SuiteDiskVariable(6002, "second", 2),
                    SuiteDiskVariable(6003, "third", 3, noRefresh = true)
                )
            )
            checkSuiteDiskContent(
                suiteDisks!![3]!!,
                monsters = listOf(SuiteDiskMonster(1001), SuiteDiskMonster(1002)),
                gadgets = listOf(SuiteDiskGadget(3001), SuiteDiskGadget(3002, state = 100)),
                regions = listOf(4001, 4002),
                npcs = listOf(2001, 2002),
                triggers = listOf("ENTER_REGION_5001", "ENTER_REGION_5002"),
                variables = listOf(
                    SuiteDiskVariable(6001, "first", 0),
                    SuiteDiskVariable(6002, "third", 2, noRefresh = true)
                )
            )
            checkSuiteDiskContent(suiteDisks!![4]!!)
        }
    }


    fun checkSuiteDiskContent(
        suiteDisk: SuiteDisk,
        monsters: List<SuiteDiskMonster> = emptyList(),
        gadgets: List<SuiteDiskGadget> = emptyList(),
        regions: List<Int> = emptyList(),
        npcs: List<Int> = emptyList(),
        triggers: List<String> = emptyList(),
        variables: List<SuiteDiskVariable> = emptyList()
    ) {

        assert(suiteDisk.monsters.size == monsters.size)
        assert(suiteDisk.gadgets.size == npcs.size)
        assert(suiteDisk.regions.size == regions.size)
        assert(suiteDisk.npcs.size == npcs.size)
        assert(suiteDisk.triggers.size == triggers.size)
        assert(suiteDisk.variables.size == variables.size)

        monsters.forEach {
            assert(suiteDisk.monsters.contains(it))
            assert(suiteDisk.monsters.find { diskMonster -> diskMonster.configId == it.configId } != null)
        }
        gadgets.forEach {
            assert(suiteDisk.gadgets.contains(it))
            assert(suiteDisk.gadgets.find { diskGadget -> diskGadget.configId == it.configId } != null)
        }
        regions.forEach {
            assert(suiteDisk.regions.contains(it))
        }
        npcs.forEach {
            assert(suiteDisk.npcs.contains(it))
        }
        triggers.forEach {
            assert(suiteDisk.triggers.contains(it))
        }
        variables.forEach {
            //assert(suiteDisk.variables.contains(it))
            assert(suiteDisk.variables.find { diskVariable -> diskVariable.configId == it.configId } != null)
        }
    }

    @Test
    fun parseGroupModels() {
        LuaEngine.registerNamespace(this::class.java.packageName)
        val group = getBaseGroup(5)
        group.load(scriptLoader)

        group.run {
            verifyMonsterModels(monsters!!)
            verifyNpcModels(npcs!!)
            verifyGadgetModels(gadgets!!)
            verifyTriggerModels(triggers!!)
            verifyRegionModels(regions!!)
            verifyPointModels(points!!)
            verifyVariableModels(variables!!)
        }
    }

    // for parseGroupModels checks parsed Models match expected values
    fun verifyMonsterModels(monsters: Map<Int, SceneMonster>) {
        assert(monsters.size == 2)
        val first = monsters[1001]!!
        compareBase(
            first, configId = 1001, position = PositionImpl(x = 11.1f, y = 111f, z = -1.11f),
            rot = PositionImpl(x = 11f, y = 110.000f, z = -100.000f)
        )
        compareCreature(first)
        compareMonsters(first, monsterId = 100001)

        val second = monsters[1002]!!
        compareBase(
            second,
            configId = 1002,
            position = PositionImpl(x = 22.2f, y = -222f, z = 2.22f),
            rot = PositionImpl(x = 22f, y = -220.000f, z = 200.000f),
            areaId = 202,
        )
        compareCreature(
            second,
            level = 2,
            visionLevel = VISION_LEVEL_REMOTE,
            markFlag = 2222,
            isOneOff = true,
            oneOffResetVersion = 5,
            dropTag = "drop2",
            dropId = 33033,
            guestBanDrop = 1,
            serverGlobalValueConfig = mapOf("SGV_ONE" to 1f, "SGV_TWO" to 2.2f),
        )
        compareMonsters(
            second,
            monsterId = 100002,
            poseId = 222,
            poseLogicState = "poseState2",
            disableWander = true,
            titleId = 3030303,
            specialNameId = 330033,
            affix = listOf(33, 333),
            isElite = true,
            climateAreaId = 44,
            aiConfigId = 4004004,
            killScore = 444,
            speedLevel = 4,
            tag = 44444444L,
            isLightConfig = true,
            sightGroupIndex = 55,
        )
    }

    fun verifyNpcModels(npcs: Map<Int, SceneNPC>) {
        assert(npcs.size == 2)
        val firstNpc = npcs[2001]!!
        compareNpc(
            firstNpc,
            configId = 2001,
            npcId = 20001,
            areaId = 1,
            position = PositionImpl(x = -11.1f, y = 1.1f, z = 11f),
            rot = PositionImpl(x = 1f, y = 11.100f, z = -11.000f)
        )

        val secondNpc = npcs[2002]!!
        compareNpc(
            secondNpc,
            configId = 2002,
            npcId = 20002,
            areaId = 2,
            position = PositionImpl(x = -22.2f, y = 2.2f, z = 22f),
            rot = PositionImpl(x = 2.000f, y = 22.2f, z = -22.000f)
        )
    }

    fun verifyGadgetModels(gadgets: Map<Int, SceneGadget>) {
        assert(gadgets.size == 3)
        val first = gadgets[3001]!!
        compareBase(
            first, configId = 3001, position = PositionImpl(x = 1.1f, y = 100f, z = -1.0f),
            rot = PositionImpl(x = 0.000f, y = 0.000f, z = 0.000f)
        )
        compareCreature(first)
        compareGadget(first, gadgetId = 200001)

        val second = gadgets[3002]!!
        compareBase(
            second,
            configId = 3002,
            position = PositionImpl(x = 2.2f, y = 200f, z = -2.0f),
            rot = PositionImpl(x = 2.000f, y = 0.200f, z = -2.000f),
            areaId = 302
        )
        compareCreature(
            second,
            level = 32,
            visionLevel = VISION_LEVEL_REMOTE,
            markFlag = 222,
            dropTag = "drop32",
            guestBanDrop = 1
        )
        compareGadget(second, gadgetId = 200002)

        val third = gadgets[3003]!!
        compareBase(
            third, configId = 3003, position = PositionImpl(x = 2.2f, y = 200f, z = -2.0f),
            rot = PositionImpl(x = 2.000f, y = 0.200f, z = -2.000f)
        )
        compareCreature(
            third,
            isOneOff = true,
            oneOffResetVersion = 303,
            dropId = 303003,
            serverGlobalValueConfig = mapOf("SGV_ONE" to 1f, "SGV_TWO" to 2.2f)
        )
        compareGadget(
            third,
            gadgetId = 200003,
            state = ScriptGadgetState.GearStart,
            pointType = 2003,
            bossChest = SceneBossChest(
                lifeTime = 2003, monsterConfigId = 2001,
                resin = 25,
                takeNum = 3
            ),
            chestDropId = 3303003,
            interactId = 3333,
            draftId = 4003003,
            routeId = 5003003,
            startRoute = false,
            isUsePointArray = true,
            persistent = true,
            showCutscene = true,
            owner = 3002,
            autopick = true,
            explore = Explore(name = "gadget3", exp = 1),
            arguments = listOf(3, 4, 5),
            isGuestCanOperate = true,
            isBlossomChest = true,
            isEnableInteract = false,
            talkState = 6300303,
            fishingId = 333,
            fishingAreas = listOf(100030, 100033),
            crucibleConfig = CrucibleConfig(
                duration = 300,
                startCd = 3,
                progressStageList = listOf(0, 300, 3300, 33333),
                mpPlayId = 33
            ),
            offeringConfig = OfferingConfig(33),
            worktopConfig = WorktopConfig(initOptions = setOf(33, 333, 3333), isPersistent = true)
        )
    }


    fun verifyTriggerModels(triggers: Map<String, SceneTrigger>) {
        assert(triggers.size == 4)
        val first = triggers["ENTER_REGION_5001"]!!
        compareTrigger(
            first,
            configId = 5001,
            name = "ENTER_REGION_5001",
            event = EventType.EVENT_ENTER_REGION,
            source = "",
            condition = "",
            action = ""
        )

        val second = triggers["ENTER_REGION_5002"]!!
        compareTrigger(
            second,
            configId = 5002,
            name = "ENTER_REGION_5002",
            event = EventType.EVENT_ENTER_REGION,
            source = "52",
            condition = "condition_EVENT_ENTER_REGION_5002",
            action = "action_EVENT_ENTER_REGION_5002",
            triggerCount = 0,
            tag = "522",
            tlogTag = "logtag_5002",
            forbidGuest = false
        )

        val third = triggers["AVATAR_NEAR_PLATFORM_5003"]!!
        compareTrigger(
            third,
            configId = 5003,
            name = "AVATAR_NEAR_PLATFORM_5003",
            event = EventType.EVENT_AVATAR_NEAR_PLATFORM,
            source = "53",
            condition = "",
            action = "",
            triggerCount = 3,
            tag = "63",
        )

        val fourth = triggers["GALLERY_ALL_AVATAR_DIE_5004"]!!
        compareTrigger(
            fourth,
            configId = 5004,
            name = "GALLERY_ALL_AVATAR_DIE_5004",
            event = EventType.EVENT_GALLERY_ALL_AVATAR_DIE,
            source = "54",
            condition = "",
            action = "",
            triggerCount = 4,
            tag = "64",
        )
    }


    fun verifyRegionModels(variables: Map<Int, SceneRegion>) {
        assert(variables.size == 5)
        val first = variables[4001]!!
        compareBase(
            first,
            configId = 4001,
            position = PositionImpl(x = 101.1f, y = 11.11f, z = -101.11f)
        )
        compareRegion(
            first,
            shape = ScriptRegionShape.SPHERE,
            radius = 10,
        )

        val second = variables[4002]!!
        compareBase(
            second,
            configId = 4002,
            position = PositionImpl(x = 101.1f, y = 11.11f, z = -101.11f)
        )
        compareRegion(
            second,
            shape = ScriptRegionShape.CYLINDER,
            radius = 200,
            height = 22f
        )

        val third = variables[4003]!!
        compareBase(
            third,
            configId = 4003,
            position = PositionImpl(x = 101.1f, y = 11.11f, z = -101.11f)
        )
        compareRegion(
            third,
            shape = ScriptRegionShape.CUBIC,
            size = PositionImpl(x = 303.3f, y = 33.33f, z = -303.33f)
        )

        val fourth = variables[4004]!!
        compareBase(
            fourth,
            configId = 4004,
            position = PositionImpl(x = 101.1f, y = 11.11f, z = -101.11f)
        )
        compareRegion(
            fourth,
            shape = ScriptRegionShape.POLYGON,
            height = 44f,
            pointArray = listOf(
                PositionImpl(x = 4444.4f, y = 4.4f),
                PositionImpl(x = 44.44f, y = 400f),
                PositionImpl(x = -4.4f, y = -444.906f)
            )
        )

        val fifth = variables[4005]!!
        compareBase(
            fifth,
            configId = 4005,
            position = PositionImpl(x = 101.1f, y = 11.11f, z = -101.11f),
            areaId = 45
        )
        compareRegion(
            fifth,
            shape = ScriptRegionShape.SPHERE,
            radius = 50,
            abilityGroupList = listOf("ability1", "ability2"),
            teamAbilityGroup = listOf("teamAbility1", "teamAbility2"),
            isTriggerReloadGroup = true,
            visionTypeList = listOf(5, 55, 555)
        )
    }


    fun verifyPointModels(variables: Map<Int, ScenePoint>) {
        assert(variables.size == 2)
        val first = variables[7001]!!
        comparePoint(
            first,
            configId = 7001,
            pos = PositionImpl(x = 7171f, y = 71.71f, z = -71.71f),
            rot = PositionImpl(x = 7f, y = 1f, z = 7.1f),
        )

        val second = variables[7002]!!
        comparePoint(
            second,
            configId = 7002,
            pos = PositionImpl(x = 7171f, y = 71.71f, z = -71.71f),
            rot = PositionImpl(x = 7f, y = 1f, z = 7.1f),
            areaId = 2,
            tag = 72L
        )
    }


    fun verifyVariableModels(variables: List<SceneVar>) {
        assert(variables.size == 2)
        val first = variables[0]
        compareVariable(
            first,
            configId = 6001,
            name = "first",
            value = 0,
        )

        val second = variables[1]
        compareVariable(
            second,
            configId = 6002,
            name = "second",
            value = 2,
            noRefresh = true
        )
    }


    fun compareBase(
        sceneObject: SceneObject,
        configId: Int = 0,
        areaId: Int = 0,
        position: Vector? = null,
        rot: Vector? = null
    ): Boolean {
        assert(sceneObject.configId == configId)
        assert(sceneObject.areaId == areaId)
        assert(sceneObject.pos == position)
        assert(sceneObject.rot == rot)
        return true
    }

    fun compareCreature(
        sceneCreature: SceneCreature,
        level: Int = 0,
        visionLevel: VisionLevelType = VISION_LEVEL_NORMAL,
        markFlag: Int = 0,
        isOneOff: Boolean = false,
        oneOffResetVersion: Int = 0,
        dropTag: String? = null,
        dropId: Int = 0,
        guestBanDrop: Int = 0,
        serverGlobalValueConfig: Map<String, Float>? = null,
    ): Boolean {
        assert(sceneCreature.level == level)
        assert(sceneCreature.visionLevel == visionLevel)
        assert(sceneCreature.markFlag == markFlag)
        assert(sceneCreature.isOneOff == isOneOff)
        assert(sceneCreature.oneOffResetVersion == oneOffResetVersion)
        assert(sceneCreature.dropTag == dropTag)
        assert(sceneCreature.dropId == dropId)
        assert(sceneCreature.guestBanDrop == guestBanDrop)
        assert(sceneCreature.serverGlobalValueConfig == serverGlobalValueConfig)
        return true
    }

    fun compareMonsters(
        sceneMonster: SceneMonster,
        monsterId: Int = 0,
        poseId: Int = 0,
        poseLogicState: String? = null,
        disableWander: Boolean = false,
        titleId: Int = 0,
        specialNameId: Int = 0,
        affix: List<Int>? = null,
        isElite: Boolean = false,
        climateAreaId: Int = 0,
        aiConfigId: Int = 0,
        killScore: Int = 0,
        speedLevel: Int = 0,
        tag: Long = 0L,
        isLightConfig: Boolean = false,
        sightGroupIndex: Int = 0,
    ): Boolean {
        assert(sceneMonster.monsterId == monsterId)
        assert(sceneMonster.poseId == poseId)
        assert(sceneMonster.poseLogicState == poseLogicState)
        assert(sceneMonster.disableWander == disableWander)
        assert(sceneMonster.titleId == titleId)
        assert(sceneMonster.specialNameId == specialNameId)
        assert(sceneMonster.affix == affix)
        assert(sceneMonster.isElite == isElite)
        assert(sceneMonster.climateAreaId == climateAreaId)
        assert(sceneMonster.aiConfigId == aiConfigId)
        assert(sceneMonster.killScore == killScore)
        assert(sceneMonster.speedLevel == speedLevel)
        assert(sceneMonster.tag == tag)
        assert(sceneMonster.isLightConfig == isLightConfig)
        assert(sceneMonster.sightGroupIndex == sightGroupIndex)
        return true
    }

    fun compareNpc(
        sceneNpc: SceneNPC,
        npcId: Int = 0,
        configId: Int = 0,
        areaId: Int = 0,
        position: Vector? = null,
        rot: Vector? = null
    ): Boolean {
        assert(sceneNpc.npcId == npcId)
        assert(sceneNpc.configId == configId)
        assert(sceneNpc.areaId == areaId)
        assert(sceneNpc.pos == position)
        assert(sceneNpc.rot == rot)
        return true
    }

    fun compareGadget(
        sceneGadget: SceneGadget,
        gadgetId: Int = 0,
        state: Int = 0,
        pointType: Int = 0,
        bossChest: SceneBossChest? = null,
        chestDropId: Int = 0,
        interactId: Int = 0,
        draftId: Int = 0,
        routeId: Int = 0,
        startRoute: Boolean = true,
        isUsePointArray: Boolean = false,
        persistent: Boolean = false,
        showCutscene: Boolean = false,
        owner: Int = 0,
        autopick: Boolean = false,
        explore: Explore? = null,
        arguments: List<Int>? = null,
        isGuestCanOperate: Boolean = false,
        isBlossomChest: Boolean = false,
        isEnableInteract: Boolean = true,
        talkState: Int = 0,
        fishingId: Int = 0,
        fishingAreas: List<Int>? = null,
        crucibleConfig: CrucibleConfig? = null,
        offeringConfig: OfferingConfig? = null,
        worktopConfig: WorktopConfig? = null,
    ): Boolean {
        assert(sceneGadget.gadgetId == gadgetId)
        assert(sceneGadget.state == state)
        assert(sceneGadget.pointType == pointType)
        assert(sceneGadget.bossChest == bossChest)
        assert(sceneGadget.chestDropId == chestDropId)
        assert(sceneGadget.interactId == interactId)
        assert(sceneGadget.draftId == draftId)
        assert(sceneGadget.routeId == routeId)
        assert(sceneGadget.startRoute == startRoute)
        assert(sceneGadget.isUsePointArray == isUsePointArray)
        assert(sceneGadget.persistent == persistent)
        assert(sceneGadget.showCutscene == showCutscene)
        assert(sceneGadget.owner == owner)
        assert(sceneGadget.autopick == autopick)
        assert(sceneGadget.explore == explore)
        assert(sceneGadget.arguments == arguments)
        assert(sceneGadget.isGuestCanOperate == isGuestCanOperate)
        assert(sceneGadget.isBlossomChest == isBlossomChest)
        assert(sceneGadget.isEnableInteract == isEnableInteract)
        assert(sceneGadget.talkState == talkState)
        assert(sceneGadget.fishingId == fishingId)
        assert(sceneGadget.fishingAreas == fishingAreas)
        assert(sceneGadget.crucibleConfig == crucibleConfig)
        assert(sceneGadget.offeringConfig == offeringConfig)
        assert(sceneGadget.worktopConfig == worktopConfig)
        return true
    }

    fun compareTrigger(
        sceneTrigger: SceneTrigger,
        name: String? = null,
        configId: Int = 0,
        event: Int,
        triggerCount: Int = 1,
        source: String? = null,
        condition: String? = null,
        action: String? = null,
        tag: String? = null,
        tlogTag: String? = null,
        forbidGuest: Boolean = true
    ): Boolean {
        assert(sceneTrigger.name == name)
        assert(sceneTrigger.configId == configId)
        assert(sceneTrigger.event == event)
        assert(sceneTrigger.triggerCount == triggerCount)
        assert(sceneTrigger.source == source)
        assert(sceneTrigger.condition == condition)
        assert(sceneTrigger.action == action)
        assert(sceneTrigger.tag == tag)
        assert(sceneTrigger.tlogTag == tlogTag)
        assert(sceneTrigger.forbidGuest == forbidGuest)
        return true
    }

    fun compareRegion(
        sceneRegion: SceneRegion,
        shape: Int = ScriptRegionShape.NONE,
        radius: Int = 0,
        height: Float = 0f,
        size: Vector? = null,
        pointArray: List<Vector>? = null,
        abilityGroupList: List<String>? = null,
        teamAbilityGroup: List<String>? = null,
        isTriggerReloadGroup: Boolean = false,
        visionTypeList: List<Int>? = null,
    ): Boolean {
        assert(sceneRegion.shape == shape)
        assert(sceneRegion.radius == radius)
        assert(sceneRegion.height == height)
        assert(sceneRegion.size == size)
        assert(sceneRegion.pointArray == pointArray)
        assert(sceneRegion.abilityGroupList == abilityGroupList)
        assert(sceneRegion.teamAbilityGroup == teamAbilityGroup)
        assert(sceneRegion.isTriggerReloadGroup == isTriggerReloadGroup)
        assert(sceneRegion.visionTypeList == visionTypeList)
        return true
    }

    fun comparePoint(
        scenePoint: ScenePoint,
        configId: Int = 0,
        pos: Vector? = null,
        rot: Vector? = null,
        areaId: Int = 0,
        tag: Long = 0L,
    ): Boolean {
        assert(scenePoint.configId == configId)
        assert(scenePoint.pos == pos)
        assert(scenePoint.rot == rot)
        assert(scenePoint.areaId == areaId)
        assert(scenePoint.tag == tag)
        return true
    }

    fun compareVariable(
        sceneVar: SceneVar,
        name: String? = null,
        configId: Int = 0,
        value: Int = 0,
        noRefresh: Boolean = false,
    ): Boolean {
        assert(sceneVar.name == name)
        assert(sceneVar.configId == configId)
        assert(sceneVar.value == value)
        assert(sceneVar.noRefresh == noRefresh)
        return true
    }
}
