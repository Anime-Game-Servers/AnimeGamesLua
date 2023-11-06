package org.anime_game_servers.gi_lua.models.scene.group;

import io.github.oshai.kotlinlogging.KLogger;
import io.github.oshai.kotlinlogging.KotlinLogging;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.anime_game_servers.gi_lua.models.SceneMeta;
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

    @Nullable
    private SceneInitConfig init_config;
    @Nullable
    private List<SceneSuite> suites;

    private List<SceneMonsterPool> monster_pools;
    private List<List<Integer>> sight_groups;

    @Nullable
    private SceneGarbage garbages;


    // internal
    private transient SceneGroupInfo groupInfo;
    private transient SceneMeta sceneMeta;
    private transient boolean loaded; // Not an actual variable in the scripts either
    private transient LuaScript script;

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
        if (!scriptLoader.loadSceneGroupScript(scriptType, typeId, groupId, cs -> {
            this.script = cs;
            // Set
            this.monsters = cs.getGlobalVariableList("monsters", SceneMonster.class).stream()
                    .collect(Collectors.toMap(x -> x.config_id, y -> y, (a, b) -> a));
            this.monsters.values().forEach(m -> {
                m.groupId = groupId;
                m.blockId = blockId;
                m.sceneMeta = sceneMeta;
            });

            this.npcs = cs.getGlobalVariableList("npcs", SceneNPC.class).stream()
                    .collect(Collectors.toMap(x -> x.config_id, y -> y, (a, b) -> a));
            this.npcs.values().forEach(m -> {
                m.groupId = groupId;
                m.blockId = blockId;
                m.sceneMeta = sceneMeta;
            });

            this.gadgets = cs.getGlobalVariableList("gadgets", SceneGadget.class).stream()
                    .collect(Collectors.toMap(x -> x.config_id, y -> y, (a, b) -> a));
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
                    .collect(Collectors.toMap(x -> x.config_id, y -> y, (a, b) -> a));
            this.regions.values().forEach(m -> {
                m.groupId = groupId;
                m.blockId = blockId;
                m.sceneMeta = sceneMeta;
            });

            this.init_config = cs.getGlobalVariable("init_config", SceneInitConfig.class);

            // Garbages // TODO: fix properly later
            /*Object garbagesValue = this.bindings.get("garbages");
            if (garbagesValue instanceof LuaValue garbagesTable) {
                this.garbages = new SceneGarbage();
                if (garbagesTable.checktable().get("gadgets") != LuaValue.NIL) {
                    this.garbages.gadgets = ScriptLoader.getSerializer().toList(SceneGadget.class, garbagesTable.checktable().get("gadgets").checktable());
                    this.garbages.gadgets.forEach(m -> m.group = this);
                }
            }*/

            // Add variables to suite
            this.variables = cs.getGlobalVariableList("variables", SceneVar.class);

            this.monster_pools = cs.getGlobalVariableList("monster_pools", SceneMonsterPool.class);
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
        if (init_config == null) return 1;
        if (init_config.getIo_type() == 1) return init_config.getSuite(); //IO TYPE FLOW
        if (init_config.isRand_suite()) {
            if (suites.size() == 1) {
                return init_config.getSuite();
            } else {
                List<Integer> randSuiteList = new ArrayList<>();
                for (int i = 0; i < suites.size(); i++) {
                    if (i == exclude_index) continue;

                    var suite = suites.get(i);
                    for (int j = 0; j < suite.getRand_weight(); j++) randSuiteList.add(Integer.valueOf(i + 1));
                }
                return randSuiteList.get(new Random().nextInt(randSuiteList.size()));
            }
        }
        return init_config.getSuite();
    }

    public Optional<SceneBossChest> searchBossChestInGroup() {
        return this.gadgets.values().stream().map(g -> g.getBoss_chest()).filter(Objects::nonNull)
                .filter(bossChest -> bossChest.getMonster_config_id() > 0)
                .findFirst();
    }

    /*public List<SceneGroup> getReplaceableGroups(Collection<SceneGroup> loadedGroups) {
        return this.is_replaceable == null ? List.of() :
            Optional.ofNullable(GameData.getGroupReplacements().get(this.id)).stream()
                .map(GroupReplacementData::getReplace_groups)
                .flatMap(List::stream)
                .map(replacementId -> loadedGroups.stream().filter(g -> g.id == replacementId).findFirst())
                .filter(Optional::isPresent).map(Optional::get)
                .filter(replacementGroup -> replacementGroup.is_replaceable != null)
                .filter(replacementGroup -> (replacementGroup.is_replaceable.isValue()
                    && replacementGroup.is_replaceable.getVersion() <= this.is_replaceable.getVersion())
                    || replacementGroup.is_replaceable.isNew_bin_only())
                .toList();
    }*/
}
