local default = {}
local defaultStats = { stats = {}}

local zombie = {
    name = "Zombie",
    health = 100,
    stats = {
        health = 100,
        atk = 10,
        def = 5
    },
    drops = {
        ['2'] = 2,
        ['3'] = 1
    }
}
local ghoul = {
    name = "Ghoul",
    health = 150,
}

local skeleton = {
    name = "Skeleton",
    health = 50,
    stats = {
        health = 75,
        atk = 5.5,
        def = 2.2,
        spd = 1.1,
        exp = 10,
        level = 1
    },
    drops = {
        ['1'] = 2,
        ['2'] = 2,
        ['3'] = 5
    },
    titles = { "Skeleton", "Undead" },
    ids = { 1, 2, 5 },
    subordinates = { zombie, ghoul},
    aiType = AIType.AGGRESSIVE
}

monsters = {
    default,
    defaultStats,
    zombie,
    ghoul,
    skeleton
}
