package DTO;

import java.util.Map;

public class DTOAllWorldsInfo {
    private Map<String, DTOWorldDefinitionInfo> dtoWorldDefinitionInfoMap;

    public DTOAllWorldsInfo(Map<String, DTOWorldDefinitionInfo> dtoWorldDefinitionInfoMap) {
        this.dtoWorldDefinitionInfoMap = dtoWorldDefinitionInfoMap;
    }

    public Map<String, DTOWorldDefinitionInfo> getDtoWorldDefinitionInfoMap() {
        return dtoWorldDefinitionInfoMap;
    }
}
