package cn.noy.expr.element.fun;

import cn.noy.expr.element.val.Value;
import cn.noy.expr.structure.ExpressionParser;
import cn.noy.expr.structure.MapParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 函数<br>
 * 值得注意的是，括号被当成了函数名为空的有一个参数的函数
 */
public class Function {
    private final String name;
    private final int arity;
    private final BiFunction<ExpressionParser, Value[], Value> function;

    private Function(String name, int arity, BiFunction<ExpressionParser, Value[], Value> function) {
        this.name = name.toLowerCase();
        this.arity = arity;
        this.function = function;
    }

    private Function(String name, int arity, java.util.function.Function<Value[], Value> function) {
        this(name, arity, (parser, args) -> function.apply(args));
    }

    public static Function of(String name, int arity, java.util.function.Function<Value[], Value> function) {
        return new Function(name, arity, function);
    }

    public static Function string(String name, int arity, java.util.function.Function<String[], String> function) {
        return new Function(name, arity, (parser, args) -> {
            String[] sArgs = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                sArgs[i] = args[i].getStringValue();
            }
            return Value.of(function.apply(sArgs));
        });
    }

    public static Function numeric(String name, int arity, java.util.function.Function<double[], Double> function) {
        return new Function(name, arity, (parser, args) -> {
            double[] dArgs = new double[args.length];
            for (int i = 0; i < args.length; i++) {
                try {
                    dArgs[i] = args[i].getNumberValue().doubleValue();
                } catch (NumberFormatException e) {
                    return Value.ERROR;
                }
            }
            return Value.of(function.apply(dArgs));
        });
    }

    public static Function scripts(String name, String[] parameterNames, List<String> scripts){
        return new Function(name, parameterNames.length, (parser, args) -> {
            Map<String, Value> parameterMap = new HashMap<>();
            for (int i = 0; i < parameterNames.length; i++) {
                String argName = parameterNames[i].trim();
                if(argName.startsWith("'") || argName.startsWith("\""))
                    argName = argName.substring(1);
                if(argName.endsWith("'") || argName.endsWith("\""))
                    argName = argName.substring(0, argName.length()-1);
                parameterMap.put(argName, args[i]);
            }
            return parser.parse(scripts, new MapParameters(parameterMap)).getValue();
        });
    }

    /**
     * 获取函数名
     * @return 函数名
     */
    public String getName() {
        return name;
    }

    /**
     * 获取函数的参数个数
     * @return 参数个数
     */
    public int getArity() {
        return arity;
    }

    /**
     * 计算函数值
     * @param args 参数
     * @return 函数值
     */
    public Value apply(ExpressionParser parser, Value... args) {
        return function.apply(parser, args);
    }

    public record Position(int start, int leftBracket, int rightBracket, int[] comma) {}

    /**
     * 查找匹配该函数的第一个位置
     * @param str 字符串
     * @return 位置
     */
    public Position findFirst(String str) {
        str = str.toLowerCase();

        Pattern pattern = Pattern.compile(name+" *[(\\[{]");
        Matcher matcher = pattern.matcher(str);
        if(!matcher.find())
            return null;
        int start = matcher.start();
        int leftBracket = matcher.end()-1;

        int rightBracket = -1;
        int cnt = 1;
        int[] comma = arity>0 ? new int[arity-1] : null;
        int commaCnt = 0;
        for (int i = leftBracket+1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                cnt++;
            } else if (c == ')' || c == ']' || c == '}') {
                cnt--;
                if(cnt == 0){
                    rightBracket = i;
                    break;
                }
            }
            if(c==','){
                if(cnt == 1){
                    if(commaCnt >= arity-1 || arity<=0)
                        return null;
                    comma[commaCnt++] = i;
                }
            }
        }
        if(start<0 || rightBracket<0) return null;
        return new Position(start, leftBracket, rightBracket, comma);
    }
}
