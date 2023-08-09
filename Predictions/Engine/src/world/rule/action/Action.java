package world.rule.action;

import world.enums.ActionType;

public abstract class Action implements ActionOperation {

    private String entityName;

    private ActionType actionType;

    public Action(String entityName, ActionType actionType) {
        this.entityName = entityName;
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "    Action: " + "actionType = " + actionType;
    }

    public abstract void operation();

    public String getEntityName() {
        return entityName;
    }
}
