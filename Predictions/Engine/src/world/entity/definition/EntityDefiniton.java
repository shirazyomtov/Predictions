package world.entity.definition;

import java.util.List;

public interface EntityDefiniton {
    String getName();
    int getAmountOfPopulation();
    List<PropertyDefinition> getProps();
}
