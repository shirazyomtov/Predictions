package world.rule.action.calculation;

import world.enums.ActionType;
import world.rule.action.Action;

public abstract class Calculation extends Action
{
    private String resultPropertyName;
    public Calculation(String entityName, String resultPropertyName) {
        super(entityName, ActionType.CALCULATION);
        this.resultPropertyName = resultPropertyName;
    }
}
