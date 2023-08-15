package world.rule.action;

import exceptions.ObjectNotExist;
import exceptions.OperationNotSupportedType;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.util.WeakHashMap;

public abstract class Action implements ActionOperation, Serializable {

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

    public abstract boolean operation(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType;

    public String getEntityName() {
        return entityName;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
