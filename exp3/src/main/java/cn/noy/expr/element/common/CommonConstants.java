package cn.noy.expr.element.common;

import cn.noy.expr.element.constant.Constant;
import cn.noy.expr.element.val.Value;

public class CommonConstants {
    public static Constant TRUE = new Constant("(true|True)", 1);
    public static Constant FALSE = new Constant("(false)|(False)", 0);
    public static Constant PI = new Constant("(pi)|(PI)", Math.PI);
    public static Constant E = new Constant("e|E", Math.E);
    public static Constant HEX = new Constant(s-> s.matches("0x[0-9a-fA-F]+")?
            Value.of(Long.parseLong(s.substring(2), 16)):null);
    public static Constant OCT = new Constant(s-> s.matches("0[0-7]+")?
            Value.of(Long.parseLong(s.substring(1), 8)):null);
    public static Constant BIN = new Constant(s-> s.matches("0b[01]+")?
            Value.of(Long.parseLong(s.substring(2), 2)):null);
    public static Constant[] CONSTANTS = new Constant[]{TRUE, FALSE, PI, E, HEX, OCT, BIN};
}
