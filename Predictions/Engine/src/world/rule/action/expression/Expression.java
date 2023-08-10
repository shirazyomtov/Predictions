package world.rule.action.expression;

import exceptions.ObjectNotExist;

public interface Expression {
    Object decipher(String EntityNam) throws ObjectNotExist, NumberFormatException;
}
