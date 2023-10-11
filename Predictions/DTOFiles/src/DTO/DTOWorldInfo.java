package DTO;

import java.util.List;

public class DTOWorldInfo {

    private List<DTOEntityInfo> currentAmountOfEntities;

    private List<DTOEnvironmentInfo> environmentInfos;

    private int currentTick;

    private int currentSecond;

    private boolean isFinish;

    private boolean isFailed;

    public DTOWorldInfo(List<DTOEntityInfo> currentAmountOfEntities, int currentTick, int currentSecond, boolean isFinish, boolean isFailed, String message, List<DTOEnvironmentInfo> environmentInfos){
        this.currentAmountOfEntities = currentAmountOfEntities;
        this.currentTick = currentTick;
        this.currentSecond = currentSecond;
        this.isFinish = isFinish;
        this.isFailed = isFailed;
        this.environmentInfos = environmentInfos;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public int getCurrentSecond() {
        return currentSecond;
    }

    public List<DTOEntityInfo> getCurrentAmountOfEntities() {
        return currentAmountOfEntities;
    }

    public boolean getIsFinish() {
        return isFinish;
    }

    public boolean getIsFailed() {
        return isFailed;
    }

    public List<DTOEnvironmentInfo> getEnvironmentInfos() {
        return environmentInfos;
    }
}

