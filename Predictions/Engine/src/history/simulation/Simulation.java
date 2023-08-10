package history.simulation;

import world.worldInstance.WorldInstance;

public  class Simulation {
    private WorldInstance worldInstance = null;

    public Simulation(WorldInstance worldInstance) {
        this.worldInstance = worldInstance;
    }

    public WorldInstance getWorldInstance() {
        return worldInstance;
    }
}
