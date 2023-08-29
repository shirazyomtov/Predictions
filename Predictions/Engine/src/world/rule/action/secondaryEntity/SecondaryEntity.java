package world.rule.action.secondaryEntity;

import jaxb.schema.generated.PRDAction;
import world.rule.action.condition.AbstractCondition;

import java.util.List;

public class SecondaryEntity {
    private final String secondaryEntityName;
    private final String count;

    private AbstractCondition condition; // todo

    public SecondaryEntity(PRDAction.PRDSecondaryEntity prdSecondaryEntity) {
        this.secondaryEntityName = prdSecondaryEntity.getEntity();
        this.count = prdSecondaryEntity.getPRDSelection().getCount();
        if(prdSecondaryEntity.getPRDSelection().getPRDCondition() != null){
            this.condition = AbstractCondition.createCondition(prdSecondaryEntity.getPRDSelection().getPRDCondition(), secondaryEntityName);
        }
    }

    public String getSecondaryEntityName() {
        return secondaryEntityName;
    }

    public String getCount() {
        return count;
    }

    public AbstractCondition getCondition() {
        return condition;
    }
}
