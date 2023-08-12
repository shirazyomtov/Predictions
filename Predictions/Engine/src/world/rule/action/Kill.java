package world.rule.action;

import history.History;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.enums.Type;
import world.propertyInstance.api.Property;

public class Kill extends Action {
    public Kill(String entityName)
    {
        super(entityName, ActionType.KILL);
    }
    @Override
    public void operation(EntityInstance entity) {
        //need the list of entities
        History.getInstance().getSimulation().getWorldInstance().getEntityInstanceList().remove(entity.getCount());
    }
}
