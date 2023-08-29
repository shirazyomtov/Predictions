package world.rule.action.calculation.binaryCalculationAction;

import jaxb.schema.generated.PRDAction;
import world.enums.CalculationBinaryTypeAction;
import world.rule.action.*;

public class BinaryCalculationActionFactory {
    public static Action createBinaryAction(CalculationBinaryTypeAction type, PRDAction prdAction)
    {
        Action selectedAction = null;
        switch (type) {
            case MULTIPLY:
                selectedAction = new Multiply(prdAction);
                break;
            case DIVIDE:
                selectedAction = new Divide(prdAction);
                break;
        }

        return selectedAction;
    }
}
