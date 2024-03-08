package cn.noy.javahw;


import cn.noy.expr.structure.ExpressionParser;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //创建表达式解析器
        ExpressionParser parser = ExpressionParser.createDefault();
        List<String> script = new LinkedList<>();
        System.out.println("Please input an expression :");
        Scanner scanner = new Scanner(System.in);
        while(true){
            String line = scanner.nextLine();
            if(line.endsWith("\\")){
                script.add(line.substring(0, line.length()-1));
            }
            else{
                script.add(line);
                break;
            }
        }
        //计算100次用时，取平均值
        long start = System.currentTimeMillis();
        for (int i = 0; i < 99; i++) {
            parser.parse(script);
        }
        String value = parser.parse(script).getStringValue();
        long end = System.currentTimeMillis();
        //输出结果
        System.out.println("Result = " + value);
        System.out.println("Time used: "+(end-start)/100D+"ms");
    }
}