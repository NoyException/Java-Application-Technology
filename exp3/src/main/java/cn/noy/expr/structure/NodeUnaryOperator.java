package cn.noy.expr.structure;

import cn.noy.expr.element.op.UnaryOperator;
import cn.noy.expr.element.val.Value;

/**
 * 一元运算符表达式节点
 * @see UnaryOperator
 */
public class NodeUnaryOperator extends ExpressionNode{
    private final UnaryOperator operator;

    public NodeUnaryOperator(UnaryOperator operator) {
        super(1);
        this.operator = operator;
    }

    @Override
    public Value getValue() {
        return operator.apply(getChild(0).getValue());
    }

}
