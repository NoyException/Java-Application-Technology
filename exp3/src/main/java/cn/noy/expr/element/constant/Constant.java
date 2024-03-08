package cn.noy.expr.element.constant;

import cn.noy.expr.element.val.Value;

import java.util.function.Function;

/**
 * 常量
 */
public class Constant {
    private final Function<String, Value> parser;

    public Constant(Function<String, Value> parser) {
        this.parser = parser;
    }

    public Constant(String symbol, Value value) {
        this(str -> str.matches(symbol) ? value : null);
    }

    public Constant(String symbol, double value){
        this(symbol, Value.of(value));
    }

    /**
     * 解析字符串
     * @param str 字符串
     * @return 常量值
     */
    public Value parse(String str) {
        return parser.apply(str);
    }
}
