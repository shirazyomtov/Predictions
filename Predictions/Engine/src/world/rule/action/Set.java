package world.rule.action;

import world.enums.ActionType;
import world.rule.action.expression.ExpressionIml;

public class Set extends Action {
    private String propertyName;
    private ExpressionIml expression;

    public Set(String entityName, String propertyName, String expression) {
        super(entityName, ActionType.SET);
        this.propertyName = propertyName;
        this.expression = new ExpressionIml(expression);
    }

    @Override
    public void operation() {

    }
}
