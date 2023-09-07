package world.rule.action.expression;

import exceptions.FormatException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import world.entity.instance.EntityInstance;
import world.worldInstance.WorldInstance;

public interface Expression {
    Object decipher(EntityInstance primaryEntity, WorldInstance worldInstance, EntityInstance secondEntity, String secondEntityName) throws ObjectNotExist, NumberFormatException, OperationNotCompatibleTypes, FormatException;
}
