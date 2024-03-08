# WebServer运行方式

该项目使用gradle（Kotlin）进行构建

### 直接运行

运行方式有多种： 
- 在shell中使用如下指令运行
```shell
./gradlew run
```
- 直接运行Main.java（但是这需要你的IDE配置好gradle）

### 使用jar包

可以使用如下命令打包
```shell
./gradlew jar
```
在jar包所在目录下运行以下指令开启服务器
```shell
java -jar WebServer.jar
```
也可以直接运行run.sh脚本

# 测试

开始运行后，通过浏览器访问 http://localhost:8080/test.zup 即可看到效果
访问 http://localhost:8080/test.htm 来查看静态页面