package cn.noy.expr.structure;

import cn.noy.expr.element.val.Value;

public interface Parameters {
    ExpressionNode get(String name);
    void set(String name, Value value);
    void set(String name, ExpressionNode value);
    boolean contains(String name);
}
