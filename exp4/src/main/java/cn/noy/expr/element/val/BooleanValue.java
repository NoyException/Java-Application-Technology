package cn.noy.expr.element.val;

public class BooleanValue extends Value{
    private final boolean value;

    BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public Class<?> getType() {
        return Boolean.class;
    }

    @Override
    public String getStringValue() {
        return Boolean.toString(value);
    }

    @Override
    public Number getNumberValue() {
        return value ? 1 : 0;
    }
}
