package allocations;

import world.termination.Termination;

import java.util.HashMap;
import java.util.Map;

public class Allocations {
    Map<Integer, Allocation> allAllocation= new HashMap<>();
    Integer numberOfAllocation = 0;

    public Map<Integer, Allocation> getAllAllocation() {
        return allAllocation;
    }
    public synchronized void addAllocation(String simulationName, String numberOfSimulationRun, String ticks, String second, String byUser){
        numberOfAllocation++;
        Termination termination = new Termination(ticks, second, byUser);
        allAllocation.put(numberOfAllocation, new Allocation(simulationName, Integer.parseInt(numberOfSimulationRun) , termination));
    }
}
