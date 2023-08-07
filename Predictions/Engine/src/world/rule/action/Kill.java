package world.rule.action;

import world.enums.ActionType;

public class Kill extends Action {


    public Kill(String entityName)
    {
        super(entityName, ActionType.KILL);
    }
    @Override
    public void operation() {

    }
}
