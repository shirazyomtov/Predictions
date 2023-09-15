package DTO;

import java.util.List;

public class DTOWorldInfo {
//    private Map<String, Integer> currentAmountOfEntities;

    private List<DTOEntityInfo> currentAmountOfEntities;

    private int currentTick;

    private int currentSecond;

    private boolean isFinish;

    private boolean isFailed;

    public DTOWorldInfo(List<DTOEntityInfo> currentAmountOfEntities, int currentTick, int currentSecond, boolean isFinish, boolean isFailed, String message){
        this.currentAmountOfEntities = currentAmountOfEntities;
        this.currentTick = currentTick;
        this.currentSecond = currentSecond;
        this.isFinish = isFinish;
        this.isFailed = isFailed;
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
}

