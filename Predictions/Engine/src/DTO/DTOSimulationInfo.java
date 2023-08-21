package DTO;

public class DTOSimulationInfo {
    private Integer simulationId;
    private String simulationDate;

    public DTOSimulationInfo(Integer simulationId, String simulationDate) {
        this.simulationId = simulationId;
        this.simulationDate = simulationDate;
    }

    public Integer getSimulationId() {
        return simulationId;
    }

    public String getSimulationDate() {
        return simulationDate;
    }
}
