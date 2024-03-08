package cn.noy.expr.element.op;

import cn.noy.expr.element.Associativity;
import cn.noy.expr.element.Priority;
import cn.noy.expr.element.val.Value;
import cn.noy.expr.structure.ExpressionNode;
import cn.noy.expr.structure.NodeTernaryOperator;
import cn.noy.expr.util.Pair;

/**
 * 三元运算符<br>
 * （因为懒所以没有使用正则表达式匹配awa）
 * @see Operator
 */
public class TernaryOperator extends Operator{

    public interface Op{
        Value apply(Value a, Value b, Value c);
    }
    private final Op operator;
    private final String firstSymbol;
    private final String secondSymbol;

    public TernaryOperator(String firstSymbol, String secondSymbol, Priority priority, Op operator) {
        super(firstSymbol+secondSymbol, priority);
        this.firstSymbol = firstSymbol;
        this.secondSymbol = secondSymbol;
        this.operator = operator;
    }

    @Override
    public Pair<Integer, String[]> split(String str) {
        int aIndex = getAssociativity()== Associativity.RIGHT_TO_LEFT ? str.indexOf(firstSymbol) : str.lastIndexOf(firstSymbol);
        int bIndex = getAssociativity()==Associativity.RIGHT_TO_LEFT ? str.indexOf(secondSymbol) : str.lastIndexOf(secondSymbol);
        if(aIndex < 0 || aIndex+firstSymbol.length() > bIndex || bIndex+secondSymbol.length() >= str.length())
            return null;
        return Pair.of(aIndex,
                new String[]{str.substring(0, aIndex),
                str.substring(aIndex+firstSymbol.length(), bIndex),
                str.substring(bIndex + secondSymbol.length())});
    }

    @Override
    public ExpressionNode createNode(ExpressionNode[] children) {
        NodeTernaryOperator node = new NodeTernaryOperator(this);
        node.setChild(0, children[0]);
        node.setChild(1, children[1]);
        node.setChild(2, children[2]);
        return node;
    }

    public Value apply(Value a, Value b, Value c){
        return operator.apply(a, b, c);
    }
}
