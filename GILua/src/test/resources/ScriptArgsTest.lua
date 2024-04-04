
function testScriptArgs(ctx, evt)
    if(evt.param1 ~= 1) then
        return -1
    end
    if(evt.param2 ~= 2) then
        return -2
    end
    if(evt.param3 ~= 3) then
        return -3
    end
    if(evt.param4 ~= 4) then
        return -4
    end
    if(evt.source_eid ~= 5) then
        return -5
    end
    if(evt.target_eid ~= 6) then
        return -6
    end
    if(evt.group_id ~= 7) then
        return -7
    end
    if(evt.type ~= 8) then
        return -8
    end
    if(evt.uid ~= 9) then
        return -9
    end

    if(evt.param_str1 ~= "paramString1") then
        return -10
    end
    if(evt.source_name ~= "source") then
        return -11
    end

    return evt
end
