package cn.noy.expr.structure;

import cn.noy.expr.element.fun.Function;
import cn.noy.expr.element.val.Value;

/**
 * 函数表达式节点
 * @see Function
 */
public class NodeFunction extends ExpressionNode{
    private final ExpressionParser parser;
    private final Function function;
    protected NodeFunction(ExpressionParser parser, Function function) {
        super(function.getArity());
        this.parser = parser;
        this.function = function;
    }

    @Override
    public Value getValue() {
        Value[] args = new Value[function.getArity()];
        for (int i = 0; i < args.length; i++) {
            args[i] = getChild(i).getValue();
        }
        return function.apply(parser, args);
    }

}
