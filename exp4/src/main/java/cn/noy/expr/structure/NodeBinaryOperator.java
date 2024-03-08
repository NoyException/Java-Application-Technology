package cn.noy.expr.structure;

import cn.noy.expr.element.op.BinaryOperator;
import cn.noy.expr.element.val.Value;

/**
 * 二元运算符表达式节点
 * @see BinaryOperator
 */
public class NodeBinaryOperator extends ExpressionNode{
    private final BinaryOperator operator;

    public NodeBinaryOperator(BinaryOperator operator) {
        super(2);
        this.operator = operator;
    }

    @Override
    public Value getValue() {
        return operator.apply(getChild(0).getValue(), getChild(1).getValue());
    }

}
