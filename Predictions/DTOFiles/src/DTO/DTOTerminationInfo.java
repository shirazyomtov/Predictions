package DTO;

public class DTOTerminationInfo {
    private Integer ticks;
    private Integer second;

    public DTOTerminationInfo(Integer ticks, Integer second) {
        this.ticks = ticks;
        this.second = second;
    }

    public Integer getTicks() {
        return ticks;
    }

    public Integer getSecond() {
        return second;
    }
}
