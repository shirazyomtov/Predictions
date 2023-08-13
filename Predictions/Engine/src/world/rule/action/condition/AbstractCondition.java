package world.rule.action.condition;

import exceptions.ObjectNotExist;
import exceptions.OperationNotSupportedType;
import history.History;
import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDElse;
import jaxb.schema.generated.PRDThen;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.rule.action.Action;
import world.rule.action.ActionFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCondition extends Action {

    List<Action> thenActions;
    List<Action> elseActions = null;

    String singularity;

    public AbstractCondition(PRDThen prdThen, PRDElse prdElse, String entityName, String singularity){
        super(entityName, ActionType.CONDITION);
        this.singularity = singularity;
        if (prdThen != null) {
            thenActions = createListActions(prdThen.getPRDAction());
        }
        if(prdElse != null) {
            elseActions = createListActions(prdElse.getPRDAction());
        }
    }

    private List<Action> createListActions(List<PRDAction> prdActions) {
        List<Action> actions = new ArrayList<>();
        for(PRDAction prdAction: prdActions){
            actions.add(ActionFactory.createAction(Enum.valueOf(ActionType.class, prdAction.getType().toUpperCase()),
                    prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy(),
                    prdAction.getValue(), prdAction.getPRDMultiply(), prdAction.getPRDDivide(),
                    prdAction.getResultProp(), prdAction.getPRDCondition(), prdAction.getPRDThen(), prdAction.getPRDElse()));
        }

        return actions;
    }

    public String getSingularity() {
        return singularity;
    }

    public void performThenOrElse(boolean flag, EntityInstance entityInstance) throws ObjectNotExist, OperationNotSupportedType {
        if(flag){
            for(Action actionThen: thenActions){
                actionThen.operation(entityInstance);
            }
        }
        else{
            if(elseActions != null){
                for(Action actionElse: elseActions){
                    actionElse.operation(entityInstance);
                }
            }
        }
    }
}