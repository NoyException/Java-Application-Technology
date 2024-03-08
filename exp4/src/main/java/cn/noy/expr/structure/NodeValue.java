package cn.noy.expr.structure;

import cn.noy.expr.element.val.Value;

public class NodeValue extends ExpressionNode{
    private final Value value;
    public NodeValue(Value value) {
        super(0);
        this.value = value;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return value.getStringValue();
    }

    @Override
    public double getDoubleValue() {
        return value.getNumberValue().doubleValue();
    }
}
