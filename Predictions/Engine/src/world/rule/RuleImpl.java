package world.rule;

import DTO.DTOActionInfo;
import DTO.DTOActivationInfo;
import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDRule;
import world.enums.ActionType;
import world.rule.action.Action;
import world.rule.action.ActionFactory;
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
            allDTOAction.add(new DTOActionInfo(action.getActionType().toString()));

        }

        return allDTOAction;
    }

    public DTOActivationInfo getDTOActivation(){
        return new DTOActivationInfo(activation.getTicks(), activation.getProbability());
    }
}
