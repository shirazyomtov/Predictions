package world.entity.definition;


import java.util.Map;

public class EntityDefinition {
    private final String name;
    private final Integer amountOfPopulation;
    private final Map<String, PropertyDefinition> allProperties;

    public EntityDefinition(String name, Integer amountOfPopulation, Map<String, PropertyDefinition> allProperties)
    {
        this.name = name;
        this.amountOfPopulation = amountOfPopulation;
        this.allProperties = allProperties;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "Name='" + name + '\'' +
                ", Amount of population=" + amountOfPopulation +
                ", All properties=" + allProperties +
                '}';
    }

    public String getName() {
        return name;
    }

    public Integer getAmountOfPopulation() {
        return amountOfPopulation;
    }

    public Map<String, PropertyDefinition> getAllProperties() {
        return allProperties;
    }
}
