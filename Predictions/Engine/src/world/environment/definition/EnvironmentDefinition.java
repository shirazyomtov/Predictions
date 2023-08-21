package world.environment.definition;

import jaxb.schema.generated.PRDEnvProperty;
import world.enums.Type;
import world.range.RangeImpl;

import java.io.Serializable;

public class EnvironmentDefinition implements Serializable {

    private final String name;

    private final Type type;

    private RangeImpl range = null;

    public EnvironmentDefinition(PRDEnvProperty prdEnvProperty){
        this.name = prdEnvProperty.getPRDName();
        this.type = Enum.valueOf(Type.class, prdEnvProperty.getType().toUpperCase());
        if(prdEnvProperty.getPRDRange()!= null) {
            this.range = new RangeImpl((float) prdEnvProperty.getPRDRange().getFrom(), (float) prdEnvProperty.getPRDRange().getTo());
        }
    }

//    @Override
//    public String toString() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Environment ").append(name).append(" details: ").append("\n");
//        stringBuilder.append("    name: '").append(name).append("\n");
//        stringBuilder.append("    type: ").append(type).append("\n");
//
//        if (range != null) {
//            stringBuilder.append("    range: ").append(range).append("\n");
//        }
//
//        return stringBuilder.toString();
//    }

    public String getName() {
        return this.name;
    }

    public Type getType(){
        return this.type;
    }

    public RangeImpl getRange() {
        return this.range;
    }
}
