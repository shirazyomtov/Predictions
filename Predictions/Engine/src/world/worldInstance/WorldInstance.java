package world.worldInstance;

import DTO.DTOEnvironmentInfo;
import world.entity.instance.EntityInstance;
import world.environment.instance.EnvironmentInstance;
import world.worldDefinition.WorldDefinition;

import java.io.Serializable;
import java.util.*;

public class WorldInstance implements Serializable {

    private  Map<String, EnvironmentInstance> environmentInstanceMap = null;

    private List<EntityInstance> entityInstanceList = null;

    private WorldDefinition worldDefinition = null;

    private Map<String, Integer> initAmountOfEntities;

    private Map<String, Integer> currentAmountOfEntities;

    private int currentTick = 1;

    public WorldInstance(Map<String, EnvironmentInstance> environmentInstanceMap, List<EntityInstance> entityInstanceList, WorldDefinition worldDefinition, Map<String, Integer> initAmountOfEntities, Map<String, Integer> currentAmountOfEntities) {
        this.environmentInstanceMap = environmentInstanceMap;
        this.entityInstanceList = entityInstanceList;
        this.worldDefinition = worldDefinition;
        this.initAmountOfEntities = initAmountOfEntities;
        this.currentAmountOfEntities = currentAmountOfEntities;
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

    public void setCurrentAmountOfEntities(Map<String, Integer> currentAmountOfEntities) {
        this.currentAmountOfEntities = currentAmountOfEntities;
        //todo: maybe change
    }

    public Map<String, Integer> getInitAmountOfEntities() {
        return initAmountOfEntities;
    }

    public Map<String, Integer> getCurrentAmountOfEntities() {
        return currentAmountOfEntities;
    }
}