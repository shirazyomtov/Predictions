package world.rule.action;

import exceptions.ObjectNotExist;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;

public abstract class Action implements ActionOperation {

    private final String entityName;

    private final ActionType actionType;

    public Action(String entityName, ActionType actionType) {
        this.entityName = entityName;
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "    Action: " + "actionType = " + actionType;
    }

    public abstract void operation(EntityInstance entity) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException;

    public String getEntityName() {
        return entityName;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
