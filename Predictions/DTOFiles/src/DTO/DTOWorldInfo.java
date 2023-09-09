package DTO;

import java.util.List;
import java.util.Map;

public class DTOWorldInfo {
//    private Map<String, Integer> currentAmountOfEntities;

    private List<DTOEntityInfo> currentAmountOfEntities;

    private int currentTick;

    private int currentSecond;

    private boolean isFinish;

    public DTOWorldInfo(List<DTOEntityInfo> currentAmountOfEntities, int currentTick, int currentSecond, boolean isFinish){
        this.currentAmountOfEntities = currentAmountOfEntities;
        this.currentTick = currentTick;
        this.currentSecond = currentSecond;
        this.isFinish = isFinish;
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
}

