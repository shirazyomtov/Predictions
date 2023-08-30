package world.rule.action.calculation.binaryCalculationAction;

import world.enums.CalculationBinaryTypeAction;
import world.rule.action.calculation.CalculationImpl;
import world.rule.action.expression.ExpressionIml;

import java.io.Serializable;

public abstract class BinaryAction extends CalculationImpl implements Serializable {

    private final CalculationBinaryTypeAction typeOfCalculation;
    private final ExpressionIml argument1;
    private final ExpressionIml argument2;

    public BinaryAction(String entityName, CalculationBinaryTypeAction typeOfCalculation, String resultPropertyName, String argument1, String argument2) {
        super(entityName, resultPropertyName);
        this.typeOfCalculation = typeOfCalculation;
        this.argument1 = new ExpressionIml(argument1, resultPropertyName);
        this.argument2 = new ExpressionIml(argument2, resultPropertyName);
    }

    public ExpressionIml getArgument1() {
        return argument1;
    }

    public ExpressionIml getArgument2() {
        return argument2;
    }

    public CalculationBinaryTypeAction getTypeOfCalculation() {
        return typeOfCalculation;
    }
}
