package DTO;

import java.util.List;

public class DTOWorldDefinitionInfo {

    private String worldName;
    private List<DTOEntityInfo> entitiesList;

    private List<DTORuleInfo> rulesList;

    private List<DTOEnvironmentInfo> environmentsList;

    private DTOGrid grid;

    public DTOWorldDefinitionInfo(String worldName, List<DTOEntityInfo> entitiesList, List<DTORuleInfo> rulesList, List<DTOEnvironmentInfo> environmentsList, DTOGrid grid) {
        this.entitiesList = entitiesList;
        this.rulesList = rulesList;
        this.environmentsList = environmentsList;
        this.grid = grid;
        this.worldName = worldName;
    }

    public String getWorldName() {
        return worldName;
    }

    public List<DTOEntityInfo> getEntitiesList() {
        return entitiesList;
    }

    public List<DTORuleInfo> getRulesList() {
        return rulesList;
    }

    public List<DTOEnvironmentInfo> getEnvironmentsList() {
        return environmentsList;
    }

    public DTOGrid getGrid() {
        return grid;
    }
}
