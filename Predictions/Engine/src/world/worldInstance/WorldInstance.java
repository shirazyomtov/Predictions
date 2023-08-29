package world.worldInstance;

import DTO.DTOEnvironmentInfo;
import world.entity.instance.EntityInstance;
import world.environment.definition.EnvironmentDefinition;
import world.environment.instance.EnvironmentInstance;
import world.worldDefinition.WorldDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class WorldInstance implements Serializable {

    private  Map<String, EnvironmentInstance> environmentInstanceMap = null;

    private List<EntityInstance> entityInstanceList = null;

    private WorldDefinition worldDefinition = null;


    private int currentTick = 1;
    public WorldInstance(Map<String, EnvironmentInstance> environmentInstanceMap, List<EntityInstance> entityInstanceList, WorldDefinition worldDefinition) {
        this.environmentInstanceMap = environmentInstanceMap;
        this.entityInstanceList = entityInstanceList;
        this.worldDefinition = worldDefinition;
    }


    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }
    public Map<String, EnvironmentInstance> getEnvironmentInstanceMap() {
        return environmentInstanceMap;
    }

    public List<EntityInstance> getEntityInstanceList() {
        return entityInstanceList;
    }

    public WorldDefinition getWorldDefinition() {
        return worldDefinition;
    }

    public List<DTOEnvironmentInfo>  createListEnvironmentNamesAndValues() {
        List<DTOEnvironmentInfo> dtoEnvironments = new ArrayList<>();
        Collection<EnvironmentInstance> collectionOfEnvironment = environmentInstanceMap.values();
        for (EnvironmentInstance environmentInstance: collectionOfEnvironment) {
            dtoEnvironments.add(new DTOEnvironmentInfo(environmentInstance.getProperty().getName(), environmentInstance.getProperty().getValue().toString()));
        }

        return dtoEnvironments;
    }

    public EntityInstance isEntityExists(String entityName){
        for(EntityInstance entityInstance: entityInstanceList){
            if(entityInstance.getName().equals(entityName)){
                return entityInstance;
            }
        }

        return null;
    }

    public void addEntityInstanceToEntityInstanceList(EntityInstance entityInstance){
        entityInstanceList.add(entityInstance);
    }

}