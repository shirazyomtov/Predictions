package world.rule.action.calculation;

import jaxb.schema.generated.PRDAction;
import world.enums.ActionType;
import world.rule.action.Action;

public abstract class CalculationImpl extends Action
{
    private final String resultPropertyName;
    public CalculationImpl(String entityName, String resultPropertyName, PRDAction.PRDSecondaryEntity prdSecondaryEntity) {
        super(entityName, ActionType.CALCULATION, prdSecondaryEntity);
        this.resultPropertyName = resultPropertyName;
    }

    public String getResultPropertyName() {
        return resultPropertyName;
    }

}
