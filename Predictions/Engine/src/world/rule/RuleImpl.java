package world.rule;

import DTO.DTOActions.*;
import DTO.DTOActions.DTOCondition.DTOConditionMultiple;
import DTO.DTOActions.DTOCondition.DTOConditionSingle;
import DTO.DTOActivationInfo;
import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDRule;
import world.rule.action.*;
import world.rule.action.calculation.binaryCalculationAction.BinaryAction;
import world.rule.action.condition.MultipleCondition;
import world.rule.action.condition.SingleCondition;
import world.rule.activation.ActivationImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
                dtoActionInfo = new DTOIncreaseAndDecrease(increaseAction.getActionType().toString(), increaseAction.getEntityName(), action.getSecondaryEntityAsString(), increaseAction.getPropertyName(), increaseAction.getExpression());
                break;
            case DECREASE:
                Decrease decreaseAction = (Decrease)action;
                dtoActionInfo = new DTOIncreaseAndDecrease(decreaseAction.getActionType().toString(), decreaseAction.getEntityName(), action.getSecondaryEntityAsString(), decreaseAction.getPropertyName(), decreaseAction.getExpression());
                break;
            case CALCULATION:
                BinaryAction binaryAction = (BinaryAction)action;
                dtoActionInfo = new DTOCalculation(binaryAction.getActionType().toString(), binaryAction.getEntityName(), action.getSecondaryEntityAsString(), binaryAction.getTypeOfCalculation().toString(), binaryAction.getResultPropertyName(),
                                                    binaryAction.getArgument1().getExpressionName(), binaryAction.getArgument2().getExpressionName());
                break;
            case CONDITION:
                if(action instanceof MultipleCondition){
                    MultipleCondition multipleCondition = (MultipleCondition)action;
                    dtoActionInfo = new DTOConditionMultiple(multipleCondition.getActionType().toString(), multipleCondition.getEntityName(), action.getSecondaryEntityAsString(), "multiple", multipleCondition.getAmountOfThenActions(), multipleCondition.getAmountOfElseActions(),
                                                            multipleCondition.getLogical(), multipleCondition.getConditions().size());
                }
                else{
                    SingleCondition singleCondition = (SingleCondition)action;
                    dtoActionInfo = new DTOConditionSingle(singleCondition.getActionType().toString(), singleCondition.getEntityName(), action.getSecondaryEntityAsString(), "single", singleCondition.getAmountOfThenActions(), singleCondition.getAmountOfElseActions(),
                                                            singleCondition.getPropertyName(), singleCondition.getOperator(), singleCondition.getValue());
                }
                break;
            case SET:
                Set set = (Set)action;
                dtoActionInfo = new DTOSet(set.getActionType().toString(), set.getEntityName(), action.getSecondaryEntityAsString(), set.getPropertyName(), set.getExpression());
                break;
            case KILL:
                dtoActionInfo = new DTOActionInfo(action.getActionType().toString(), action.getEntityName(), action.getSecondaryEntityAsString());
                break;
            case REPLACE:
                Replace replace = (Replace)action;
                dtoActionInfo = new DTOReplace(replace.getActionType().toString(), replace.getEntityName(), replace.getSecondaryEntityAsString(), replace.getCreateEntityName(), replace.getMode());
                break;
            case PROXIMITY:
                Proximity proximity = (Proximity)action;
                dtoActionInfo = new DTOProximity(proximity.getActionType().toString(), proximity.getEntityName(), proximity.getSecondaryEntityAsString(), proximity.getTargetEntity(), proximity.getOf(), proximity.getActions().size());
                break;
        }

        return dtoActionInfo;
    }

    public DTOActivationInfo getDTOActivation(){
        return new DTOActivationInfo(activation.getTicks(), activation.getProbability());
    }
}
