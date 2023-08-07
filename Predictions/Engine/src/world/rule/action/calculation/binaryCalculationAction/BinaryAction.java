package world.rule.action.calculation.binaryCalculationAction;

import world.rule.action.calculation.Calculation;
import world.rule.action.expression.ExpressionIml;

public abstract class BinaryAction extends Calculation {

    private ExpressionIml argument1;
    private ExpressionIml argument2;

    public BinaryAction(String entityName, String resultPropertyName, String argument1, String argument2) {
        super(entityName, resultPropertyName);
        this.argument1 = new ExpressionIml(argument1);
        this.argument2 = new ExpressionIml(argument2);
    }
}
