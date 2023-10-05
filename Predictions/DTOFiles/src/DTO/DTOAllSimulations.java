package DTO;

import java.util.List;

public class DTOAllSimulations {
    List<DTOSimulationInfo> dtoSimulationInfos;

    public DTOAllSimulations(List<DTOSimulationInfo> dtoSimulationInfos) {
        this.dtoSimulationInfos = dtoSimulationInfos;
    }

    public List<DTOSimulationInfo> getDtoSimulationInfos() {
        return dtoSimulationInfos;
    }
}
