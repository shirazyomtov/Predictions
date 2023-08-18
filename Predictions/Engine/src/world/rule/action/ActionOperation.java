package world.rule.action;

import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import exceptions.OperationNotSupportedType;
import world.entity.instance.EntityInstance;
import world.worldInstance.WorldInstance;

public interface ActionOperation {
    boolean operation(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, OperationNotSupportedType, OperationNotCompatibleTypes;
}
