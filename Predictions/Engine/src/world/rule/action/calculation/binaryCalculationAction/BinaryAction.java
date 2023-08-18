package world.rule.action.calculation.binaryCalculationAction;

import world.rule.action.calculation.CalculationImpl;
import world.rule.action.expression.ExpressionIml;

import java.io.Serializable;

public abstract class BinaryAction extends CalculationImpl implements Serializable {

    private final ExpressionIml argument1;
    private final ExpressionIml argument2;

    public BinaryAction(String entityName, String resultPropertyName, String argument1, String argument2) {
        super(entityName, resultPropertyName);
        this.argument1 = new ExpressionIml(argument1, resultPropertyName);
        this.argument2 = new ExpressionIml(argument2, resultPropertyName);
    }

    public ExpressionIml getArgument1() {
        return argument1;
    }

    public ExpressionIml getArgument2() {
        return argument2;
    }


}
