package DTO.DTOActions.DTOCondition;

public class DTOConditionMultiple extends DTOCondition {

    private String logical;
    private String amountOfConditions;

    public DTOConditionMultiple(String actionName, String entityName, String secondEntityName, String conditionType, String amountOfActionThen, String amountOfActionElse, String logical, int amountOfConditions) {
        super(actionName, entityName, secondEntityName, conditionType, amountOfActionThen, amountOfActionElse);
        this.logical = logical;
        this.amountOfConditions = String.valueOf(amountOfConditions);
    }

    public String getLogical() {
        return logical;
    }

    public String getAmountOfConditions() {
        return amountOfConditions;
    }
}
