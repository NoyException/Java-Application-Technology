package cn.noy.expr.element.val;

public abstract class Value {
    public static final Value ERROR = new Value() {
        @Override
        public Class<?> getType() {
            return null;
        }

        @Override
        public String getStringValue() {
            return "[ERROR]";
        }

        @Override
        public Number getNumberValue() {
            return Double.NaN;
        }
    };

    public static Value of(boolean value) {
        return new BooleanValue(value);
    }

    public static Value of(int value) {
        return new IntegerValue(value);
    }

    public static Value of(long value) {
        return new LongValue(value);
    }

    public static Value of(double value){
        return new DoubleValue(value);
    }

    public static Value of(String value){
        return new StringValue(value);
    }

    public abstract Class<?> getType();

    public abstract String getStringValue();

    public abstract Number getNumberValue();

}
