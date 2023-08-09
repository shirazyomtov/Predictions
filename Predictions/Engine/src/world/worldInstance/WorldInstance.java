package world.worldInstance;

import world.entity.instance.EntityInstance;
import world.environment.instance.EnvironmentInstance;

import java.util.List;
import java.util.Map;

public class WorldInstance {

    private  Map<String, EnvironmentInstance> environmentInstanceMap = null;

    private List<EntityInstance> entityInstanceList = null;

    public WorldInstance(Map<String, EnvironmentInstance> environmentInstanceMap, List<EntityInstance> entityInstanceList) {
        this.environmentInstanceMap = environmentInstanceMap;
        this.entityInstanceList = entityInstanceList;
    }

    private int currentTick = 0;

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
}
