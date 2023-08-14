package history;

import history.simulation.Simulation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class History implements Serializable {
    private static final Object creationalLockContext = new Object();
    private static History instance = null;
    private Map<Integer, Simulation> allSimulations = new HashMap<>();
    private Integer currentSimulationNumber = 0;

    private History() {

    }

    public static History getInstance() {
        if (instance == null) {
            synchronized (creationalLockContext) {
                if (instance == null) {
                    instance = new History();
                }
            }
        }

        return instance;
    }
    public void setCurrentSimulationNumber(Integer currentSimulationNumber) {
        this.currentSimulationNumber = currentSimulationNumber;
    }

    public Simulation getSimulation() {
        return allSimulations.get(currentSimulationNumber);
    }

    public void addSimulation(Simulation simulation){
        allSimulations.put(currentSimulationNumber, simulation);
    }

    public Map<Integer, Simulation> getAllSimulations() {
        return allSimulations;
    }

    public void removeCurrentSimulation(){
        allSimulations.remove(currentSimulationNumber);
    }
}
