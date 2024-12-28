

function expectIntArray(context)
    local param = {1, 2, 3, 4, 5}
    local array = KotlinFunctions.expectIntArray(context, param)
    for i,v in ipairs(array) do
        if v == nil then
            return "v: "..i
        end
        if array[i] == nil or array[i] ~= v then
            return "array i: "..i.." "..v
        end
        if param[i] ~= array[i] then
            return "param[i+1]: "..i.." "..param[i].." "..array[i]
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
    if result.zz ~= nil then
        return "ZZ"
    end

    return result
end
