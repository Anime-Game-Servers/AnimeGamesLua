package org.anime_game_servers.gi_lua.models.scene.group;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.anime_game_servers.core.base.annotations.lua.LuaNames;
import org.anime_game_servers.gi_lua.models.loader.SceneGroupScriptLoadParams;
import org.anime_game_servers.gi_lua.models.scene.SceneMeta;
import org.anime_game_servers.gi_lua.models.loader.ScriptSource;
import org.anime_game_servers.gi_lua.models.scene.block.SceneGroupInfo;
import org.anime_game_servers.gi_lua.models.loader.GIScriptLoader;
import org.anime_game_servers.lua.engine.LuaScript;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("FieldMayBeFinal")
@ToString
@Getter
public class SceneGroup {
    private static KLogger logger = KotlinLogging.INSTANCE.logger(SceneGroup.class.getName());

    // from group script
    @Nullable
    private Map<Integer, SceneMonster> monsters; // <ConfigId, Monster>
    @Nullable
    private Map<Integer, SceneNPC> npcs; // <ConfigId, Npc>
    @Nullable
    private Map<Integer, SceneGadget> gadgets; // <ConfigId, Gadgets>
    @Nullable
    private Map<String, SceneTrigger> triggers; // <TriggerName, Trigger>
    @Nullable
    private Map<Integer, SceneRegion> regions;  // <ConfigId, Region>
    @Nullable
    private Map<Integer, ScenePoint> points;  // <ConfigId, ScenePoint>
    @Nullable
    private List<SceneVar> variables;

    @Nullable @LuaNames("init_config")
    private SceneInitConfig initConfig;
    @Nullable
    private List<SceneSuite> suites;

    @LuaNames("monster_pools")
    private List<SceneMonsterPool> monsterPools;
    @LuaNames("sight_groups")
    private List<List<Integer>> sightGroups;

    @Nullable
    private SceneGarbage garbages;


    // internal
    private transient SceneGroupInfo groupInfo;
    private transient SceneMeta sceneMeta;
    private transient boolean loaded; // Not an actual variable in the scripts either
    private transient LuaScript script;
    private static final Random random = new Random();

    public static SceneGroup of(SceneGroupInfo groupInfo) {
        var group = new SceneGroup(groupInfo);
        return group;
    }

    protected SceneGroup(SceneGroupInfo groupInfo) {
        this.groupInfo = groupInfo;
        this.sceneMeta = groupInfo.getSceneMeta();
    }

    public boolean hasGarbages() {
        return this.garbages != null && !garbages.isEmpty();
    }

    @Nullable
    public List<SceneGadget> getGarbageGadgets() {
        return this.garbages == null ? null : this.garbages.getGadgets();
    }


    public SceneSuite getSuiteByIndex(int index) {
        if (index < 1 || index > suites.size()) {
            return null;
        }
        return this.suites.get(index - 1);
    }

    public synchronized SceneGroup load(GIScriptLoader scriptLoader) {
        if (this.loaded) {
            return this;
        }
        // Set flag here so if there is no script, we don't call this function over and over again.
        this.loaded = true;
        val sceneId = sceneMeta.getSceneId();
        val groupId = groupInfo.getId();
        val blockId = groupInfo.getBlockId();
        val activityId = groupInfo.getActivityId();

        val scriptType = activityId == 0 ? ScriptSource.SCENE : ScriptSource.ACTIVITY;
        val typeId = activityId == 0 ? sceneId : activityId;
        val groupParams = new SceneGroupScriptLoadParams(scriptType, typeId, groupId);
        if (!scriptLoader.loadData(groupParams, cs -> {
            this.script = cs;
            // Set
            this.monsters = cs.getGlobalVariableList("monsters", SceneMonster.class).stream()
                    .collect(Collectors.toMap(x -> x.configId, y -> y, (a, b) -> a));
            this.monsters.values().forEach(m -> {
                m.groupId = groupId;
                m.blockId = blockId;
                m.sceneMeta = sceneMeta;
            });

            this.npcs = cs.getGlobalVariableList("npcs", SceneNPC.class).stream()
                    .collect(Collectors.toMap(x -> x.configId, y -> y, (a, b) -> a));
            this.npcs.values().forEach(m -> {
                m.groupId = groupId;
                m.blockId = blockId;
                m.sceneMeta = sceneMeta;
            });

            this.gadgets = cs.getGlobalVariableList("gadgets", SceneGadget.class).stream()
                    .collect(Collectors.toMap(x -> x.configId, y -> y, (a, b) -> a));
            this.gadgets.values().forEach(m -> {
                m.groupId = groupId;
                m.blockId = blockId;
                m.sceneMeta = sceneMeta;
            });

            this.triggers = cs.getGlobalVariableList("triggers", SceneTrigger.class).stream()
                    .collect(Collectors.toMap(SceneTrigger::getName, y -> y, (a, b) -> a));
            this.triggers.values().forEach(t -> {
                t.setGroupId(groupId);
                t.setBlockId(blockId);
                t.setSceneMeta(sceneMeta);
            });

            this.suites = cs.getGlobalVariableList("suites", SceneSuite.class);
            this.regions = cs.getGlobalVariableList("regions", SceneRegion.class).stream()
                    .collect(Collectors.toMap(x -> x.configId, y -> y, (a, b) -> a));
            this.regions.values().forEach(m -> {
                m.groupId = groupId;
                m.blockId = blockId;
                m.sceneMeta = sceneMeta;
            });

            this.initConfig = cs.getGlobalVariable("init_config", SceneInitConfig.class);

            // Garbages
            this.garbages = cs.getGlobalVariable("init_config", SceneGarbage.class);

            // Add variables to suite
            this.variables = cs.getGlobalVariableList("variables", SceneVar.class);

            this.monsterPools = cs.getGlobalVariableList("monster_pools", SceneMonsterPool.class);
            //this.sight_groups = cs.getGlobalVariableList("sight_groups", List<Integer>.class);

            // Add monsters and gadgets to suite
            this.suites.forEach(i -> i.init(this));
        })) {
            return null;
        }

        logger.debug(() -> "Successfully loaded group " + groupId + " in scene " + sceneId + ".");
        return this;
    }

    public int findInitSuiteIndex(int exclude_index) { //TODO: Investigate end index
        if (initConfig == null) return 1;
        if (initConfig.getIoType() == 1) return initConfig.getSuite(); //IO TYPE FLOW
        if (initConfig.isRandSuite()) {
            if (suites.size() == 1) {
                return initConfig.getSuite();
            } else {
                List<Integer> randSuiteList = new ArrayList<>();
                for (int i = 0; i < suites.size(); i++) {
                    if (i == exclude_index) continue;

                    var suite = suites.get(i);
                    for (int j = 0; j < suite.getRandWeight(); j++) randSuiteList.add(Integer.valueOf(i + 1));
                }
                return randSuiteList.get(random.nextInt(randSuiteList.size()));
            }
        }
        return initConfig.getSuite();
    }

    public Optional<SceneBossChest> searchBossChestInGroup() {
        return this.gadgets.values().stream().map(g -> g.getBossChest()).filter(Objects::nonNull)
                .filter(bossChest -> bossChest.getMonsterConfigId() > 0)
                .findFirst();
    }
}
