package DTO;


public class DTOActionInfo {

    private final String actionName;

    private final String entityName;

    public DTOActionInfo(String actionName, String entityName) {
        this.actionName = actionName;
        this.entityName = entityName;
    }

    public String getActionName() {
        return actionName;
    }

    public String getEntityName() {
        return entityName;
    }
}
