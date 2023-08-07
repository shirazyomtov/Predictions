package world.rule.action.expression;

public class ExpressionIml implements Expression {
    private  String expressionName;

    public ExpressionIml(String expressionName) {
        this.expressionName = expressionName;
    }

    @Override
    public Object decipher() {
        return null;
    }
}
