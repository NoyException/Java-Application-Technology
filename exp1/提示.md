# 提示

### 寻找源码

- 在IDEA中，按住`Ctrl`键，然后鼠标点击`String`、`StringBuilder`、`StringBuffer`，可以直接跳转到对应的源码中。
- 你也可以双击`Shift`键，然后输入`String`、`StringBuilder`、`StringBuffer`，也可以找到对应的源码。
- 你也可以在[OpenJDK](http://openjdk.java.net/)中找到对应的源码。

查看注释，可以理解大部分区别

### 进一步提示

- 与正常的类相比，`String`在很多地方与基础数据类型较为相似
- `String`已经拥有`+`操作符，为什么还要有`StringBuilder`和`StringBuffer`？
- 对比`StringBuilder`和`StringBuffer`两者源码，两者只有细微区别
- 为什么Java会同时保留`StringBuilder`和`StringBuffer`这两个类？

### 简化版答案（慎看）（仅供参考）（不要直接抄）

- `String`是不可变的，而`StringBuilder`和`StringBuffer`是可变的。
- `String`使用值传递，而`StringBuilder`和`StringBuffer`使用引用传递。
- `String`的`+`操作符，会产生大量的临时对象，而`StringBuilder`和`StringBuffer`则不会。
- `StringBuilder`和`StringBuffer`的API几乎完全一致，只有`StringBuffer`多了`synchronized`关键字。
- `StringBuffer`的`synchronized`关键字，会导致性能下降。
- `StringBuffer`的`synchronized`关键字，可以保证线程安全，所以`StringBuffer`是线程安全的，`StringBuilder`则不是。