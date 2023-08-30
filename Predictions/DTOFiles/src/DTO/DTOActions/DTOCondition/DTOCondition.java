package DTO.DTOActions.DTOCondition;

import DTO.DTOActions.DTOActionInfo;

public class DTOCondition extends DTOActionInfo {
    private String conditionType;
    private String amountOfActionThen;
    private String amountOfActionElse;

    public DTOCondition(String actionName, String entityName, String conditionType, String amountOfActionThen, String amountOfActionElse) {
        super(actionName, entityName);
        this.conditionType = conditionType;
        this.amountOfActionThen = amountOfActionThen;
        this.amountOfActionElse = amountOfActionElse;
    }

    public String getConditionType() {
        return conditionType;
    }

    public String getAmountOfActionThen() {
        return amountOfActionThen;
    }

    public String getAmountOfActionElse() {
        return amountOfActionElse;
    }
}
