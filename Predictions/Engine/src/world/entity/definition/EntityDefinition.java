package world.entity.definition;

import java.util.List;

public interface EntityDefinition {
    String getName();
    List<PropertyDefinition> getProps();
}
