# 运行方式

该项目使用gradle（Kotlin）进行构建

运行方式有多种： 
- 使用如下指令运行
```shell
./gradlew run
```
使用该方法时，可以在[build.gradle.kts](build.gradle.kts)中修改要爬取的地址。

- 直接运行Main.java（但是这需要你的IDE配置好gradle）

使用该方法时，可直接从控制台输入要爬取的地址。

# 测试结果

这里使用了：
- [科普物理，教出一票法神？](http://www.zzzcn.org/197_197480/)（选取原因：体积小，章节少，方便测试）
- [道诡异仙](https://www.ishuquge.la/txt/158004/index.html)（选取原因：体积大，且我喜欢看:) ）
- [庆余年](http://www.biquge66.net/book/697/) 选取原因：体积大，适合做并发测试，11.21MB用时2分55秒

结果见“测试结果”文件夹。

