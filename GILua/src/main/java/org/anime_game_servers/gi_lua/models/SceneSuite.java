package org.anime_game_servers.gi_lua.models;

import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@ToString
@Getter
public class SceneSuite {
    // make it refer the default empty list to avoid NPE caused by some group
    private List<Integer> monsters = List.of();
    private List<Integer> gadgets = List.of();
    private List<String> triggers = List.of();
    private List<Integer> regions = List.of();
    private List<Integer> npcs = List.of();
    private int rand_weight;

    private boolean ban_refresh = false;

    private transient List<SceneMonster> sceneMonsters = List.of();
    private transient List<SceneGadget> sceneGadgets = List.of();
    private transient List<SceneTrigger> sceneTriggers = List.of();
    private transient List<SceneRegion> sceneRegions = List.of();
    private transient List<SceneNPC> sceneNPCs = List.of();

    public void init(SceneGroup sceneGroup) {
        val monsters = sceneGroup.getMonsters();
        if(monsters != null){
            this.sceneMonsters = new ArrayList<>(
                this.monsters.stream()
                    .filter(monsters::containsKey)
                    .map(monsters::get)
                    .toList()
            );
        }

        val gadgets = sceneGroup.getGadgets();
        if(gadgets != null){
            this.sceneGadgets = new ArrayList<>(
                this.gadgets.stream()
                    .filter(gadgets::containsKey)
                    .map(gadgets::get)
                    .toList()
            );
        }

        val triggers = sceneGroup.getTriggers();
        if(triggers != null) {
            this.sceneTriggers = new ArrayList<>(
                this.triggers.stream()
                    .filter(triggers::containsKey)
                    .map(triggers::get)
                    .toList()
            );
        }
        val regions = sceneGroup.getRegions();
        if(regions != null) {
            this.sceneRegions = new ArrayList<>(
                this.regions.stream()
                    .filter(regions::containsKey)
                    .map(regions::get)
                    .toList()
            );
        }
        val npcs = sceneGroup.getNpcs();
        if(npcs != null) {
            this.sceneNPCs = new ArrayList<>(
                this.npcs.stream()
                    .filter(npcs::containsKey)
                    .map(npcs::get)
                    .toList()
            );
        }

    }
}
