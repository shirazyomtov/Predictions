package world.entity.definition;

import jaxb.schema.generated.PRDProperty;
import world.enums.Type;
import world.range.RangeImpl;

import java.io.Serializable;

public class PropertyDefinition implements Serializable {
    private final String name;

    private final Type type;

    private final boolean isRandomInitialize;

    private Object init = null;

    private RangeImpl range = null;

    public PropertyDefinition(PRDProperty prdProperty) {
        this.name = prdProperty.getPRDName();
        this.type = Enum.valueOf(Type.class, prdProperty.getType().toUpperCase());
        this.isRandomInitialize = prdProperty.getPRDValue().isRandomInitialize();
        if (prdProperty.getPRDValue().getInit() != null) {
            this.init = prdProperty.getPRDValue().getInit();
        }
        if(prdProperty.getPRDRange()!= null) {
            this.range = new RangeImpl((float) prdProperty.getPRDRange().getFrom(), (float) prdProperty.getPRDRange().getTo());
        }
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

    public RangeImpl getRange() {
        return range;
    }
}
