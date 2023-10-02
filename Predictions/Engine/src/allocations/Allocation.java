package allocations;

import world.termination.Termination;

public class Allocation {
    private String simulationName;
   // private String userName;
    private Integer numberOfSimulationRun;
    private Termination termination;

    private StatusRequest statusRequest;

    private Integer numberOfRunningSimulationNow = 0;
    private Integer numberFinishSimulation = 0;

    public Allocation(String simulationName, Integer numberOfSimulationRun, Termination termination) {
        this.simulationName = simulationName;
        this.numberOfSimulationRun = numberOfSimulationRun;
        this.termination = termination;
        this.statusRequest = StatusRequest.PENDING;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public Integer getNumberOfSimulationRun() {
        return numberOfSimulationRun;
    }

    public Termination getTermination() {
        return termination;
    }

    public StatusRequest getStatusRequest() {
        return statusRequest;
    }

    public void updateStatus(String status) {
        statusRequest = StatusRequest.valueOf(status);
    }
}
