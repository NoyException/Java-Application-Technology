package cn.noy.expr.element;

/**
 * 运算符结合性
 * 左结合：a op b op c = (a op b) op c
 * 右结合：a op b op c = a op (b op c)
 */
public enum Associativity {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT
}
