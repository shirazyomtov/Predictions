package world.worldInstance;

import DTO.DTOEnvironmentInfo;
import world.entity.instance.EntityInstance;
import world.environment.instance.EnvironmentInstance;
import world.worldDefinition.WorldDefinition;

import java.io.Serializable;
import java.util.*;

public class WorldInstance implements Serializable {

    private  Map<String, EnvironmentInstance> environmentInstanceMap = new HashMap<>();

    private List<EntityInstance> entityInstanceList = null;

    private WorldDefinition worldDefinition = null;

    private Map<String, Integer> initAmountOfEntities;

    private Map<String, Integer> currentAmountOfEntities;


    public WorldInstance(Map<String, EnvironmentInstance> environmentInstanceMap, List<EntityInstance> entityInstanceList, WorldDefinition worldDefinition, Map<String, Integer> initAmountOfEntities, Map<String, Integer> currentAmountOfEntities) {
        this.entityInstanceList = entityInstanceList;
        this.worldDefinition = worldDefinition;
        this.initAmountOfEntities = initAmountOfEntities;
        this.currentAmountOfEntities = currentAmountOfEntities;
        createEnvironmentMap(environmentInstanceMap);
    }

    private void createEnvironmentMap(Map<String, EnvironmentInstance> environmentsInstance) {
        for(String environmentName: environmentsInstance.keySet()){
            worldDefinition.checkValidationValue(environmentName, environmentsInstance.get(environmentName).getProperty().getValue().toString(), environmentInstanceMap);
        }
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

    public Map<String, Integer> getInitAmountOfEntities() {
        return initAmountOfEntities;
    }

    public Map<String, Integer> getCurrentAmountOfEntities() {
        return currentAmountOfEntities;
    }

    public void setCurrentAmountOfEntitiesAfterKill(EntityInstance entityToRemove) {
        currentAmountOfEntities.put(entityToRemove.getName(), currentAmountOfEntities.get(entityToRemove.getName()) - 1);
    }

    public void setCurrentAmountOfEntitiesAfterReplace(EntityInstance entityToRemove) {
        currentAmountOfEntities.put(entityToRemove.getName(), currentAmountOfEntities.get(entityToRemove.getName()) + 1);
    }
}