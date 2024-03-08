package cn.noy.expr.element.op;

import cn.noy.expr.element.Associativity;
import cn.noy.expr.element.Priority;
import cn.noy.expr.element.val.Value;
import cn.noy.expr.structure.ExpressionNode;
import cn.noy.expr.structure.NodeUnaryOperator;
import cn.noy.expr.util.Pair;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一元运算符
 * @see Operator
 */
public class UnaryOperator extends Operator{
    private final Function<Value, Value> operator;
    public UnaryOperator(String symbol, Priority priority, Function<Value, Value> operator) {
        super(symbol, priority);
        this.operator = operator;
    }

    @Override
    public Pair<Integer, String[]> split(String str) {
        String symbol = getAssociativity() == Associativity.RIGHT_TO_LEFT ? "^" + getSymbol() : getSymbol() + "$";
        Pattern pattern = Pattern.compile(symbol);
        Matcher matcher = pattern.matcher(str);

        if(!matcher.find())
            return null;
        int start = matcher.start();
        int end = matcher.end();

        if(start < 0 || end > str.length())
            return null;

        if(getAssociativity() == Associativity.RIGHT_TO_LEFT){
            return Pair.of(start, new String[]{str.substring(end)});
        }
        else{
            return Pair.of(start, new String[]{str.substring(0, start)});
        }
    }

    @Override
    public ExpressionNode createNode(ExpressionNode[] children) {
        NodeUnaryOperator node = new NodeUnaryOperator(this);
        node.setChild(0, children[0]);
        return node;
    }

    public Value apply(Value a){
        return operator.apply(a);
    }
}
