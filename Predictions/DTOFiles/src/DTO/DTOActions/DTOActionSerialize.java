package DTO.DTOActions;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class DTOActionSerialize implements JsonSerializer<DTOActionInfo>{
    @Override
    public JsonElement serialize(DTOActionInfo src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement jsonElement = context.serialize(src);
        jsonElement.getAsJsonObject().addProperty("type", src.getClass().getSimpleName());
        return jsonElement;
    }
}
