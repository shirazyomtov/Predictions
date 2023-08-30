package DTO.DTOActions;

public class DTOProximity extends DTOActionInfo{

    private final String targetEntity;
    private final String of;
    private final String amountOfActions;

    public DTOProximity(String actionName, String entityName, String secondEntityName, String targetEntity, String of, int amountOfActions) {
        super(actionName, entityName, secondEntityName);
        this.targetEntity = targetEntity;
        this.of = of;
        this.amountOfActions = String.valueOf(amountOfActions);
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public String getOf() {
        return of;
    }

    public String getAmountOfActions() {
        return amountOfActions;
    }
}
