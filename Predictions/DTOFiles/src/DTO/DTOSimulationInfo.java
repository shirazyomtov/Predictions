package DTO;

public class DTOSimulationInfo {
    private Integer simulationId;
    private String simulationDate;
    private Boolean isFinish;

    private Boolean isFailed;

    private Boolean isBonusActive;

    private String message;

    public DTOSimulationInfo(Integer simulationId, String simulationDate, Boolean isFinish, Boolean isFailed, Boolean isBonusActive, String message) {
        this.simulationId = simulationId;
        this.simulationDate = simulationDate;
        this.isFinish = isFinish;
        this.isFailed = isFailed;
        this.isBonusActive = isBonusActive;
        this.message = message;
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

    public Boolean getFailed() {
        return isFailed;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getBonusActive() {
        return isBonusActive;
    }
}
