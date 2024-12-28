
function testScriptContext(ctx, evt)
    if(ctx.source_entity_id ~= evt.source_eid) then
        return "seid" .. ctx.source_entity_id
    end
    if(ctx.target_entity_id ~= evt.target_eid) then
        return "teid"..ctx.target_entity_id
    end
    if(ctx.uid ~= evt.uid) then
        return "uid"..ctx.uid
    end
    if(ctx.owner_uid ~= 55) then
        return "ouid"..ctx.owner_uid
    end

    return evt
end
