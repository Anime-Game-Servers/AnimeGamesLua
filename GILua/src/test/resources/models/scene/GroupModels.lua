monsters = {
    {
        config_id = 1001, monster_id = 100001, pos = { x = 11.1, y = 111, z = -1.11 }, rot = { x = 11, y = 110.000, z = -100.000 },
    },
    {
        config_id = 1002,
        monster_id = 100002,
        pos = { x = 22.2, y = -222, z = 2.22 },
        rot = { x = 22, y = -220.000, z = 200.000 },
        level = 2,
        area_id = 202,
        vision_level = VisionLevelType.VISION_LEVEL_REMOTE,
        mark_flag = 2222,
        drop_tag = "drop2",
        guest_ban_drop = 1,
        pose_id = 222,
        pose_logic_state = "poseState2",
        isOneoff = true,
        drop_id = 33033,
        disableWander = true,
        title_id = 3030303,
        special_name_id = 330033,
        affix = { 33, 333 },
        isElite = true,
        climate_area_id = 44,
        ai_config_id = 4004004,
        kill_score = 444,
        speed_level = 4,
        tag = 44444444,
        is_light_config = true,
        oneoff_reset_version = 5,
        sight_group_index = 55,
        server_global_value_config = { ["SGV_ONE"] = 1, ["SGV_TWO"] = 2.2 }
    }
}

npcs = {
    { config_id = 2001, npc_id = 20001, pos = { x = -11.1, y = 1.1, z = 11 }, rot = { x = 1.000, y = 11.1, z = -11.000 }, area_id = 1 },
    { config_id = 2002, npc_id = 20002, pos = { x = -22.2, y = 2.2, z = 22 }, rot = { x = 2.000, y = 22.2, z = -22.000 }, area_id = 2 }
}

gadgets = {
    { config_id = 3001, gadget_id = 200001, pos = { x = 1.1, y = 100, z = -1.0 }, rot = { x = 0.000, y = 0.000, z = 0.000 } },
    {
        config_id = 3002,
        gadget_id = 200002,
        pos = { x = 2.2, y = 200, z = -2.0 },
        rot = { x = 2.000, y = 0.200, z = -2.000 },
        level = 32,
        area_id = 302,
        vision_level = VisionLevelType.VISION_LEVEL_REMOTE,
        mark_flag = 222,
        drop_tag = "drop32",
        guest_ban_drop = 1
    },
    {
        config_id = 3003,
        gadget_id = 200003,
        pos = { x = 2.2, y = 200, z = -2.0 },
        rot = { x = 2.000, y = 0.200, z = -2.000 },
        state = GadgetState.GearStart,
        point_type = 2003,
        boss_chest = { monster_config_id = 2001, resin = 25, life_time = 2003, take_num = 3 },
        drop_id = 303003,
        chest_drop_id = 3303003,
        interact_id = 3333,
        isOneoff = true,
        draft_id = 4003003,
        route_id = 5003003,
        start_route = false,
        is_use_point_array = true,
        persistent = true,
        showcutscene = true,
        owner = 3002,
        autopick = true,
        explore = { name = "gadget3", exp = 1 },
        arguments = { 3, 4, 5 },
        oneoff_reset_version = 303,
        is_guest_can_operate = true,
        is_blossom_chest = true,
        is_enable_interact = false,
        talk_state = 6300303,
        fishing_id = 333,
        fishing_areas = { 100030, 100033 },
        crucible_config = { duration = 300, start_cd = 3, progress_stage = { 0, 300, 3300, 33333 }, mp_play_id = 33 },
        offering_config = { offering_id = 33 },
        worktop_config = { init_options = { 33, 333, 3333 }, is_persistent = true },
        server_global_value_config = { ["SGV_ONE"] = 1, ["SGV_TWO"] = 2.2 }
    }

}

regions = {
    {
        config_id = 4001,
        shape = RegionShape.SPHERE,
        radius = 10,
        pos = { x = 101.1, y = 11.11, z = -101.11 }
    },
    {
        config_id = 4002,
        shape = RegionShape.CYLINDER,
        radius = 200,
        pos = { x = 101.1, y = 11.11, z = -101.11 },
        height = 22.000,
    },
    {
        config_id = 4003,
        shape = RegionShape.CUBIC,
        size = { x = 303.3, y = 33.33, z = -303.33 },
        pos = { x = 101.1, y = 11.11, z = -101.11 }
    },
    {
        config_id = 4004,
        shape = RegionShape.POLYGON,
        pos = { x = 101.1, y = 11.11, z = -101.11 },
        height = 44.000,
        point_array = { { x = 4444.4, y = 4.4 }, { x = 44.44, y = 400 }, { x = -4.4, y = -444.906 } }
    },
    {
        config_id = 4005,
        shape = RegionShape.SPHERE,
        radius = 50,
        pos = { x = 101.1, y = 11.11, z = -101.11 },
        area_id = 45,
        ability_group_list = {"ability1", "ability2"},
        team_ability_group = {"teamAbility1", "teamAbility2"},
        is_trigger_reload_group = true,
        vision_type_list = { 5, 55, 555}
    },
}

triggers = {
    {
        config_id = 5001,
        name = "ENTER_REGION_5001",
        event = EventType.EVENT_ENTER_REGION,
        source = "",
        condition = "",
        action = "",
    },
    {
        config_id = 5002,
        name = "ENTER_REGION_5002",
        event = EventType.EVENT_ENTER_REGION,
        source = "52",
        condition = "condition_EVENT_ENTER_REGION_5002",
        action = "action_EVENT_ENTER_REGION_5002",
        trigger_count = 0,
        tag = "522",
        tlog_tag = "logtag_5002",
        forbid_guest = false
    },
    { config_id = 5003, name = "AVATAR_NEAR_PLATFORM_5003",   event = EventType.EVENT_AVATAR_NEAR_PLATFORM,   source = "53", condition = "", action = "", trigger_count = 3, tag = "63" },
    { config_id = 5004, name = "GALLERY_ALL_AVATAR_DIE_5004", event = EventType.EVENT_GALLERY_ALL_AVATAR_DIE, source = "54", condition = "", action = "", trigger_count = 4, tag = "64" }
}

variables = {
    { config_id = 6001, name = "first" },
    { config_id = 6002, name = "second", value = 2, no_refresh = true }
}

points = {
    { config_id = 7001, pos = { x = 7171, y = 71.71, z = -71.71 }, rot = { x = 7, y = 1, z = 7.1 } },
    { config_id = 7002, pos = { x = 7171, y = 71.71, z = -71.71 }, rot = { x = 7, y = 1, z = 7.1 }, area_id = 2, tag = 72 },
}

init_config = {
    suite = 1,
    end_suite = 0,
    rand_suite = false
}

suites = {
    {
        monsters = { 1001 },
        gadgets = { 3001, 3002 },
        regions = { 4001 },
        triggers = { 5001 },
        rand_weight = 100
    }
}
