package world.rule.action.expression;

import exceptions.FormatException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import world.entity.instance.EntityInstance;
import world.worldInstance.WorldInstance;

public interface Expression {
    Object decipher(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException, OperationNotCompatibleTypes, FormatException;
}
