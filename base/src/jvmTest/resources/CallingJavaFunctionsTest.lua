

function expectIntArray(context)
    local param = {1, 2, 3, 4, 5}
    local array = KotlinFunctions.expectIntArray(context, param)
    for i,v in ipairs(array) do
        if v == Nil then
            return Nil
        end
    end
    return array
end

function expectObjectTable(context)
    local x = 1.1
    local y = 2.4
    local z = 3.6
    local result = KotlinFunctions.expectObjectTable(context, {x=x, y=y, z=z})

    if(result.x ~= x) then
        return Nil
    end
    if(result.y ~= y) then
        return Nil
    end
    if(result.z ~= z) then
        return Nil
    end

    return array
end
