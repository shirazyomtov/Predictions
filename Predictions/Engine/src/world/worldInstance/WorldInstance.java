package world.worldInstance;

import world.entity.instance.EntityInstance;
import world.environment.definition.EnvironmentDefinition;
import world.environment.instance.EnvironmentInstance;
import world.worldDefinition.WorldDefinition;

import java.io.Serializable;
import java.util.ArrayList;
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

    public   List<String>  createListEnvironmentNamesAndValues() {
        List<String> environmentNameAndValues = new ArrayList<>();
        for (Map.Entry<String, EnvironmentInstance> environmentEntry : environmentInstanceMap.entrySet()) {
            environmentNameAndValues.add(environmentEntry.getValue().toString());
        }

        return environmentNameAndValues;
    }
}