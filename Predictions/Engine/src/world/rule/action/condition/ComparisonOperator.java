package world.rule.action.condition;

public enum ComparisonOperator {
    LESSTHAN("bt"),
    BIGGERTHAN("lt"),
    EQUAL("="),
    NOTEQUAL("!=");

    private String operator;

    ComparisonOperator(String operator) {
        this.operator = operator;
    }

    public boolean isValidComparisonOperator(String operatorType) {
        for (ComparisonOperator option : ComparisonOperator.values()) {
            if (option.operator.equals(operatorType)) {
                return true;
            }
        }
        return false;
    }

}
