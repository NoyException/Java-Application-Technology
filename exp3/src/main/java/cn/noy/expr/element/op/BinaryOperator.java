package cn.noy.expr.element.op;

import cn.noy.expr.element.Associativity;
import cn.noy.expr.element.Priority;
import cn.noy.expr.element.val.Value;
import cn.noy.expr.structure.ExpressionNode;
import cn.noy.expr.structure.NodeBinaryOperator;
import cn.noy.expr.util.Pair;

import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 二元运算符
 * @see Operator
 */
public class BinaryOperator extends Operator{

    private final BiFunction<Value, Value, Value> operator;

    public BinaryOperator(String symbol, Priority priority, BiFunction<Value, Value, Value> operator) {
        super(symbol, priority);
        this.operator = operator;
    }

    @Override
    public Pair<Integer, String[]> split(String str) {
        String symbol = getSymbol();
        Pattern pattern = Pattern.compile(symbol);
        Matcher matcher = pattern.matcher(str);

        int start = -1;
        int end = -1;
        if(getAssociativity() == Associativity.LEFT_TO_RIGHT){
            while(matcher.find()){
                start = matcher.start();
                end = matcher.end();
            }
        }
        else{
            if(matcher.find()){
                start = matcher.start();
                end = matcher.end();
            }
        }
        if(start < 0 || end > str.length())
            return null;
        return Pair.of(start, new String[]{str.substring(0, start), str.substring(end)});
    }

    @Override
    public ExpressionNode createNode(ExpressionNode[] children) {
        NodeBinaryOperator node = new NodeBinaryOperator(this);
        node.setChild(0, children[0]);
        node.setChild(1, children[1]);
        return node;
    }

    public Value apply(Value a, Value b){
        return operator.apply(a, b);
    }
}
