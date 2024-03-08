package cn.noy.expr.structure;

import cn.noy.expr.element.op.TernaryOperator;
import cn.noy.expr.element.val.Value;

/**
 * 三元运算符表达式节点
 * @see TernaryOperator
 */
public class NodeTernaryOperator extends ExpressionNode{
    private final TernaryOperator operator;

    public NodeTernaryOperator(TernaryOperator operator) {
        super(3);
        this.operator = operator;
    }

    @Override
    public Value getValue() {
        return operator.apply(getChild(0).getValue(), getChild(1).getValue(), getChild(2).getValue());
    }

}
