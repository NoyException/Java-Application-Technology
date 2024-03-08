package cn.noy.expr.element.common;

import cn.noy.expr.element.op.BinaryOperator;
import cn.noy.expr.element.op.Operator;
import cn.noy.expr.element.op.TernaryOperator;
import cn.noy.expr.element.op.UnaryOperator;
import cn.noy.expr.element.val.Value;

import java.util.regex.Pattern;

public class CommonOperators {
    public static final Operator ADD = new BinaryOperator(Pattern.quote("+"), CommonPriorities.ADD_SUB, (a, b) -> {
        if(a.getType()==String.class || b.getType()==String.class)
            return Value.of(a.getStringValue() + b.getStringValue());
        return Value.of(a.getNumberValue().doubleValue() + b.getNumberValue().doubleValue());
    });

    public static final Operator SUB = new BinaryOperator("(?<=.)-", CommonPriorities.ADD_SUB, (a, b) -> Value.of(a.getNumberValue().doubleValue() - b.getNumberValue().doubleValue()));
    public static final Operator MUL = new BinaryOperator(Pattern.quote("*"), CommonPriorities.MUL_DIV_MOD, (a, b) -> Value.of(a.getNumberValue().doubleValue() * b.getNumberValue().doubleValue()));
    public static final Operator DIV = new BinaryOperator(Pattern.quote("/"), CommonPriorities.MUL_DIV_MOD, (a, b) -> Value.of(a.getNumberValue().doubleValue() / b.getNumberValue().doubleValue()));
    public static final Operator MOD = new BinaryOperator(Pattern.quote("%"), CommonPriorities.MUL_DIV_MOD, (a, b) -> Value.of(a.getNumberValue().doubleValue() % b.getNumberValue().doubleValue()));
    public static final Operator LEFT_SHIFT = new BinaryOperator("<<", CommonPriorities.SHIFT, (a, b) -> {
        if(a.getType() == String.class)
            return Value.of(a.getStringValue().substring(b.getNumberValue().intValue()));
        return Value.of(a.getNumberValue().longValue() << b.getNumberValue().longValue());
    });
    public static final Operator RIGHT_SHIFT = new BinaryOperator(">>", CommonPriorities.SHIFT, (a, b) -> {
        if(a.getType() == String.class)
            return Value.of(a.getStringValue().substring(0, a.getStringValue().length() - b.getNumberValue().intValue()));
        return Value.of(a.getNumberValue().longValue() >> b.getNumberValue().longValue());
    });
    public static final Operator TERNARY_CONDITIONAL = new TernaryOperator("?", ":", CommonPriorities.TERNARY_CONDITIONAL, (a, b, c) -> a.getNumberValue().doubleValue()!=0 ? b : c);
    public static final Operator GREATER = new BinaryOperator("(?<![>])>(?![>=])", CommonPriorities.COMPARE, (a, b) -> {
        if(a.getType()==String.class || b.getType()==String.class)
            return Value.of(a.getStringValue().compareTo(b.getStringValue()) > 0);
        return Value.of(a.getNumberValue().doubleValue() > b.getNumberValue().doubleValue());
    });
    public static final Operator LESS = new BinaryOperator("(?<![<])<(?![<=])", CommonPriorities.COMPARE, (a, b) -> {
        if(a.getType()==String.class || b.getType()==String.class)
            return Value.of(a.getStringValue().compareTo(b.getStringValue()) < 0);
        return Value.of(a.getNumberValue().doubleValue() < b.getNumberValue().doubleValue());
    });
    public static final Operator GREATER_EQUAL = new BinaryOperator(">=", CommonPriorities.COMPARE, (a, b) -> {
        if(a.getType()==String.class || b.getType()==String.class)
            return Value.of(a.getStringValue().compareTo(b.getStringValue()) >= 0);
        return Value.of(a.getNumberValue().doubleValue() >= b.getNumberValue().doubleValue());
    });
    public static final Operator LESS_EQUAL = new BinaryOperator("<=", CommonPriorities.COMPARE, (a, b) -> {
        if(a.getType()==String.class || b.getType()==String.class)
            return Value.of(a.getStringValue().compareTo(b.getStringValue()) <= 0);
        return Value.of(a.getNumberValue().doubleValue() <= b.getNumberValue().doubleValue());
    });
    public static final Operator EQUAL = new BinaryOperator("==", CommonPriorities.COMPARE, (a, b) -> {
        if(a.getType()==String.class || b.getType()==String.class)
            return Value.of(a.getStringValue().equals(b.getStringValue()));
        return Value.of(a.getNumberValue().doubleValue() == b.getNumberValue().doubleValue());
    });
    public static final Operator NOT_EQUAL = new BinaryOperator(Pattern.quote("!="), CommonPriorities.COMPARE, (a, b) -> {
        if(a.getType()==String.class || b.getType()==String.class)
            return Value.of(!a.getStringValue().equals(b.getStringValue()));
        return Value.of(a.getNumberValue().doubleValue() != b.getNumberValue().doubleValue());
    });
    public static final Operator BITWISE_AND = new BinaryOperator("(?<![&])&(?![&])", CommonPriorities.BITWISE_AND, (a, b) -> Value.of(a.getNumberValue().longValue() & b.getNumberValue().longValue()));
    public static final Operator BITWISE_XOR = new BinaryOperator(Pattern.quote("^"), CommonPriorities.BITWISE_XOR, (a, b) -> Value.of(a.getNumberValue().longValue() ^ b.getNumberValue().longValue()));
    public static final Operator BITWISE_OR = new BinaryOperator("(?<![|])\\|(?![|])", CommonPriorities.BITWISE_OR, (a, b) -> Value.of(a.getNumberValue().longValue() | b.getNumberValue().longValue()));
    public static final Operator AND = new BinaryOperator(Pattern.quote("&&"), CommonPriorities.LOGICAL_AND, (a, b) -> Value.of(a.getNumberValue().doubleValue()!=0 && b.getNumberValue().doubleValue()!=0));
    public static final Operator OR = new BinaryOperator(Pattern.quote("||"), CommonPriorities.LOGICAL_OR, (a, b) -> Value.of(a.getNumberValue().doubleValue()!=0 || b.getNumberValue().doubleValue()!=0));
    public static final Operator NOT = new UnaryOperator(Pattern.quote("!"), CommonPriorities.UNARY, a -> Value.of(a.getNumberValue().doubleValue()==0));
    public static final Operator NEG = new UnaryOperator("(?<!.)-", CommonPriorities.UNARY, a -> Value.of(-a.getNumberValue().doubleValue()));
    public static final Operator FACTORIAL = new UnaryOperator("(?<![!])\\!(?![!])", CommonPriorities.UNARY_LEFT_TO_RIGHT, a -> {
        long n = a.getNumberValue().longValue();
        if (n < 0) throw new IllegalArgumentException("阶乘的数字必须大于等于0");
        long result = 1;
        for (long i = 2; i <= n; i++) {
            result *= i;
        }
        return Value.of(result);
    });
    public static final Operator DOUBLE_FACTORIAL = new UnaryOperator(Pattern.quote("!!"), CommonPriorities.UNARY_LEFT_TO_RIGHT, a -> {
        long n = a.getNumberValue().longValue();
        if (n < 0) throw new IllegalArgumentException("阶乘的数字必须大于等于0");
        long result = 1;
        for (long i = n % 2 == 0 ? 2 : 1; i <= n; i += 2) {
            result *= i;
        }
        return Value.of(result);
    });

    public static final Operator[] OPERATORS = new Operator[]{
            ADD, SUB, MUL, DIV, MOD, LEFT_SHIFT, RIGHT_SHIFT, TERNARY_CONDITIONAL, GREATER, LESS, GREATER_EQUAL, LESS_EQUAL, EQUAL, NOT_EQUAL, BITWISE_AND, BITWISE_XOR, BITWISE_OR, AND, OR, NOT, NEG, FACTORIAL, DOUBLE_FACTORIAL
    };
}
