package world.rule;

import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDRule;
import world.enums.ActionType;
import world.enums.CalculationBinaryTypeAction;
import world.enums.Type;
import world.rule.action.Action;
import world.rule.action.ActionFactory;
import world.rule.activation.Activation;

import java.util.ArrayList;
import java.util.List;

public class RuleImpl implements Rule {
    private final String ruleName;

    private final Activation activation;

    private final Integer amountOfActions;
    private final List<Action> allAction = new ArrayList<>();

    public RuleImpl(PRDRule prdRule) {
      this.ruleName = prdRule.getName();
      this.activation = new Activation(prdRule.getPRDActivation());
      this.amountOfActions = prdRule.getPRDActions().getPRDAction().size();
      for (PRDAction prdAction: prdRule.getPRDActions().getPRDAction())
      {
          Action action = createAction(prdAction);
          allAction.add(action);
      }
    }

    private Action createAction(PRDAction prdAction) {
       return ActionFactory.createAction(Enum.valueOf(ActionType.class, prdAction.getType().toUpperCase()),
                                         prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy(),
                                         prdAction.getValue(), prdAction.getPRDMultiply(), prdAction.getPRDDivide(),
                                         prdAction.getResultProp());
    }

    @Override
    public String getRuleName() {
        return ruleName;
    }

    @Override
    public Activation getActivation() {
        return activation;
    }

    @Override
    public List<Action> nameActions() {
        return allAction;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Rule Name: ").append(ruleName).append("\n");
        stringBuilder.append("Activation: ").append(activation).append("\n");
        stringBuilder.append("Amount of Actions: ").append(amountOfActions).append("\n");
        stringBuilder.append("All Actions:\n");
        if (allAction != null) {
            for (Action action : allAction) {
                stringBuilder.append(action).append("\n");
            }
        } else {
            stringBuilder.append("No actions defined.\n");
        }
        return stringBuilder.toString();
    }
}
