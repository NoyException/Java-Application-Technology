package cn.noy.expr.element.val;

public class DoubleValue extends Value{
    private final double value;

    DoubleValue(double value) {
        this.value = value;
    }

    @Override
    public Class<?> getType() {
        return Double.class;
    }

    @Override
    public String getStringValue() {
        return Double.toString(value);
    }

    @Override
    public Number getNumberValue() {
        return value;
    }
}
