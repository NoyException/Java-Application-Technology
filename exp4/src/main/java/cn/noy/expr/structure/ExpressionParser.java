package cn.noy.expr.structure;

import cn.noy.expr.element.Associativity;
import cn.noy.expr.element.common.CommonConstants;
import cn.noy.expr.element.common.CommonFunctions;
import cn.noy.expr.element.common.CommonOperators;
import cn.noy.expr.element.constant.Constant;
import cn.noy.expr.element.fun.Function;
import cn.noy.expr.element.op.Operator;
import cn.noy.expr.element.val.Value;
import cn.noy.expr.util.Pair;
import com.google.common.collect.TreeMultimap;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表达式解析器<br>
 * 用于解析表达式字符串为表达式树<br>
 * 需要使用{@link Builder}构建<br>
 */
public class ExpressionParser {
    private List<Function> functions;
    private TreeMultimap<Double, Operator> operators;
    private List<Constant> constants;

    private ExpressionParser() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ExpressionParser createDefault() {
        return builder().registerCommon().build();
    }

    public ExpressionNode parse(List<String> script){
        return parse(script, new MapParameters());
    }

    public ExpressionNode parse(List<String> script, Parameters parameters) {

        script = script.stream()
                .flatMap(s -> Arrays.stream(s.split(";")))
                .filter(s -> !s.matches(" *#.*"))
                .filter(s -> !s.trim().isEmpty())
                .toList();

        ExpressionNode ret = null;

        LinkedList<Boolean> ifStack = new LinkedList<>();
        ifStack.addLast(true);

        String functionName = null;
        String[] functionArgs = null;
        LinkedList<String> functionScript = null;

        for (String line : script) {

            if(line.matches(" *function .*")){
                String def = line.substring(line.indexOf("function")+8).trim();
                functionName = def.substring(0, def.indexOf('(')).trim();
                functionArgs = def.substring(def.indexOf('(')+1, def.indexOf(')')).split(",");
                functionScript = new LinkedList<>();
                continue;
            }

            if(line.matches(" *endfunction *")){
                if(functionArgs == null)
                    throw new IllegalArgumentException("在没有定义函数的情况下使用了endfunction");
                functions.add(Function.scripts(functionName, functionArgs, functionScript));
                functionName = null;
                functionArgs = null;
                functionScript = null;
                continue;
            }

            if(functionName != null){
                functionScript.add(line);
                continue;
            }

            line = replaceStrings(line, parameters);

            if(line.matches(" *if .*")){
                if(!ifStack.getLast())
                    ifStack.addLast(false);
                String condition = line.substring(line.indexOf("if")+2).trim();
                double value = parseReplacingFunctions(condition, parameters).getDoubleValue();
                boolean b = value != 0 && !Double.isNaN(value);
                ifStack.addLast(b);
                continue;
            }

            if(line.matches(" *else *")){
                ifStack.addLast(!ifStack.removeLast());
                continue;
            }

            if(line.matches(" *endif *")){
                ifStack.removeLast();
                continue;
            }

            if(!ifStack.getLast())
                continue;

            if(line.matches(" *return .*")){
                String expr = line.substring(line.indexOf("return")+6).trim();
                ret = parseReplacingFunctions(expr, parameters);
                break;
            }

            int index = line.indexOf('=');
            if(index < 0 || line.charAt(index+1) == '='){
                ret = parseReplacingFunctions(line, parameters);
                continue;
            }
            String name = line.substring(0, index).trim();
            String expr = line.substring(index+1).trim();
            ExpressionNode value = parseReplacingFunctions(expr, parameters);
            parameters.set(name, value);
        }

        if(ret != null) return ret;
        return new NodeValue(Value.of("[no return value]"));
    }

    /**
     * 解析表达式
     * @param str 表达式字符串
     * @return 表达式树的根节点
     */
    public ExpressionNode parse(String str) {
        return parse(str, new MapParameters());
    }

    /**
     * 解析表达式<br>
     * str中变量名以$开头，如$var1<br>
     * variables中的变量名不需要以$开头<br>
     * @param str 表达式字符串
     * @param parameters 参数
     * @return 表达式树的根节点
     */
    public ExpressionNode parse(String str, Parameters parameters) {
        str = replaceStrings(str, parameters);
        return parseReplacingFunctions(str, parameters);
    }

    private String replaceStrings(String str, Parameters parameters) {
        Pattern pattern = Pattern.compile("(?<!\\\\)[\"'].*?(?<!\\\\)[\"']");
        Matcher matcher = pattern.matcher(str);
        StringBuilder sb = new StringBuilder();
        while(matcher.find()){
            String s = matcher.group().replaceAll("\\\\[\"']", "'");
            s = s.substring(1, s.length()-1);
            int code = s.hashCode();
            matcher.appendReplacement(sb, "\\$"+code);
            parameters.set(Integer.toString(code), Value.of(s));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private ExpressionNode parseReplacingFunctions(String str, Parameters parameters) {
        str = str.trim();

        Function fun = null;
        Function.Position first = null;
        for (Function function : functions) {
            Function.Position tmp = function.findFirst(str);
            if (tmp != null && (first == null || tmp.start() < first.start())) {
                fun = function;
                first = tmp;
            }
        }
        if (fun == null)
            return parseOperatorsAndConstants(str, parameters);

        ExpressionNode[] children = new ExpressionNode[fun.getArity()];
        if (first.comma() != null) {
            if (first.comma().length > 0) {
                children[0] = parseReplacingFunctions(str.substring(first.leftBracket() + 1, first.comma()[0]), parameters);
                for (int i = 1; i < fun.getArity() - 1; i++) {
                    children[i] = parseReplacingFunctions(str.substring(first.comma()[i - 1] + 1, first.comma()[i]), parameters);
                }
                children[fun.getArity() - 1] = parseReplacingFunctions(str.substring(first.comma()[fun.getArity() - 2] + 1, first.rightBracket()), parameters);
            } else children[0] = parseReplacingFunctions(str.substring(first.leftBracket() + 1, first.rightBracket()), parameters);
        }

        NodeFunction nodeFunction = new NodeFunction(this, fun);
        for (int i = 0; i < fun.getArity(); i++) {
            nodeFunction.setChild(i, children[i]);
        }

        int code = nodeFunction.hashCode();
        String id = Integer.toString(code);

        parameters.set(id, nodeFunction);
        return parseReplacingFunctions(str.substring(0, first.start()) + "$" + id + str.substring(first.rightBracket() + 1), parameters);
    }

    private ExpressionNode parseOperatorsAndConstants(String str, Parameters variables) {
        str = str.trim();
        Operator op = null;
        int index = -1;
        String[] ss = null;
        for (Operator operator : operators.values()) {
            Pair<Integer, String[]> split = operator.split(str);
            if (split == null) {
                continue;
            }
            if(op == null){
                op = operator;
                index = split.first();
                ss = split.second();
            }
            else {
                if(operator.getPriority().priority()>op.getPriority().priority()) break;
                if(operator.getAssociativity() == Associativity.LEFT_TO_RIGHT){
                    if(split.first()>index){
                        op = operator;
                        index = split.first();
                        ss = split.second();
                    }
                }
                else{
                    if(split.first()<index){
                        op = operator;
                        index = split.first();
                        ss = split.second();
                    }
                }
            }
        }
        if(op!=null){
            ExpressionNode[] children = new ExpressionNode[ss.length];
            for (int i = 0; i < ss.length; i++) {
                children[i] = parseOperatorsAndConstants(ss[i], variables);
            }
            return op.createNode(children);
        }
        if (str.startsWith("$")) {
            String s = str.substring(1);
            if (variables.contains(s)) return variables.get(s);
            return new NodeValue(Value.of("[unknown parameter: " + s+"]"));
        }
        if (str.isEmpty()) return new NodeValue(Value.of(""));
        for (Constant constant : constants) {
            Value parse = constant.parse(str);
            if (parse != null) return new NodeValue(parse);
        }

        try{
            return new NodeValue(Value.of(Long.parseLong(str)));
        } catch (NumberFormatException e1){
            try {
                return new NodeValue(Value.of(Double.parseDouble(str)));
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("无法解析: " + str);
            }
        }
    }

    /**
     * 表达式解析器构建器
     */
    public static class Builder {
        private final List<Function> functions = new LinkedList<>();
        private final TreeMultimap<Double, Operator> operators =
                TreeMultimap.create(Comparator.naturalOrder(), Operator::compareTo);
        private final List<Constant> constants = new LinkedList<>();

        private Builder() {
        }

        /**
         * 构建表达式解析器
         * @return 表达式解析器
         */
        public ExpressionParser build() {
            ExpressionParser parser = new ExpressionParser();
            parser.functions = new ArrayList<>(functions);
            parser.operators = TreeMultimap.create(operators);
            parser.constants = new ArrayList<>(constants);
            return parser;
        }

        /**
         * 注册函数
         * @param function 函数
         * @return this
         */
        public Builder registerFunction(Function function) {
            functions.add(function);
            return this;
        }

        /**
         * 注册函数
         * @param functions 函数
         * @return this
         */
        public Builder registerFunction(Function... functions) {
            for (Function function : functions) {
                registerFunction(function);
            }
            return this;
        }

        /**
         * 注册运算符
         * @param operator 运算符
         * @return this
         */
        public Builder registerOperator(Operator operator) {
            operators.put(operator.getPriority().priority(), operator);
            return this;
        }

        /**
         * 注册运算符
         * @param operators 运算符
         * @return this
         */
        public Builder registerOperator(Operator... operators) {
            for (Operator operator : operators) {
                registerOperator(operator);
            }
            return this;
        }

        /**
         * 注册常量
         * @param constant 常量
         * @return this
         */
        public Builder registerConstant(Constant constant) {
            constants.add(constant);
            return this;
        }

        /**
         * 注册常量
         * @param constants 常量
         * @return this
         */
        public Builder registerConstant(Constant... constants) {
            for (Constant constant : constants) {
                registerConstant(constant);
            }
            return this;
        }

        /**
         * 注册常用函数、运算符、常量
         * @return this
         */
        public Builder registerCommon() {
            for (Function function : CommonFunctions.NUMERIC_FUNCTIONS) {
                registerFunction(function);
            }
            for (Function function : CommonFunctions.STRING_FUNCTIONS) {
                registerFunction(function);
            }
            for (Operator operator : CommonOperators.OPERATORS) {
                registerOperator(operator);
            }
            for (Constant constant : CommonConstants.CONSTANTS) {
                registerConstant(constant);
            }
            return this;
        }

    }
}
