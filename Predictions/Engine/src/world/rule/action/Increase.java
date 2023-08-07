package world.rule.action;

import world.enums.ActionType;
import world.rule.action.expression.ExpressionIml;

public class Increase extends Action{

    private String propertyName;
    private ExpressionIml expression;

    public Increase(String entityName, String propertyName, String expression) {
        super(entityName, ActionType.INCREASE);
        this.propertyName = propertyName;
        this.expression = new ExpressionIml(expression);
    }

    @Override
    public void operation() {

    }
}
