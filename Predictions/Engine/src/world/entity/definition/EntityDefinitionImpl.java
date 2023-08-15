package world.entity.definition;


import java.io.Serializable;
import java.util.List;


public class EntityDefinitionImpl implements EntityDefinition, Serializable {
    private final String name;
    private final Integer amountOfPopulation;
    private final List<PropertyDefinition> allProperties;

    public EntityDefinitionImpl(String name, Integer amountOfPopulation, List<PropertyDefinition> allProperties)
    {
        this.name = name;
        this.amountOfPopulation = amountOfPopulation;
        this.allProperties = allProperties;
    }

    @Override
    public String toString() {
        StringBuilder entityDetails = new StringBuilder();
        entityDetails.append("    Entity ").append(name).append(" details: ").append("\n");
        entityDetails.append("        Name = '").append(name).append("',").append("\n");
        entityDetails.append("        Amount of population = ").append(amountOfPopulation).append(",").append("\n");
        entityDetails.append("        All properties: ").append("\n");
        for (PropertyDefinition property : allProperties) {
            entityDetails.append("    ").append(property).append("\n");
        }
        return entityDetails.toString();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAmountOfPopulation() {
        return this.amountOfPopulation;
    }

    @Override
    public List<PropertyDefinition> getProps() {
        return this.allProperties;
    }
}
