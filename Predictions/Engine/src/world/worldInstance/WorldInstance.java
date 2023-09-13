package world.worldInstance;

import DTO.DTOEnvironmentInfo;
import DTO.DTOGrid;
import world.entity.instance.EntityInstance;
import world.entity.instance.location.Location;
import world.environment.instance.EnvironmentInstance;
import world.twoDimensionalGrid.TwoDimensionalGrid;
import world.worldDefinition.WorldDefinition;

import java.io.Serializable;
import java.util.*;

public class WorldInstance implements Serializable {

    private  Map<String, EnvironmentInstance> environmentInstanceMap = new HashMap<>();

    private List<EntityInstance> entityInstanceList = null;

    private WorldDefinition worldDefinition = null;

    private TwoDimensionalGrid twoDimensionalGrid;
    private Map<String, Integer> initAmountOfEntities;

    private Map<String, Integer> currentAmountOfEntities;

    private Map<Integer, Map<String, Integer>> amountOfEntitiesPerTick = new HashMap<>();

    private Map<Integer, Integer> secondsPerTick = new HashMap<>();


    public WorldInstance(Map<String, EnvironmentInstance> environmentInstanceMap, List<EntityInstance> entityInstanceList, WorldDefinition worldDefinition, Map<String, Integer> initAmountOfEntities, Map<String, Integer> currentAmountOfEntities, int rows, int cols) {
        this.entityInstanceList = entityInstanceList;
        this.worldDefinition = worldDefinition;
        this.initAmountOfEntities = initAmountOfEntities;
        this.currentAmountOfEntities = currentAmountOfEntities;
        this.twoDimensionalGrid = new TwoDimensionalGrid(rows,cols);
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

        public TwoDimensionalGrid getTwoDimensionalGrid() {
            return twoDimensionalGrid;
    }

    public void initLocation(){
        for(EntityInstance entityInstance: entityInstanceList){
            entityInstance.setLocation(twoDimensionalGrid.createNewLocation());
        }
    }

    public void addAmountOfEntitiesPerTick(Integer tick){
        Map<String, Integer> amountOfEntitiesInSpecificTick = new HashMap<>();
        for(String entityName: currentAmountOfEntities.keySet()){
            amountOfEntitiesInSpecificTick.put(entityName, currentAmountOfEntities.get(entityName));
        }
        amountOfEntitiesPerTick.put(tick, amountOfEntitiesInSpecificTick);
    }

    public Map<Integer, Map<String, Integer>> getAmountOfEntitiesPerTick() {
        return amountOfEntitiesPerTick;
    }

    public Float getAverageTickValueOfSpecificProperty(String entityName, String propertyName){
        float sum = 0;
        float count = 0;
        for (EntityInstance entityInstance: entityInstanceList){
            if(entityInstance.getName().equals(entityName)){
                sum = sum + entityInstance.getAvgAmountOfTickTheValueDosentChange(propertyName);
                count++;
            }
        }
        return  sum/count;
    }

    public void setSecondsPerTick(Integer tick, Integer second) {
        this.secondsPerTick.put(tick, second);
    }

    public Map<Integer, Integer> getSecondsPerTick() {
        return secondsPerTick;
    }
}