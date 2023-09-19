package world.rule.action;

import exceptions.*;
import world.entity.instance.EntityInstance;
import world.worldInstance.WorldInstance;

import java.util.List;

public interface ActionOperation {
    List<Action> operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity, String secondEntityName) throws ObjectNotExist, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine;
}
