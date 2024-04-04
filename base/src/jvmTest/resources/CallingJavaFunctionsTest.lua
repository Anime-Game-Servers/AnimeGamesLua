

function expectIntArray(context)
    local param = {1, 2, 3, 4, 5}
    local array = KotlinFunctions.expectIntArray(context, param)
    for i,v in ipairs(array) do
        if v == Nil then
            return i
        end
    end
    return array
end

function expectObjectTable(context)
    local x = 1
    local y = 2
    local z = 3
    local result = KotlinFunctions.expectObjectTable(context, {x=x, y=y, z=z})

    if result.x ~= x then
        return "X"
    end
    if result.y ~= y then
        return "Y"
    end
    if result.z ~= z then
        return "Z"
    end

    return result
end
