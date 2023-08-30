package DTO.DTOActions.DTOCondition;


public class DTOConditionSingle extends DTOCondition {
    private String propertyName;
    private String operator;
    private String value;

    public DTOConditionSingle(String actionName, String entityName, String conditionType, String amountOfActionThen, String amountOfActionElse, String propertyName, String operator, String value) {
        super(actionName, entityName,conditionType, amountOfActionThen, amountOfActionElse);
        this.propertyName = propertyName;
        this.operator = operator;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }
}
