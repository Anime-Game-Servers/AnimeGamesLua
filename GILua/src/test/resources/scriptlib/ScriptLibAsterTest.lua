function testScriptLibAster(ctx, evt)
    args = { config_id = evt.param1, difficulty_id = evt.param2 }
    return ScriptLib.CreateAsterMidGeneralRewardGadget(ctx, args)
end
