package world;

import world.entity.definition.EntityDefinition;

import java.util.Map;

public final class World {
    private final Map<String, EntityDefinition> entityDefinition;

    public World(Map<String, EntityDefinition> entityDefinition)
    {
        this.entityDefinition = entityDefinition;
    }

    public Map<String, EntityDefinition> getEntityDefinition() {
        return entityDefinition;
    }
}
