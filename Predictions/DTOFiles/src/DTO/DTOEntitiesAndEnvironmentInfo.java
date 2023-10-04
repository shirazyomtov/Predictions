package DTO;

import java.util.List;

public class DTOEntitiesAndEnvironmentInfo {
    List<DTOEntityInfo> dtoEntityInfoList;
    List<DTOEnvironmentInfo> dtoEnvironmentInfoList;

    public DTOEntitiesAndEnvironmentInfo(List<DTOEntityInfo> dtoEntityInfoList, List<DTOEnvironmentInfo> dtoEnvironmentInfoList) {
        this.dtoEntityInfoList = dtoEntityInfoList;
        this.dtoEnvironmentInfoList = dtoEnvironmentInfoList;
    }

    public List<DTOEntityInfo> getDtoEntityInfoList() {
        return dtoEntityInfoList;
    }

    public List<DTOEnvironmentInfo> getDtoEnvironmentInfoList() {
        return dtoEnvironmentInfoList;
    }
}
