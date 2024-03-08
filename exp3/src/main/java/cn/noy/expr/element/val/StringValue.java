package cn.noy.expr.element.val;

public class StringValue extends Value {
    private final String value;

    StringValue(String value) {
        this.value = value;
    }

    @Override
    public Class<?> getType() {
        return String.class;
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public Number getNumberValue() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            if(value.isEmpty())
                return 0;
            try{
                return Boolean.parseBoolean(value) ? 1 : 0;
            }catch (NumberFormatException e2){
                return Double.NaN;
            }
        }
    }

}
