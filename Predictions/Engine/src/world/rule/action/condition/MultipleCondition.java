package world.rule.action.condition;

import exceptions.*;
import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDCondition;
import jaxb.schema.generated.PRDElse;
import jaxb.schema.generated.PRDThen;
import world.entity.instance.EntityInstance;
import world.rule.action.Action;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultipleCondition extends AbstractCondition implements Serializable {

    private List<AbstractCondition> conditions;
    private String logical;

    public MultipleCondition(PRDThen prdThen, PRDElse prdElse, PRDCondition prdCondition, PRDAction.PRDSecondaryEntity prdSecondaryEntity, String entityName) {
        super(prdThen, prdElse, entityName, "multiple", prdSecondaryEntity);
        if(prdCondition.getPRDCondition() != null) {
            this.conditions = createListConditions(prdCondition.getPRDCondition());
        }
        this.logical = prdCondition.getLogical();
    }

    @Override
    public Action operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        boolean flag;
        Action killOrReplace = null ;
        flag = checkCondition(entity, conditions, logical, worldInstance, secondaryEntity);
        killOrReplace = performThenOrElse(flag, entity, worldInstance, secondaryEntity);
        return killOrReplace;
    }

    public boolean checkCondition(EntityInstance entity, List<AbstractCondition> conditions, String logical, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        if (logical.equals("or")) {
            for (AbstractCondition condition : conditions) {
                if (condition.getSingularity().equals("multiple")) {
                    if (checkCondition(entity, ((MultipleCondition)condition).getConditions(), ((MultipleCondition)condition).getLogical(), worldInstance, secondaryEntity)) {
                        return true;
                    }
                } else {
                    if (((SingleCondition)condition).checkIfConditionIsTrue(entity, worldInstance, secondaryEntity)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else if (logical.equals("and")) {
            for (AbstractCondition condition : conditions) {
                if (condition.getSingularity().equals("multiple")) {
                    if (!checkCondition(entity, ((MultipleCondition)condition).getConditions(), ((MultipleCondition)condition).getLogical(), worldInstance, secondaryEntity)) {
                        return false;
                    }
                } else {
                    if (!((SingleCondition)condition).checkIfConditionIsTrue(entity, worldInstance, secondaryEntity)) {
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

    public List<AbstractCondition> createListConditions(List<PRDCondition> prdConditions) {
        List<AbstractCondition> conditionsList = new ArrayList<>();
        AbstractCondition selectedAction = null;
        for(PRDCondition prdCondition: prdConditions){
            if(prdCondition.getSingularity().equals("single")) {
                selectedAction = new SingleCondition(null, null, prdCondition, null);
            }
            else if(prdCondition.getSingularity().equals("multiple")){
                selectedAction = new MultipleCondition(null, null, prdCondition, null, getEntityName());
            }
            conditionsList.add(selectedAction);
        }

        return conditionsList;
    }
}
