# 从Hello World开始

1. 下载IntelliJ IDEA
2. 创建新项目（选择Java17或以上，选择Gradle）
3. 在工程的目录src/main/java下，新建Java类：com.example.Main，这样做会建立两个目录com和example，然后在里面建立文件Main.java
4. 里面会初始化出一个public class Main{}，展开
5. 在Main类里面写：
```Java
public static void main(String[] args){
	System.out.println("Hello world!");
}
```
这里面`static`的意思是，这是一个静态方法，与实例（也叫对象，但在Java里叫**实例**）无关，可以直接通过`Main.main(args)`调用。
在Java中，数据类型全部前置，所以数组声明方式为`T[] t;`。
`System`是一个工具类，`out`是里面的静态字段（也叫静态成员函数，成员函数在Java里叫**字段**），是一个输出流的实例，代表了系统输出流。`println()`是`OutputStream`的方法（也叫成员函数，在Java里叫**方法**），用于输出一行并换行。
6. 点击`main()`旁边的绿色三角（`Main`旁边也有，效果一样）即可运行。值得说明的是，后续的工程构建不会从这个地方运行。

# Java的称呼与格式规范

> 所有Java程序员都应该遵守该规范。Java的所有库都是这样写的。

称呼规范：
- 成员变量(member variable)->字段(field)
- 成员函数(member function)->方法(method)

格式规范
- 非静态字段：小驼峰（例：anExample）
- 静态字段：全大写+下划线（例：AN_EXAMPLE）
- 方法：小驼峰（例：anExample()）
- 类名（包括interface等）：大驼峰，主类名必须与文件名相同（例AnExample=AnExample.java）
- 包名：全小写，不加下划线（例：com.anexample）
- 工件Id（ArtifactId）：全小写，不加下划线，子模块用“-”表示（例：papermc，papermc-api）
- jar包名：大驼峰，可带版本（例：ExampleJar-1.0.0.jar）

# Java的OOP式编程

> Java是一门面向对象的语言，一切事物都被视为对象。

### 基础数据类型
> int、double、float、boolean……
需要注意的有：
- 基础数据类型外加一个String使用值传递
- 布尔值是boolean不是bool
- 小数默认double，想用float要写成2F或2.0F
- c中的long和int在java就是int，c中的long long在java才是long
  此外，Java有个很特殊的地方：所有基础类型都有一个类的包装（Wrapper）
  比如：
- int -> Integer
- char -> Character
- double -> Double
- ...
  这些封装后的类可以与基础数据类型之间直接进行赋值，比如
```Java
Integer i1 = 1;
int i2 = i1;
```
但不同的是，类是可以被赋予空值的
```Java
Integer i1 = null;
//但不能int i2 = null;（直接报错）或者int i2 = i1;（运行时报错）
```
Integer中有许多工具函数，比如`Integer.toString(int)`等。
Integer等wrapper与正常的类不同的是，他也是值传递。
> 值得一提的是，对于Integer之间的比较需要使用equals方法而非==，这在后面对所有类的实例进行比较时是相同的。只有基础数据类型本身使用==才是逻辑等于，而在实例中的含义为判断是否是同一个实例。

### 万物皆对象

所有的代码都需要写在类里。每一个Java文件的文件名都需要与其中的主类相同，每个文件只能有一个主类，修饰符为`public class`。我不推荐在public class外再写一个class（在主类里面写可以）。

普通运行时，类中的静态方法`public static void main(String[])`会被直接调用。

### 修饰符

> static

类中的static方法可以直接使用`类名.方法名()`调用，而非static方法只能使用`实例.方法名()`调用。

static字段只会在该类被第一次加载时创建一次，此后所有实例共享该字段；而非static字段每个实例各持有一个。static字段也可以直接通过类名访问。

static还可以用于标识静态代码块，在该类被首次加载时为静态字段初始化
```Java
public class A{
	private static Map<Integer, String> byId;
	static{
		//在静态代码块中初始化byId
		byId = new HashMap<>();
		byId.put(0, "System");
	}
}
```

> privilege

每个字段、方法等都需要加上权限修饰符，权限修饰符如下：
- public
- protected
- private
- (package-private)
  如果不加，就默认为package-private，在protected权限的基础下，同一个文件目录下的类都可以访问（但子目录不行）。

> final

final在修饰不同类型时效果不同：
- 修饰字段时，表示该字段一经初始化，不能指向其它实例
- 修饰方法时，表示子类不能override该方法
- 修饰类时，表示不允许有子类继承

*其它修饰符在前期用不到，暂不做介绍*

### 类与继承

类的类型分为以下几种：
- class
- interface
- enum
- record
- ...
  这里我们主要讲前两个。class可以被abstract修饰，修饰后的类即为抽象类，抽象类有以下几种特性：

- 只有抽象类才可以拥有抽象方法。抽象方法就是没有实现的方法，只有一个声明。
- 抽象类无法被直接实例化。
- 抽象类的非抽象子类必须实现所有的抽象方法。

不同于c++的是，Java中所有的方法都默认有c++中的virtual，而final方法就相当于c++中没有virtual的函数。

子类实例可以直接用抽象父类的变量保存，比如：
```Java
List<Integer> list = new LinkedList<>();
```
使用`instanceof`来判断一个实例是否是某个类及其子类的实例：
```Java
if(list instanceof ArrayList){
	System.out.println("这是个数组");
}
else if(list instanceof LinkedList l){
	System.out.println("这是个链表");
	//直接调用独属于LinkedList的方法
	int lastElement = l.getLast();
}
```
直接使用`(子类)父类变量名`进行类型转换，比如
```Java
LinkedList l = (LinkedList)list;
```
构造实例统一用new（除了基础数据类型和String），不需要手动销毁。

### 超级父类Object

在Java中，所有类都继承自Object类，所以所有类都会有Object中的方法，比如
- toString()
- hashCode()
- equals()
- ...
  你可以通过重载的方式去重写这些方法，这尤为重要。

### 内部类

在Java中，你可以在类中定义类，这样的类被称为“内部类”。比如：
```Java
public class A{
	public static class B{}
}
```
你可以使用`new A.B()`来创建一个B。

我们常规认识中的内部类都是**静态**内部类，还有一种非静态内部类，区别如下：
- 静态内部类可以视为一个独立的类，但它拥有外面那层类的所有字段和方法的权限，但它不能直接调用外层类的非静态字段或方法。创建方法为`A.B b = new A.B()`
- 非静态内部类同样拥有这些权限，但它可以直接调用外层类的非静态字段或方法，因为非静态内部类是从A的实例中创建的，也就是说，创建方法变成了`A.B b = new a.B()`，其中a是A的实例。（这也就意味着b可以直接操控a的字段和方法）

### 接口

下面是接口的特性：
- 类之间只能单继承，但接口可以多继承。
- 接口相当于只有抽象方法、静态方法和静态字段的类。
- 可以使用`default`关键字间接实现非抽象方法。
- 接口一般代表了某种特征，比如Serializable、Cloneable、RandomAccess等
- 接口也可以代表某些抽象概念，比如Collection<T>、List<T>等
- 在API中，一般都会使用Interface
- 在下一章AOP中，接口也可以用于表达某个函数。

### 简单设计模式

- 普通的类（类和实例的关系：人类与具体的人）
- 工具类（全为静态方法）
- 单例模式（类和实例的关系：选课系统概念与具体的选课系统）
- 工厂模式（工厂类与产品类的关系：工厂类相当于产品类的构造器）
- Builder模式（Builder类一般是某一主类的静态内部类，作用相当于主类的构造器）
- ...

更复杂的设计模式可以自行了解。

# Java的特殊语法

### this和super

this表示**当前实例**自身，super表示**父类化的实例**自身。
```Java
@Override
public void setA(A a){
	super.setA(a);//调用父类的setA(A);
	this.a = a;
}
```
这两者还有充当构造函数的作用
```Java
Student(int id, String name){
	super(id);//调用父类的构造函数Person(int);
	this.name = name;
}
Student(int id, String name, String englishName){
	this(id, name);//调用自身的另一个构造函数Student(int,String);
	this.englishName = englishName;
}
```
作为构造函数的this或super必须在本构造函数的第一行调用。

### 强化switch

Java支持c同款的switch，并在高版本中添加了强化switch，共有两个用法，请看样例：
```Java
public void fun(Color color){
	switch(color){
		case RED,ORANGE,YELLOW -> System.out.println("这是暖色调");
		case PURPLE -> {
			count++;
			System.out.println("这是你第"+count+"次输入紫色");
		}
		default -> System.out.println("我不认识这个颜色");
	}
	lastColor = color;
}

public boolean isLastColorWarm(){
	return switch(lastColor){
		case RED,ORANGE,YELLOW -> true;
		default -> {
			System.out.println("这不是暖色");
			return false;
		}
	};
}
```

### 匿名内部类

Java其实是可以直接实例化抽象类或者接口的，但实际上，Java只不过是创建了一个新的类（匿名的）继承了抽象类或接口，然后再创建这个类的实例罢了。比如：
```Java
Runnable task = new Runnable{
	@Override
	public void run(){
		//写些什么
	}
}
```
这里就是创建了一个实现了Runnable接口的一个匿名内部类，再为这个内部类创建了一个实例task。

其实匿名内部类是可以初始化的，直接使用一个大括号即可。
```Java
Runnable task = new Runnable{
	private long timestamp;
	//下面是构造函数
	{
		timestamp = System.currentTimeMillis();
	}
	@Override
	public void run(){
		System.out.println(timestamp);
	}
}
```

### 常用工具类
- System
- Math
- StringBuilder
- Collections
- Arrays

# 多线程

略

# Java的AOP式编程

### Lambda表达式

格式为：`(a, b, ...) -> {...; return x;}`
该格式有许多变体：
- 只有一个参数时，可简化为`a -> {...; return x;}`（没有参数时不能省略小括号）
- 函数体内只有一行时，可简化为`(a, b, ...) -> b.setA(a)`
- 函数体内只有一行，且该行为return时，可简化为`(a, b, ...) -> a`
- 如果某个静态方法刚好符合要求的参数类型和返回值类型，可以直接写成`ExampleClass::fun`
- 如果某个非静态方法刚好符合要求的参数类型和返回值类型，可以直接写成`exampleInstance::fun`

Lambda表达式一般使用以下类来储存：
- Runnable：()->void
- Consumer<T>：T->void
- Supplier<T>：()->T
- Predicate<T>：T->boolean
- Function<T,R>：T->R
- ...（更多请查看Java的functional包）

如果想要自定义类型，则需要定义一个只有一个方法的interface，比如
```Java
public interface Listener{
	boolean onTouch(User user, Position touchPosition, long duration);
}
```
```Java
...
Listener listener = (user,pos,duration)->{
	...
	return true;
}
...
boolean isCancelled = listener.onTouch(currentUser, p, d);
...
```

### Stream流

*写不动了，能看到这就差不多足够了，加油*

### 表示类的类：Class<T>

### 注解与反射