package cn.noy.expr.element.common;

import cn.noy.expr.element.Associativity;
import cn.noy.expr.element.Priority;

public class CommonPriorities {
    public static final Priority TERNARY_CONDITIONAL = new Priority(-10, Associativity.RIGHT_TO_LEFT);
    public static final Priority LOGICAL_OR = new Priority(-6, Associativity.LEFT_TO_RIGHT);
    public static final Priority LOGICAL_AND = new Priority(-5, Associativity.LEFT_TO_RIGHT);
    public static final Priority BITWISE_OR = new Priority(-4, Associativity.LEFT_TO_RIGHT);
    public static final Priority BITWISE_XOR = new Priority(-3, Associativity.LEFT_TO_RIGHT);
    public static final Priority BITWISE_AND = new Priority(-2, Associativity.LEFT_TO_RIGHT);
    public static final Priority COMPARE = new Priority(-1, Associativity.LEFT_TO_RIGHT);
    public static final Priority SHIFT = new Priority(0, Associativity.LEFT_TO_RIGHT);
    public static final Priority ADD_SUB = new Priority(1, Associativity.LEFT_TO_RIGHT);
    public static final Priority MUL_DIV_MOD = new Priority(2, Associativity.LEFT_TO_RIGHT);
    public static final Priority UNARY = new Priority(3, Associativity.RIGHT_TO_LEFT);
    public static final Priority UNARY_LEFT_TO_RIGHT = new Priority(4, Associativity.LEFT_TO_RIGHT);
}
