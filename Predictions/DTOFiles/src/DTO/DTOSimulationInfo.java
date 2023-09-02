package DTO;

public class DTOSimulationInfo {
    private Integer simulationId;
    private String simulationDate;
    private Boolean isFinish;

    public DTOSimulationInfo(Integer simulationId, String simulationDate, Boolean isFinish) {
        this.simulationId = simulationId;
        this.simulationDate = simulationDate;
        this.isFinish = isFinish;
    }

    public Integer getSimulationId() {
        return simulationId;
    }

    public String getSimulationDate() {
        return simulationDate;
    }

    public Boolean getFinish() {
        return isFinish;
    }
}
