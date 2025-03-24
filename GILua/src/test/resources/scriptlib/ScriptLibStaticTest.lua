
function testScriptLibStatic(ctx, evt)
    getRet = ScriptLib.GetEntityType(evt.param1)
    plRet = ScriptLib.PrintLog(evt.paramString1)
    pclRet = ScriptLib.PrintContextLog(ctx, evt.paramString1)
    return {getRet, plRet, pclRet}
end

function testScriptLibStaticCompat(ctx, evt)
    getRet = ScriptLib.GetEntityType(ctx, evt.param1)
    plRet = ScriptLib.PrintLog(ctx, evt.paramString1)
    return {getRet, plRet}
end
