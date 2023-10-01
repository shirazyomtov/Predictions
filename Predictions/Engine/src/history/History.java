package history;

import DTO.DTOSimulationInfo;
import history.simulation.Simulation;
import threadManager.ThreadManager;

import java.io.Serializable;
import java.util.*;

public final class History implements Serializable {
    private Map<Integer, Simulation> allSimulations = new HashMap<>();
    private Integer currentSimulationNumber = 0;

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

    public List<DTOSimulationInfo> getDTOSimulations(){
        List<DTOSimulationInfo> dtoSimulations = new ArrayList<>();
        TreeMap<Integer, Simulation> allSimulation = getSortMapOfSimulations();
        for (Map.Entry<Integer, Simulation> entry : allSimulation.entrySet()) {
            Integer simulationId = entry.getKey();
            Simulation simulation = entry.getValue();
            String formattedDateTime = simulation.getFormattedDateTime();

            dtoSimulations.add(new DTOSimulationInfo(simulationId, formattedDateTime, simulation.getIsFinish(), simulation.getIsFailed(), simulation.getIsBonus(), simulation.getMessage()));
        }

        return dtoSimulations;
    }

}
