package allocations;

import world.termination.Termination;

public class Allocation {
    private String simulationName;
    private String userName;

    private Integer numberOfSimulationRun;
    private Termination termination;

    private StatusRequest statusRequest;

    private Integer numberOfRunningSimulationNow = 0;
    private Integer numberFinishSimulation = 0;

    public Allocation(String simulationName, Integer numberOfSimulationRun, Termination termination, String userName) {
        this.simulationName = simulationName;
        this.numberOfSimulationRun = numberOfSimulationRun;
        this.termination = termination;
        this.statusRequest = StatusRequest.PENDING;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public Integer getNumberOfRunningSimulationNow() {
        return numberOfRunningSimulationNow;
    }

    public Integer getNumberFinishSimulation() {
        return numberFinishSimulation;
    }

    public void setNumberOfRunningSimulationNow() {
        this.numberOfRunningSimulationNow++;
    }

    public void setNumberFinishSimulation(Integer numberFinishSimulation) {
        this.numberFinishSimulation = numberFinishSimulation;
    }
}
