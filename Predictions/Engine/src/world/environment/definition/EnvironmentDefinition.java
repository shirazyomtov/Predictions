package world.environment.definition;

import jaxb.schema.generated.PRDEnvProperty;
import world.enums.Type;
import world.range.Range;

public class EnvironmentDefinition {

    private final String name;

    private final Type type;

    private Range range = null;

    public EnvironmentDefinition(PRDEnvProperty prdEnvProperty){
        this.name = prdEnvProperty.getPRDName();
        this.type = Enum.valueOf(Type.class, prdEnvProperty.getType().toUpperCase());
        if(prdEnvProperty.getPRDRange()!= null) {
            this.range = new Range((float) prdEnvProperty.getPRDRange().getFrom(), (float) prdEnvProperty.getPRDRange().getTo());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Environment{");
        stringBuilder.append("name='").append(name).append('\'');
        stringBuilder.append(", type=").append(type);

        if (range != null) {
            stringBuilder.append(", range=").append(range);
        }

        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
