package world.entity.definition;

import java.util.List;

public interface EntityDefinition {
    String getName();
    int getAmountOfPopulation();
    List<PropertyDefinition> getProps();
}
