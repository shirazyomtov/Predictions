package DTO;

public class DTOSimulationInfo {
    private Integer simulationId;
    private String simulationDate;
    private Boolean isFinish;

    private Boolean isFailed;

    private String message;

    private String worldName;

    private String userName;

    public DTOSimulationInfo(String userName, String worldName, Integer simulationId, String simulationDate, Boolean isFinish, Boolean isFailed, String message) {
        this.userName = userName;
        this.worldName = worldName;
        this.simulationId = simulationId;
        this.simulationDate = simulationDate;
        this.isFinish = isFinish;
        this.isFailed = isFailed;
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

    public String getWorldName() {
        return worldName;
    }

    public String getUserName() {
        return userName;
    }
}
