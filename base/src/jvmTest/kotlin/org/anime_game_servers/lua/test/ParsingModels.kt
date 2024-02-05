package org.anime_game_servers.lua.test

import kotlinx.io.Source
import org.anime_game_servers.core.base.annotations.lua.LuaNames
import org.anime_game_servers.core.base.annotations.lua.LuaStatic
import org.anime_game_servers.jnlua_engine.JNLuaEngine
import org.anime_game_servers.lua.engine.*
import org.anime_game_servers.lua.models.ScriptType
import org.anime_game_servers.lua.utils.asSource
import org.anime_game_servers.luaj_engine.LuaJEngine
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path


const val defaultName = ""
const val defaultHealth = 0
const val defaultHealthStat = 0
const val defaultAtkStat = 0f
const val defaultDefStat = 0f
const val defaultSpdStat = 0.0
const val defaultExpStat = 0L
const val defaultLevelStat = 0

@LuaStatic
enum class AIType {
    DEFAULT,
    AGGRESSIVE,
    DEFENSIVE,
    PASSIVE
}

data class Stats(
    var health: Int = defaultHealthStat,
    var atk: Float = defaultAtkStat,
    @field:LuaNames("def")
    var defense: Float = defaultDefStat,
    var spd: Double = defaultSpdStat,
    var exp: Long = defaultExpStat,
    var level: Int = defaultLevelStat,
)


data class Monster(
    var name: String = defaultName,
    var health: Int = defaultHealth,
    var stats: Stats? = null,
    var drops: Map<Int, Int>? = null,
    var titles: Set<String>? = null,
    var ids: List<Long>? = null,
    var subordinates: List<Monster>? = null,
    var aiType: AIType = AIType.DEFAULT
)

abstract class TestScriptLoader : BaseScriptLoader{
    abstract val engine: LuaEngine
    override fun getScriptPath(scriptName: String): Path? {
        val uri = ClassLoader.getSystemResource(scriptName).toURI()
        return Path.of(uri)
    }

    override fun openScript(params: BaseScriptLoader.ScriptLoadParams): Source? {
        val basePath: String = params.getBasePath()
        val scriptPath = getScriptPath(basePath) ?: return null
        if(Files.exists(scriptPath)){
            return scriptPath.asSource()
        }
        return null
    }

    override fun getScript(scriptLoadParams: BaseScriptLoader.ScriptLoadParams): LuaScript? {
        val basePath: String = scriptLoadParams.getBasePath()
        val scriptPath = getScriptPath(basePath) ?: return null
        return engine.getScript(scriptPath, scriptLoadParams.getScriptType());
    }

    override fun getRequireScriptParams(scriptName: String): BaseScriptLoader.ScriptLoadParams {
        return object : BaseScriptLoader.ScriptLoadParams {
            override fun getBaseDirectory(): String {
                return ""
            }

            override fun getScriptName(): String {
                return scriptName
            }
        }
    }
}

class LuaJTest : ParsingTest() {

    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = LuaJEngine(ScriptConfig(this, RequireMode.DISABLED))
    }

    @Test
    fun runTest(){
        parseMonsterTest()
    }
}

class JNLuaTest : ParsingTest() {

    override val scriptLoader = object : TestScriptLoader(){
        override val engine: LuaEngine = JNLuaEngine(ScriptConfig(this, RequireMode.DISABLED))
    }

    @Test
    fun runTest(){
        parseMonsterTest()
    }
}

abstract class ParsingTest{
    abstract val scriptLoader : TestScriptLoader

    fun getEngine(): LuaEngine {
        return scriptLoader.engine
    }

    fun parseMonsterTest() {
        val engine = getEngine()
        LuaEngine.registerNamespace(this::class.java.packageName)
        engine.addGlobals()
        val uri = ClassLoader.getSystemResource("ParsingTest.lua").toURI()
        val path = Path.of(uri);
        val script = engine.getScript(path, ScriptType.EXECUTABLE)

        assert(script != null)

        script!!.evaluate()

        val monsters = script.getGlobalVariableList("monsters", Monster::class.java)
        assert(monsters != null)
        assert(monsters.size == 5)
        checkDefault(monsters[0])
        checkDefaultStats(monsters[1])
        checkZombie(monsters[2])
        checkGhoul(monsters[3])
        checkSkeleton(monsters[4])
    }

    fun checkDefault(monster: Monster){
        assert(monster.name == defaultName)
        assert(monster.health == defaultHealth)
        assert(monster.stats == null)
        assert(monster.drops == null)
        assert(monster.titles == null)
        assert(monster.ids == null)
        assert(monster.subordinates == null)
        assert(monster.aiType == AIType.DEFAULT)
    }

    fun checkDefaultStats(monster: Monster){
        assert(monster.name == defaultName)
        assert(monster.health == defaultHealth)
        assert(monster.stats != null)
        checkDefaultStats(monster.stats!!)
        assert(monster.drops == null)
        assert(monster.titles == null)
        assert(monster.ids == null)
        assert(monster.subordinates == null)
    }

    fun checkDefaultStats(stats: Stats){
        assert(stats.health == defaultHealthStat)
        assert(stats.atk == defaultAtkStat)
        assert(stats.defense == defaultDefStat)
        assert(stats.spd == defaultSpdStat)
        assert(stats.exp == defaultExpStat)
        assert(stats.level == defaultLevelStat)
    }

    fun checkZombie(monster: Monster){
        assert(monster.name == "Zombie")
        assert(monster.health == 100)
        assert(monster.stats != null)
        assert(monster.stats!!.health == 100)
        assert(monster.stats!!.atk == 10f)
        assert(monster.stats!!.defense == 5f)
        assert(monster.stats!!.spd == defaultSpdStat)
        assert(monster.stats!!.exp == defaultExpStat)
        assert(monster.stats!!.level == defaultLevelStat)
        assert(monster.drops != null)
        assert(monster.drops!!.size == 2)
        assert(monster.drops!![2] == 2)
        assert(monster.drops!![3] == 1)
        assert(!monster.drops!!.containsKey(1))
        assert(monster.titles == null)
        assert(monster.ids == null)
        assert(monster.subordinates == null)
        assert(monster.aiType == AIType.DEFAULT)
    }

    fun checkGhoul(monster: Monster){
        assert(monster.name == "Ghoul")
        assert(monster.health == 150)
        assert(monster.stats == null)
        assert(monster.drops == null)
        assert(monster.titles == null)
        assert(monster.ids == null)
        assert(monster.subordinates == null)
        assert(monster.aiType == AIType.DEFAULT)
    }
    fun checkSkeleton(monster: Monster){
        assert(monster.name == "Skeleton")
        assert(monster.health == 50)
        assert(monster.stats != null)
        assert(monster.stats!!.health == 75)
        assert(monster.stats!!.atk == 5.5f)
        assert(monster.stats!!.defense == 2.2f)
        assert(monster.stats!!.spd == 1.1)
        assert(monster.stats!!.exp == 10L)
        assert(monster.stats!!.level == 1)
        assert(monster.drops != null)
        assert(monster.drops!!.size == 3)
        assert(monster.drops!![1] == 2)
        assert(monster.drops!![2] == 2)
        assert(monster.drops!![3] == 5)
        assert(!monster.drops!!.containsValue(0))
        assert(monster.titles != null)
        assert(monster.titles!!.size == 2)
        assert(monster.titles!!.contains("Skeleton"))
        assert(monster.titles!!.contains("Undead"))
        assert(monster.ids != null)
        assert(monster.ids!!.size == 3)
        assert(monster.ids!![0] == 1L)
        assert(monster.ids!![1] == 2L)
        assert(monster.ids!![2] == 5L)
        assert(monster.subordinates != null)
        assert(monster.subordinates!!.size == 2)
        checkZombie(monster.subordinates!![0])
        checkGhoul(monster.subordinates!![1])
        assert(monster.aiType == AIType.AGGRESSIVE)
    }
}
