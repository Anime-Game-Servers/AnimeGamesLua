monsters = {
    {
        config_id = 1001, monster_id = 100001, pos = { x = 11.1, y = 111, z = -1.11 }, rot = { x = 11, y = 110.000, z = -100.000 },
    },
    {
        config_id = 1002, monster_id = 100002, pos = { x = 22.2, y = -222, z = 2.22 },rot = { x = 22, y = -220.000, z = 200.000 },
    },
    {
        config_id = 1003, monster_id = 100003,
    },
    {
        config_id = 1004, monster_id = 100004,
    },
    {
        config_id = 1005, monster_id = 100005,
    },
}

npcs = {
    { config_id = 2001, npc_id = 20001, pos = { x = -11.1, y = 1.1, z = 11 }, rot = { x = 1.000, y = 11.1, z = -11.000 }, area_id = 1 },
    { config_id = 2002, npc_id = 20002, pos = { x = -22.2, y = 2.2, z = 22 }, rot = { x = 2.000, y = 22.2, z = -22.000 }, area_id = 2 },
    { config_id = 2003, npc_id = 20003, pos = { x = -22.2, y = 2.2, z = 22 }, rot = { x = 2.000, y = 22.2, z = -22.000 }, area_id = 3 },
    { config_id = 2004, npc_id = 20004, pos = { x = -22.2, y = 2.2, z = 22 }, rot = { x = 2.000, y = 22.2, z = -22.000 }, area_id = 4 }
}

gadgets = {
    { config_id = 3001, gadget_id = 200001, pos = { x = 1.1, y = 100, z = -1.0 }, rot = { x = 0.000, y = 0.000, z = 0.000 } },
    { config_id = 3002, gadget_id = 200002, pos = { x = 2.2, y = 200, z = -2.0 }, rot = { x = 2.000, y = 0.200, z = -2.000 } },
    { config_id = 3003, gadget_id = 200003, pos = { x = 2.2, y = 200, z = -2.0 }, rot = { x = 2.000, y = 0.200, z = -2.000 } },
    { config_id = 3004, gadget_id = 200004, pos = { x = 2.2, y = 200, z = -2.0 }, rot = { x = 2.000, y = 0.200, z = -2.000 } }
}

regions = {
	{ config_id = 4001, shape = RegionShape.SPHERE, radius = 10, pos = { x = 101.1, y = 11.11, z = -101.11 } },
	{ config_id = 4002, shape = RegionShape.CYLINDER, radius = 200, pos = { x = 202.2, y = 22.22, z = -202.22 }, height = 22.000 },
	{ config_id = 4003, shape = RegionShape.CUBIC, size = { x = 303.3, y = 33.33, z = -303.33 }, pos = { x = 3333.3, y = 3000.33, z = -3.3 } },
	{ config_id = 4004, shape = RegionShape.POLYGON, pos = { x = 404.4, y = 44.44, z = -404.44 }, height = 44.000, point_array = { { x = 4444.4, y = 4.4 }, { x = 44.44, y = 400 }, { x = -4.4, y = -444.906 } } }
}

triggers = {
    { config_id = 5001, name = "ENTER_REGION_5001", event = EventType.EVENT_ENTER_REGION, source = "", condition = "", action = "" },
    { config_id = 5002, name = "ENTER_REGION_5002", event = EventType.EVENT_ENTER_REGION, source = "", condition = "", action = "" },
    { config_id = 5003, name = "AVATAR_NEAR_PLATFORM_5003", event = EventType.EVENT_AVATAR_NEAR_PLATFORM, source = "", condition = "", action = "" },
    { config_id = 5004, name = "GALLERY_ALL_AVATAR_DIE_5004", event = EventType.EVENT_GALLERY_ALL_AVATAR_DIE, source = "", condition = "", action = "" }
}

variables = {
    { config_id = 6001, name = "first", value = 0 },
    { config_id = 6002, name = "second", value = 2 },
    { config_id = 6003, name = "third", value = 3, no_refresh = true },
    { config_id = 6004, name = "fourth", value = 4, no_refresh = true },
}

init_config = {
    io_type = IOType.GROUP_IO_TYPE_FLOW,
	suite = 1,
	end_suite = 0,
	rand_suite = false
}

suite_disk = {
	[1] = {
        gadgets = {
    	    { config_id = 3001, state = 0 }
    	 },
        monsters = {
            { config_id = 1001 }
        },
    	regions = { 4001 },
    	triggers = { "ENTER_REGION_5001" },
    	npcs = { 2001 },
        variables = {
    	    { config_id = 6001, name = "first", value = 0 }
    	}
    },
	[2] = {
        gadgets = {
            { config_id = 3002, state = 100 },
    	    { config_id = 3003, state = 0 }
    	 },
        monsters = {
            { config_id = 1002 },
            { config_id = 1003 },
        },
    	regions = { 4002, 4003 },
    	triggers = { "ENTER_REGION_5002", "AVATAR_NEAR_PLATFORM_5003" },
    	npcs = { 2002, 2003 },
        variables = {
    	    { config_id = 6002, name = "second", value = 2 },
    	    { config_id = 6003, name = "third", value = 3, no_refresh = true }
    	}
    },
	[3] = {
        gadgets = {
            { config_id = 3001, state = 0 },
    	    { config_id = 3002, state = 100 }
    	 },
        monsters = {
            { config_id = 1001 },
            { config_id = 1002 },
        },
    	regions = { 4001, 4002 },
    	triggers = { "ENTER_REGION_5001", "ENTER_REGION_5002" },
    	npcs = { 2001, 2002 },
        variables = {
    	    { config_id = 6001, name = "first", value = 0 },
    	    { config_id = 6002, name = "second", value = 2 }
    	}
    },
	[4] = {
        gadgets = { },
        monsters = { },
    	regions = { },
    	triggers = { },
    	npcs = { },
        variables = { }
    }
}
