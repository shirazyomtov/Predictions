package world.rule.action.condition;

import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import exceptions.OperationNotSupportedType;
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

public class MultipleCondition extends AbstractCondition implements Serializable {

    private List<AbstractCondition> conditions;
    private String logical;

    public MultipleCondition(PRDThen prdThen, PRDElse prdElse, String entityName, List<PRDCondition> conditions, String logical) {
        super(prdThen, prdElse, entityName, "multiple");
        this.conditions = createListConditions(conditions);
        this.logical = logical;
    }

    private List<AbstractCondition> createListConditions(List<PRDCondition> prdConditions) {
        List<AbstractCondition> conditionsList = new ArrayList<>();
        AbstractCondition selectedAction = null;
        for(PRDCondition prdCondition: prdConditions){
            if(prdCondition.getSingularity().equals("single")) {
                selectedAction = new SingleCondition(null, null, prdCondition.getOperator(),prdCondition.getValue(), prdCondition.getEntity(), prdCondition.getProperty());
            }
            else if(prdCondition.getSingularity().equals("multiple")){
                selectedAction = new MultipleCondition(null, null, getEntityName(), prdCondition.getPRDCondition(), prdCondition.getLogical());
            }
            conditionsList.add(selectedAction);
        }

        return conditionsList;
    }

    @Override
    public boolean operation(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes{
        boolean flag;
        boolean kill = false;
        flag = checkCondition(entity, conditions, logical, worldInstance);
        kill = performThenOrElse(flag, entity, worldInstance);
        return kill;
    }

    private boolean checkCondition(EntityInstance entity, List<AbstractCondition> conditions, String logical, WorldInstance worldInstance) throws ObjectNotExist, OperationNotSupportedType, OperationNotCompatibleTypes {
        if (logical.equals("or")) {
            for (AbstractCondition condition : conditions) {
                if (condition.getSingularity().equals("multiple")) {
                    if (checkCondition(entity, ((MultipleCondition)condition).getConditions(), ((MultipleCondition)condition).getLogical(), worldInstance)) {
                        return true;
                    }
                } else {
                    if (((SingleCondition)condition).checkIfConditionIsTrue(entity, worldInstance)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else if (logical.equals("and")) {
            for (AbstractCondition condition : conditions) {
                if (condition.getSingularity().equals("multiple")) {
                    if (!checkCondition(entity, ((MultipleCondition)condition).getConditions(), ((MultipleCondition)condition).getLogical(), worldInstance)) {
                        return false;
                    }
                } else {
                    if (!((SingleCondition)condition).checkIfConditionIsTrue(entity, worldInstance)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public List<AbstractCondition> getConditions() {
        return conditions;
    }

    public String getLogical() {
        return logical;
    }
}
