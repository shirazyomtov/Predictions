package world.entity.definition;


import java.util.List;


public class EntityDefinitionImpl implements EntityDefiniton {
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
        return "Entity{" +
                "Name='" + name + '\'' +
                ", Amount of population=" + amountOfPopulation +
                ", All properties=" + allProperties +
                '}';
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
