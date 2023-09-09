package world.rule.action;

import exceptions.EntityNotDefine;
import jaxb.schema.generated.PRDAction;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.worldInstance.WorldInstance;

import java.io.Serializable;

public class Kill extends Action implements Serializable {
    public Kill(String entityName, PRDAction.PRDSecondaryEntity prdSecondaryEntity)
    {
        super(entityName, ActionType.KILL, prdSecondaryEntity);
    }
    @Override
    public Action operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity, String secondEntityName) throws EntityNotDefine {
        EntityInstance entityPrimary = checkAndGetAppropriateInstance(entity, secondaryEntity, secondEntityName);
        if(entityPrimary != null) {
            EntityInstance entityToRemove = null;
            for (EntityInstance entityInstance : worldInstance.getEntityInstanceList()) {
                if (entityInstance == entityPrimary) {
                    entityToRemove = entityInstance;
                }
            }
            if (entityToRemove != null) {
                worldInstance.getTwoDimensionalGrid().setTwoD_arr(entityToRemove.getLocation().getRow(), entityToRemove.getLocation().getCol(), false);
                worldInstance.getEntityInstanceList().remove(entityToRemove);
                worldInstance.setCurrentAmountOfEntitiesAfterKill(entityToRemove);
            }
            return null;
        }
        else {
            return null;
        }
    }
}
