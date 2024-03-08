package cn.noy.expr.element.op;

import cn.noy.expr.element.Associativity;
import cn.noy.expr.element.Priority;
import cn.noy.expr.structure.ExpressionNode;
import cn.noy.expr.util.Pair;

/**
 * 运算符
 */
public abstract class Operator implements Comparable<Operator>{
    private final String symbol;
    private final Priority priority;

    protected Operator(String symbol, Priority priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    /**
     * 获取运算符优先级
     * @return 优先级
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * 获取运算符（正则表达式）
     * @return 运算符
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * 获取运算符结合性
     * @return 结合性
     */
    public Associativity getAssociativity() {
        return priority.associativity();
    }

    /**
     * 该运算符分割字符串的方式
     * @param str 字符串
     * @return 运算符在字符串中的位置与分割后的字符串数组
     */
    public abstract Pair<Integer, String[]> split(String str);

    /**
     * 该运算符创建节点的方式
     * @param children 子节点
     * @return 节点
     */
    public abstract ExpressionNode createNode(ExpressionNode[] children);

    @Override
    public int compareTo(Operator o) {
        int i = this.priority.compareTo(o.priority);
        return i == 0 ? this.symbol.compareTo(o.symbol) : i;
    }
}
