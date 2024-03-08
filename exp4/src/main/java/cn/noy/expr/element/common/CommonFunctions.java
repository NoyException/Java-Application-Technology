package cn.noy.expr.element.common;

import cn.noy.expr.element.fun.Function;
import cn.noy.expr.element.val.Value;

public class CommonFunctions {
    public static final Function BRACKET = Function.string("", 1, args -> args[0]);
    public static final Function HIGH = Function.of("high", 1, args -> Value.of(args[0].getNumberValue().longValue()>>16));
    public static final Function LOW = Function.of("low", 1, args -> Value.of(args[0].getNumberValue().longValue()&0xffff));
    public static final Function SIN = Function.numeric("sin", 1, args -> Math.sin(args[0]));
    public static final Function COS = Function.numeric("cos", 1, args -> Math.cos(args[0]));
    public static final Function TAN = Function.numeric("tan", 1, args -> Math.tan(args[0]));
    public static final Function COT = Function.numeric("cot", 1, args -> 1 / Math.tan(args[0]));
    public static final Function SEC = Function.numeric("sec", 1, args -> 1 / Math.cos(args[0]));
    public static final Function CSC = Function.numeric("csc", 1, args -> 1 / Math.sin(args[0]));
    public static final Function LN = Function.numeric("ln", 1, args -> Math.log(args[0]));
    public static final Function LG = Function.numeric("lg", 1, args -> Math.log10(args[0]));
    public static final Function LOG = Function.numeric("log", 2, args -> Math.log(args[0]) / Math.log(args[1]));
    public static final Function SQRT = Function.numeric("sqrt", 1, args -> Math.sqrt(args[0]));
    public static final Function CBRT = Function.numeric("cbrt", 1, args -> Math.cbrt(args[0]));
    public static final Function ABS = Function.numeric("abs", 1, args -> Math.abs(args[0]));
    public static final Function FLOOR = Function.numeric("floor", 1, args -> Math.floor(args[0]));
    public static final Function CEIL = Function.numeric("ceil", 1, args -> Math.ceil(args[0]));
    public static final Function ROUND = Function.of("round", 1, args -> Value.of(Math.round(args[0].getNumberValue().doubleValue())));
    public static final Function MAX = Function.numeric("max", 2, args -> Math.max(args[0], args[1]));
    public static final Function MAX_3 = Function.numeric("max", 3, args -> Math.max(Math.max(args[0], args[1]), args[2]));
    public static final Function MAX_4 = Function.numeric("max", 4, args -> Math.max(Math.max(Math.max(args[0], args[1]), args[2]), args[3]));
    public static final Function MIN = Function.numeric("min", 2, args -> Math.min(args[0], args[1]));
    public static final Function MIN_3 = Function.numeric("min", 3, args -> Math.min(Math.min(args[0], args[1]), args[2]));
    public static final Function MIN_4 = Function.numeric("min", 4, args -> Math.min(Math.min(Math.min(args[0], args[1]), args[2]), args[3]));
    public static final Function POW = Function.numeric("pow", 2, args -> Math.pow(args[0], args[1]));
    public static final Function EXP = Function.numeric("exp", 1, args -> Math.exp(args[0]));
    public static final Function SIGNUM = Function.numeric("signum", 1, args -> Math.signum(args[0]));
    public static final Function RANDOM_0 = Function.numeric("rand", 0, args -> Math.random());
    public static final Function RANDOM_1 = Function.numeric("rand", 1, args -> Math.random() * args[0]);
    public static final Function RANDOM_2 = Function.numeric("rand", 2, args -> Math.random() * (args[1] - args[0]) + args[0]);

    public static final Function LENGTH = Function.of("length", 1, args -> Value.of(args[0].getStringValue().length()));
    public static final Function SUBSTRING_1 = Function.of("substring", 2, args -> Value.of(args[0].getStringValue().substring(args[1].getNumberValue().intValue())));
    public static final Function SUBSTRING_2 = Function.of("substring", 3, args -> Value.of(args[0].getStringValue().substring(args[1].getNumberValue().intValue(), args[2].getNumberValue().intValue())));
    public static final Function INDEX_OF_1 = Function.of("indexOf", 2, args -> Value.of(args[0].getStringValue().indexOf(args[1].getStringValue())));
    public static final Function INDEX_OF_2 = Function.of("indexOf", 3, args -> Value.of(args[0].getStringValue().indexOf(args[1].getStringValue(), args[2].getNumberValue().intValue())));
    public static final Function LAST_INDEX_OF_1 = Function.of("lastIndexOf", 2, args -> Value.of(args[0].getStringValue().lastIndexOf(args[1].getStringValue())));
    public static final Function LAST_INDEX_OF_2 = Function.of("lastIndexOf", 3, args -> Value.of(args[0].getStringValue().lastIndexOf(args[1].getStringValue(), args[2].getNumberValue().intValue())));
    public static final Function STARTS_WITH = Function.of("startsWith", 2, args -> Value.of(args[0].getStringValue().startsWith(args[1].getStringValue())));
    public static final Function ENDS_WITH = Function.of("endsWith", 2, args -> Value.of(args[0].getStringValue().endsWith(args[1].getStringValue())));
    public static final Function CONTAINS = Function.of("contains", 2, args -> Value.of(args[0].getStringValue().contains(args[1].getStringValue())));
    public static final Function REPLACE = Function.string("replace", 3, args -> args[0].replace(args[1], args[2]));
    public static final Function REPLACE_FIRST = Function.string("replaceFirst", 4, args -> args[0].replaceFirst(args[1], args[2]));
    public static final Function CONCAT = Function.string("concat", 2, args -> args[0].concat(args[1]));
    public static final Function TO_LOWER_CASE = Function.string("toLowerCase", 1, args -> args[0].toLowerCase());
    public static final Function TO_UPPER_CASE = Function.string("toUpperCase", 1, args -> args[0].toUpperCase());
    public static final Function EQUALS = Function.of("equals", 2, args -> Value.of(args[0].getStringValue().equals(args[1].getStringValue())));
    public static final Function MATCHES = Function.of("matches", 2, args -> Value.of(args[0].getStringValue().matches(args[1].getStringValue())));

    public static final Function[] NUMERIC_FUNCTIONS = new Function[]{
        BRACKET, HIGH, LOW, SIN, COS, TAN, COT, SEC, CSC, LN, LG, LOG, SQRT, CBRT, ABS, FLOOR, CEIL, ROUND, MAX, MAX_3, MAX_4, MIN, MIN_3, MIN_4, POW, EXP, SIGNUM, RANDOM_0, RANDOM_1, RANDOM_2
    };

    public static final Function[] STRING_FUNCTIONS = new Function[]{
        LENGTH, SUBSTRING_1, SUBSTRING_2, INDEX_OF_1, INDEX_OF_2, LAST_INDEX_OF_1, LAST_INDEX_OF_2, STARTS_WITH, ENDS_WITH, CONTAINS, REPLACE, REPLACE_FIRST, CONCAT, TO_LOWER_CASE, TO_UPPER_CASE, EQUALS, MATCHES
    };
}
