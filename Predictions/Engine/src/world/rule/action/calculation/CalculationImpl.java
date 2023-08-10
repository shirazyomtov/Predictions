package world.rule.action.calculation;

import world.enums.ActionType;
import world.rule.action.Action;

public abstract class CalculationImpl extends Action
{
    private final String resultPropertyName;
    public CalculationImpl(String entityName, String resultPropertyName) {
        super(entityName, ActionType.CALCULATION);
        this.resultPropertyName = resultPropertyName;
    }

    public String getResultPropertyName() {
        return resultPropertyName;
    }

}
