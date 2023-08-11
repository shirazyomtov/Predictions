package world.rule.action.expression;

import exceptions.ObjectNotExist;
import world.entity.instance.EntityInstance;

public interface Expression {
    Object decipher(EntityInstance entity) throws ObjectNotExist, NumberFormatException;
}
