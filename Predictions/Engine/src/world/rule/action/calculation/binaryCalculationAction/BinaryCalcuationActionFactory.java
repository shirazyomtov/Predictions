package world.rule.action.calculation.binaryCalculationAction;

import world.enums.CalculationBinaryTypeAction;
import world.rule.action.*;

public class BinaryCalcuationActionFactory {
    public static Action createBinaryAction(CalculationBinaryTypeAction type, String entityName, String resultPropertyName, String argument1, String argument2)
    {
        Action selectedAction = null;
        switch (type) {
            case MULTIPLY:
                selectedAction = new Multiply(entityName, resultPropertyName, argument1, argument2);
                break;
            case DIVIDE:
                selectedAction = new Divide(entityName, resultPropertyName, argument1, argument2);
                break;
        }

        return selectedAction;
    }
}
