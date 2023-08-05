package world.entity.definition;

import jaxb.schema.generated.PRDProperty;
import world.enums.Type;
import world.range.Range;

public class PropertyDefinition {
    private final String name;

    private final Type type;

    private final boolean isRandomInitialize;

    private final Object init;

    private final Range range;

    public PropertyDefinition(PRDProperty prdProperty) {
        this.name = prdProperty.getPRDName();
        this.type = Enum.valueOf(Type.class, prdProperty.getType().toUpperCase());
        this.isRandomInitialize = prdProperty.getPRDValue().isRandomInitialize();
        this.init = prdProperty.getPRDValue().getInit();
        this.range = new Range((float) prdProperty.getPRDRange().getFrom(), (float) prdProperty.getPRDRange().getTo());
    }

    @Override
    public String toString() {
        return "Property{" +
                "Name='" + name + '\'' +
                ", Type=" + type.name() +
                ", Is random initialize=" + isRandomInitialize +
                ", Range=" + range +
                '}';
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isRandomInitialize() {
        return isRandomInitialize;
    }

    public Object getInit() {
        return init;
    }

    public Range getRange() {
        return range;
    }
}
