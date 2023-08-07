package world.rule.action;

import world.enums.ActionType;
import world.rule.action.expression.ExpressionIml;

public class Decrease extends Action{

    private String propertyName;
    private ExpressionIml expression;

    public Decrease(String entityName, String propertyName, String expression) {
        super(entityName, ActionType.DECREASE);
        this.propertyName = propertyName;
        this.expression = new ExpressionIml(expression);
    }

    @Override
    public void operation() {

    }
}
