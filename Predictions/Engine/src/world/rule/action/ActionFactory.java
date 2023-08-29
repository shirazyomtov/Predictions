package world.rule.action;

import jaxb.schema.generated.*;
import world.enums.ActionType;
import world.rule.action.calculation.binaryCalculationAction.BinaryCalculationActionFactory;
import world.rule.action.condition.MultipleCondition;
import world.rule.action.condition.SingleCondition;

import java.util.ArrayList;
import java.util.List;

import static world.enums.CalculationBinaryTypeAction.DIVIDE;
import static world.enums.CalculationBinaryTypeAction.MULTIPLY;


public final class ActionFactory {

    public static Action createAction(PRDAction prdAction)
    {
        Action selectedAction = null;
        switch (Enum.valueOf(ActionType.class, prdAction.getType().toUpperCase())) {
            case INCREASE:
                selectedAction = new Increase(prdAction);
                break;
            case DECREASE:
                selectedAction = new Decrease(prdAction);
                break;
            case CALCULATION:
                if(prdAction.getPRDMultiply() != null) {
                    selectedAction = BinaryCalculationActionFactory.createBinaryAction(MULTIPLY, prdAction);
                }
                else if (prdAction.getPRDDivide() != null) {
                    selectedAction = BinaryCalculationActionFactory.createBinaryAction(DIVIDE , prdAction);
                }
                break;
            case CONDITION:
                if(prdAction.getPRDCondition().getSingularity().equals("single")) {
                    selectedAction = new SingleCondition(prdAction.getPRDThen(), prdAction.getPRDElse(), prdAction.getPRDCondition(), prdAction.getPRDSecondaryEntity());
                }
                else if(prdAction.getPRDCondition().getSingularity().equals("multiple")){
                    selectedAction = new MultipleCondition(prdAction.getPRDThen(), prdAction.getPRDElse(), prdAction.getPRDCondition(), prdAction.getPRDSecondaryEntity(), prdAction.getEntity());
                }
                break;
            case SET:
                selectedAction = new Set(prdAction);
                break;
            case KILL:
                selectedAction = new Kill(prdAction.getEntity(), prdAction.getPRDSecondaryEntity());
                break;
            case REPLACE:
                selectedAction = new Replace(prdAction.getKill(), prdAction.getCreate(), prdAction.getMode(), prdAction.getPRDSecondaryEntity());
                break;
            case PROXIMITY:
                selectedAction = new Proximity(prdAction);
        }

        return selectedAction;
    }
    public static List<Action> createListActions(List<PRDAction> prdActions) {
        List<Action> actions = new ArrayList<>();
        for(PRDAction prdAction: prdActions){
            actions.add(ActionFactory.createAction(prdAction));
        }
        return actions;
    }
}
