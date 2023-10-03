package DTO.DTOActions;

import DTO.DTOActions.DTOCondition.DTOCondition;
import DTO.DTOActions.DTOCondition.DTOConditionMultiple;
import DTO.DTOActions.DTOCondition.DTOConditionSingle;
import com.google.gson.*;

import java.lang.reflect.Type;

public class DTOActionDeserialize implements JsonDeserializer<DTOActionInfo> {
    @Override
    public DTOActionInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        switch (type) {
            case "DTOIncreaseAndDecrease":
                return context.deserialize(jsonObject, DTOIncreaseAndDecrease.class);
            case "DTOCalculation":
                return context.deserialize(jsonObject, DTOCalculation.class);
            case "DTOConditionMultiple":
                return context.deserialize(jsonObject, DTOConditionMultiple.class);
            case "DTOConditionSingle":
                return context.deserialize(jsonObject, DTOConditionSingle.class);
            case "DTOSet":
                return context.deserialize(jsonObject, DTOSet.class);
            case "DTOReplace":
                return context.deserialize(jsonObject, DTOReplace.class);
            case "DTOProximity":
                return context.deserialize(jsonObject, DTOProximity.class);
            default:
                throw new JsonParseException("Unknown type: " + type);
        }
    }
}