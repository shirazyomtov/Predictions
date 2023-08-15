package world.rule.action;

import history.History;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.worldInstance.WorldInstance;

import java.io.Serializable;

public class Kill extends Action implements Serializable {
    public Kill(String entityName)
    {
        super(entityName, ActionType.KILL);
    }
    @Override
    public boolean operation(EntityInstance entity, WorldInstance worldInstance) {
        EntityInstance entityToRemove = null;
        for (EntityInstance entityInstance: worldInstance.getEntityInstanceList()){
            if(entityInstance == entity){
                entityToRemove = entityInstance;
            }
        }
        if (entityToRemove != null) {
            worldInstance.getEntityInstanceList().remove(entityToRemove);
        }
        return false;
    }
}
