package world.rule.action;

import com.sun.org.apache.bcel.internal.generic.PUSH;
import exceptions.*;
import jaxb.schema.generated.PRDAction;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.rule.action.secondaryEntity.SecondaryEntity;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.util.WeakHashMap;

public abstract class Action implements ActionOperation, Serializable {

    private final String entityName;

    private final ActionType actionType;

    private SecondaryEntity secondaryEntity = null;
    protected Action(String entityName, ActionType actionType, PRDAction.PRDSecondaryEntity prdSecondaryEntity) {
        this.entityName = entityName;
        this.actionType = actionType;
        if (prdSecondaryEntity != null) {
            this.secondaryEntity = new SecondaryEntity(prdSecondaryEntity);
        }
    }

    public abstract Action operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine;

    public String getEntityName() {
        return entityName;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public SecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }

    public String getSecondaryEntityAsString(){
        if(secondaryEntity != null){
            return secondaryEntity.getSecondaryEntityName();
        }
        else{
            return "";
        }
    }

    public EntityInstance checkAndGetAppropriateInstance(EntityInstance entity, EntityInstance secondaryEntity) throws EntityNotDefine{
        if(entity.getName().equals(entityName)){
            return entity;
        }
        else if (secondaryEntity != null && secondaryEntity.getName().equals(entityName)) {
            return  secondaryEntity;
        }
        else{
            if (secondaryEntity !=null) {
                throw new EntityNotDefine(entityName, entity.getName(), secondaryEntity.getName());
            }
            else{
                throw new EntityNotDefine(entityName, entity.getName(), null);
            }
        }
    }
}
