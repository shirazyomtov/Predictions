package world.entity.definition;


import DTO.DTOPropertyInfo;
import DTO.DTORangeInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class EntityDefinitionImpl implements EntityDefinition, Serializable {
    private final String name;
    private  Integer amountOfPopulation = 0;
    private final List<PropertyDefinition> allProperties;

    public EntityDefinitionImpl(String name, Integer amountOfPopulation, List<PropertyDefinition> allProperties)
    {
        this.name = name;
        this.amountOfPopulation = amountOfPopulation;
        this.allProperties = allProperties;
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

    public List<DTOPropertyInfo> getDTOProperties() {
        List<DTOPropertyInfo> propertyInfos = new ArrayList<>();
        for(PropertyDefinition propertyDefinition: allProperties){
            if(propertyDefinition.getRange() != null) {
                propertyInfos.add(new DTOPropertyInfo(propertyDefinition.getName(), propertyDefinition.getType().toString(), propertyDefinition.isRandomInitialize(),
                        new DTORangeInfo(propertyDefinition.getRange().getFrom().toString(), propertyDefinition.getRange().getTo().toString())));
            }
            else{
                propertyInfos.add(new DTOPropertyInfo(propertyDefinition.getName(), propertyDefinition.getType().toString(), propertyDefinition.isRandomInitialize(),null));
            }
        }
        return propertyInfos;
    }

    public void setAmountOfPopulation(Integer amountOfPopulation) {
        this.amountOfPopulation = amountOfPopulation;
    }
}
