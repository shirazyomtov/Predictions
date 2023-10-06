package history;

import DTO.DTOSimulationInfo;
import history.simulation.Simulation;
import threadManager.ThreadManager;

import java.io.Serializable;
import java.util.*;

public final class History implements Serializable {
    private Map<Integer, Simulation> allSimulations = new HashMap<>();
    private Integer currentSimulationNumber = 0;

    public  void setCurrentSimulationNumber(Integer currentSimulationNumber) {
        this.currentSimulationNumber = currentSimulationNumber;
    }

    public Simulation getSimulation() {
        return allSimulations.get(currentSimulationNumber);
    }

    public  void addSimulation(Simulation simulation){
        allSimulations.put(currentSimulationNumber, simulation);
    }

    public Map<Integer, Simulation> getAllSimulations() {
        return allSimulations;
    }

    public synchronized void removeCurrentSimulation(){
        allSimulations.remove(currentSimulationNumber);
        setCurrentSimulationNumber(currentSimulationNumber -1);
    }

    public void setAllSimulations(Map<Integer, Simulation> allSimulations) {
        this.allSimulations = allSimulations;
    }

    public Integer getCurrentSimulationNumber() {
        return currentSimulationNumber;
    }

    public TreeMap<Integer, Simulation> getSortMapOfSimulations() {
        return new TreeMap<>(allSimulations);
    }


}
