# 运行方式

该项目使用gradle（Kotlin）进行构建

运行方式有多种： 
- 在shell中使用如下指令运行
```shell
./gradlew run
```

- 直接运行Main.java（但是这需要你的IDE配置好gradle）

# 测试结果

测试输入：
```
1-3+1*2
```
输出：
```
Result = 0.0
Time used: 1.08ms
```
___
测试输入：
```
{(!(0>PI))?-[High(0x1F0010>>1+1)-3]!!:0}*100+3%2
```
输出：
```
Result = -799.0
Time used: 2.63ms
```
___
测试输入：
```
( - [ ( cos { 2 * PI / 3 } ) ] )
```
输出：
```
Result = 0.4999999999999998
Time used: 2.46ms
```
___
测试输入：
```
Low((1+1<<3)>>1<<2)
```
输出：
```
Result = 32.0
Time used: 1.85ms
```
---
测试输入：
```
toUpperCase(concat("to",concat("get","her")))
```
输出：
```
Result = TOGETHER
Time used: 2.38ms
```
---
测试输入：
```
function greaterThanTen(a) \
    $a > 10 \
endfunction \
greaterThanTen(11)
```
输出：
```
Result = 1.0
Time used: 1.76ms
```