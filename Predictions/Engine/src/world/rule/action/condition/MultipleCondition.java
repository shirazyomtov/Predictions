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
    private int countOr = 0;
    private int countAnd = 0;

    private int countTotalCondition = 0;

    public MultipleCondition(PRDThen prdThen, PRDElse prdElse, PRDCondition prdCondition, PRDAction.PRDSecondaryEntity prdSecondaryEntity, String entityName) {
        super(prdThen, prdElse, entityName, "multiple", prdSecondaryEntity);
        if(prdCondition.getPRDCondition() != null) {
            this.conditions = createListConditions(prdCondition.getPRDCondition());
        }
        this.logical = prdCondition.getLogical();
    }

    @Override
    public Action operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity, String secondEntityName) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        boolean flag;
        Action killOrReplace = null ;
        flag = checkCondition(entity, conditions, logical, worldInstance, secondaryEntity, secondEntityName);
        if (countOr + countAnd != countTotalCondition) {
            killOrReplace = performThenOrElse(flag, entity, worldInstance, secondaryEntity, secondEntityName);
            return killOrReplace;
        }
        else{
            return null;
        }
    }

    public boolean checkCondition(EntityInstance entity, List<AbstractCondition> conditions, String logical, WorldInstance worldInstance, EntityInstance secondaryEntity, String secondEntityName) throws ObjectNotExist, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        Boolean flag;
        if (logical.equals("or")) {
            for (AbstractCondition condition : conditions) {
                if (condition.getSingularity().equals("multiple")) {
                    if (checkCondition(entity, ((MultipleCondition)condition).getConditions(), ((MultipleCondition)condition).getLogical(), worldInstance, secondaryEntity, secondEntityName)) {
                        return true;
                    }
                } else {
                    flag = ((SingleCondition)condition).checkIfConditionIsTrue(entity, worldInstance, secondaryEntity, secondEntityName);
                    countTotalCondition++;
                    if(flag == null){
                        countOr++;
                    }
                    else {
                        if (flag) {
                            return true;
                        }
                    }
                }
            }
            if(countOr == conditions.size()){
                return true;
            }
            return false;
        }
        else if (logical.equals("and")) {
            for (AbstractCondition condition : conditions) {
                if (condition.getSingularity().equals("multiple")) {
                    if (!checkCondition(entity, ((MultipleCondition)condition).getConditions(), ((MultipleCondition)condition).getLogical(), worldInstance, secondaryEntity, secondEntityName)) {
                        return false;
                    }
                } else {
                    flag = ((SingleCondition)condition).checkIfConditionIsTrue(entity, worldInstance, secondaryEntity, secondEntityName);
                    countTotalCondition++;
                    if(flag == null){
                        countAnd++;
                    }
                    else {
                        if (!flag) {
                            return false;
                        }
                    }
                }
            }
            if(countAnd == conditions.size() ){
                return false;
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
