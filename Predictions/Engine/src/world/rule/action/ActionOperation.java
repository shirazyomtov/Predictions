package world.rule.action;

import exceptions.ObjectNotExist;
import exceptions.OperationNotSupportedType;
import world.entity.instance.EntityInstance;

public interface ActionOperation {
    void operation(EntityInstance entity) throws ObjectNotExist, OperationNotSupportedType;
}
