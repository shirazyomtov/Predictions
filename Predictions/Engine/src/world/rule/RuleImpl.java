package world.rule;

import DTO.DTOActions.DTOActionInfo;
import DTO.DTOActions.DTOCalculation;
import DTO.DTOActions.DTOCondition.DTOConditionMultiple;
import DTO.DTOActions.DTOCondition.DTOConditionSingle;
import DTO.DTOActions.DTOSet;
import DTO.DTOActivationInfo;
import DTO.DTOActions.DTOIncreaseAndDecrease;
import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDRule;
import world.enums.ActionType;
import world.enums.CalculationBinaryTypeAction;
import world.rule.action.*;
import world.rule.action.calculation.binaryCalculationAction.BinaryAction;
import world.rule.action.condition.AbstractCondition;
import world.rule.action.condition.MultipleCondition;
import world.rule.action.condition.SingleCondition;
import world.rule.activation.ActivationImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

public class RuleImpl implements Rule, Serializable {
    private final String ruleName;

    private final ActivationImpl activation;

    private final Integer amountOfActions;
    private final List<Action> allAction = new ArrayList<>();

    public RuleImpl(PRDRule prdRule) {
      this.ruleName = prdRule.getName();
      this.activation = new ActivationImpl(prdRule.getPRDActivation());
      this.amountOfActions = prdRule.getPRDActions().getPRDAction().size();
      for (PRDAction prdAction: prdRule.getPRDActions().getPRDAction())
      {
          Action action = createAction(prdAction);
          allAction.add(action);
      }
    }

    private Action createAction(PRDAction prdAction) {
        return ActionFactory.createAction(prdAction);
//       return ActionFactory.createAction(Enum.valueOf(ActionType.class, prdAction.getType().toUpperCase()),
//                                         prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy(),
//                                         prdAction.getValue(), prdAction.getPRDMultiply(), prdAction.getPRDDivide(),
//                                         prdAction.getResultProp(), prdAction.getPRDCondition(), prdAction.getPRDThen(), prdAction.getPRDElse(),
//                                         prdAction.getKill(), prdAction.getCreate(), prdAction.getMode(),
//                                         prdAction.getPRDBetween().getSourceEntity(), prdAction.getPRDBetween().getTargetEntity(),
//                                         prdAction.getPRDEnvDepth().getOf(), prdAction.getPRDActions());
    }

    @Override
    public String getRuleName() {
        return ruleName;
    }

    @Override
    public ActivationImpl getActivation() {
        return activation;
    }

    @Override
    public List<Action> nameActions() {
        return allAction;
    }

    public Integer getAmountOfActions() {
        return amountOfActions;
    }

    public List<DTOActionInfo> getDTOActions(){
        List<DTOActionInfo> allDTOAction = new ArrayList<>();
        for(Action action: allAction){
            allDTOAction.add(setDTOActionByType(action));
        }

        return allDTOAction;
    }

    private DTOActionInfo setDTOActionByType(Action action) {
        DTOActionInfo dtoActionInfo = null;
        switch (action.getActionType()){
            case INCREASE:
                Increase increaseAction = (Increase)action;
                dtoActionInfo = new DTOIncreaseAndDecrease(increaseAction.getActionType().toString(), increaseAction.getEntityName(), increaseAction.getPropertyName(), increaseAction.getExpression());
                break;
            case DECREASE:
                Decrease decreaseAction = (Decrease)action;
                dtoActionInfo = new DTOIncreaseAndDecrease(decreaseAction.getActionType().toString(), decreaseAction.getEntityName(), decreaseAction.getPropertyName(), decreaseAction.getExpression());
                break;
            case CALCULATION:
                BinaryAction binaryAction = (BinaryAction)action;
                dtoActionInfo = new DTOCalculation(binaryAction.getActionType().toString(), binaryAction.getEntityName(), binaryAction.getTypeOfCalculation().toString(), binaryAction.getResultPropertyName(),
                                                    binaryAction.getArgument1().getExpressionName(), binaryAction.getArgument2().getExpressionName());
                break;
            case CONDITION:
                if(action instanceof MultipleCondition){
                    MultipleCondition multipleCondition = (MultipleCondition)action;
                    dtoActionInfo = new DTOConditionMultiple(multipleCondition.getActionType().toString(), multipleCondition.getEntityName(), "multiple", multipleCondition.getAmountOfThenActions(), multipleCondition.getAmountOfElseActions(),
                                                            multipleCondition.getLogical(), multipleCondition.getConditions().size());
                }
                else{
                    SingleCondition singleCondition = (SingleCondition)action;
                    dtoActionInfo = new DTOConditionSingle(singleCondition.getActionType().toString(), singleCondition.getEntityName(), "single", singleCondition.getAmountOfThenActions(), singleCondition.getAmountOfElseActions(),
                                                            singleCondition.getPropertyName(), singleCondition.getOperator(), singleCondition.getValue());
                }
                break;
            case SET:
                Set set = (Set)action;
                dtoActionInfo = new DTOSet(set.getActionType().toString(), set.getEntityName(), set.getPropertyName(), set.getExpression());
                break;
            case KILL:
                dtoActionInfo = new DTOActionInfo(action.getActionType().toString(), action.getEntityName());
        }

        return dtoActionInfo;
    }

    public DTOActivationInfo getDTOActivation(){
        return new DTOActivationInfo(activation.getTicks(), activation.getProbability());
    }
}
