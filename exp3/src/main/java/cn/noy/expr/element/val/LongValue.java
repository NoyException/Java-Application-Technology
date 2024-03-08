package cn.noy.expr.element.val;

public class LongValue extends Value{
    private final long value;

    LongValue(long value) {
        this.value = value;
    }

    @Override
    public Class<?> getType() {
        return Long.class;
    }

    @Override
    public String getStringValue() {
        return Long.toString(value);
    }

    @Override
    public Number getNumberValue() {
        return value;
    }
}
