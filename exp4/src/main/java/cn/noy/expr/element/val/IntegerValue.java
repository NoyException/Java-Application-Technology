package cn.noy.expr.element.val;

public class IntegerValue extends Value{
    private final int value;

    IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public Class<?> getType() {
        return Integer.class;
    }

    @Override
    public String getStringValue() {
        return Integer.toString(value);
    }

    @Override
    public Number getNumberValue() {
        return value;
    }
}
