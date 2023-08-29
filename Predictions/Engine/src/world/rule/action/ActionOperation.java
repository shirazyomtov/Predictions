package world.rule.action;

import exceptions.*;
import world.entity.instance.EntityInstance;
import world.worldInstance.WorldInstance;

public interface ActionOperation {
    Action operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine;
}
