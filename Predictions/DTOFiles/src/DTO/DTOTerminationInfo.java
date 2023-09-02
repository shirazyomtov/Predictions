package DTO;

public class DTOTerminationInfo {
    private Integer ticks;
    private Integer second;

    private Boolean terminationByUser;

    public DTOTerminationInfo(Integer ticks, Integer second, Boolean terminationByUser) {
        this.ticks = ticks;
        this.second = second;
        this.terminationByUser = terminationByUser;
    }

    public Integer getTicks() {
        return ticks;
    }

    public Integer getSecond() {
        return second;
    }

    public Boolean getTerminationByUser() {
        return terminationByUser;
    }
}
