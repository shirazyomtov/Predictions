package DTO.DTOActions;


public class DTOActionInfo {

    private final String actionName;

    private final String entityName;

    private final String secondEntityName;

    public DTOActionInfo(String actionName, String entityName, String secondEntityName) {
        this.actionName = actionName;
        this.entityName = entityName;
        this.secondEntityName = secondEntityName;
    }

    public String getActionName() {
        return actionName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getSecondEntityName() {
        return secondEntityName;
    }
}
