package world.rule.action.condition;

import exceptions.*;
import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDCondition;
import jaxb.schema.generated.PRDElse;
import jaxb.schema.generated.PRDThen;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.rule.action.Action;
import world.rule.action.ActionFactory;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCondition extends Action implements Serializable {

    List<Action> thenActions;
    List<Action> elseActions = null;

    String singularity;

    public AbstractCondition(PRDThen prdThen, PRDElse prdElse, String entityName, String singularity, PRDAction.PRDSecondaryEntity prdSecondaryEntity){
        super(entityName, ActionType.CONDITION, prdSecondaryEntity);
        this.singularity = singularity;
        if (prdThen != null) {
            thenActions = ActionFactory.createListActions(prdThen.getPRDAction());
        }
        if(prdElse != null) {
            elseActions = ActionFactory.createListActions(prdElse.getPRDAction());
        }
    }

    public String getSingularity() {
        return singularity;
    }

    public Action performThenOrElse(boolean flag, EntityInstance entityInstance, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        if(flag){
            if(thenActions != null) {
                for (Action actionThen : thenActions) {
                    if (!actionThen.getActionType().equals(ActionType.KILL) && !actionThen.getActionType().equals(ActionType.REPLACE)) {
                        actionThen.operation(entityInstance, worldInstance, secondaryEntity);
                    } else {
                        return actionThen;
                    }
                }
            }
        }
        else{
            if(elseActions != null){
                for(Action actionElse: elseActions){
                    if (!actionElse.getActionType().equals(ActionType.KILL) && !actionElse.getActionType().equals(ActionType.REPLACE)) {
                        actionElse.operation(entityInstance, worldInstance, secondaryEntity);
                    }
                    else {
                        return actionElse;
                    }
                }
            }
        }
        return null;
    }

    public static AbstractCondition createCondition(PRDCondition prdCondition, String entityName){
        AbstractCondition selectedAction = null;

        if(prdCondition.getSingularity().equals("single")) {
            selectedAction = new SingleCondition(null, null, prdCondition, null);
        }
        else if(prdCondition.getSingularity().equals("multiple")){
            selectedAction = new MultipleCondition(null, null, prdCondition, null, entityName);
        }

        return selectedAction;
    }

    public String getAmountOfThenActions() {
        return String.valueOf(thenActions.size());
    }

    public String getAmountOfElseActions() {
        if(elseActions != null) {
            return String.valueOf(elseActions.size());
        }
        else {
            return "";
        }
    }
}
