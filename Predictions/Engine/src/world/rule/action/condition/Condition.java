package world.rule.action.condition;

import world.enums.ActionType;
import world.rule.action.Action;

public class Condition  extends Action {

    public Condition(String entityName)
    {
        super(entityName, ActionType.CONDITION);
    }
    @Override
    public void operation() {

    }
}
