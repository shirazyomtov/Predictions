package DTO;

public class DTOQueueManagementInfo {
    private Integer numberOfRunningSimulations = 0;
    private Integer numberOfSimulationsInQueue = 0;
    private Integer numberOfFinishSimulations = 0;

    public DTOQueueManagementInfo(Integer numberOfRunningSimulations, Integer numberOfSimulationsInQueue, Integer numberOfFinishSimulations) {
        this.numberOfRunningSimulations = numberOfRunningSimulations;
        this.numberOfSimulationsInQueue = numberOfSimulationsInQueue;
        this.numberOfFinishSimulations = numberOfFinishSimulations;
    }

    public Integer getNumberOfRunningSimulations() {
        return numberOfRunningSimulations;
    }

    public Integer getNumberOfSimulationsInQueue() {
        return numberOfSimulationsInQueue;
    }

    public Integer getNumberOfFinishSimulations() {
        return numberOfFinishSimulations;
    }
}
