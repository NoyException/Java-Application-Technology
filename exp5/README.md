### 测试方式

运行方式有多种： 
- 在shell中使用如下指令运行
```shell
./gradlew run
```
- 直接运行Main.java（但是这需要你的IDE配置好gradle）

# 测试

- 首先，启动MySQL服务器
- 然后，在数据库中预先创建好一个表
- 接着，运行Main，根据指引输入测试参数，选定之前创建的表
- 在exp5/src/main/java/cn/noy/javahw/test/下可以找到生成的java文件

# 测试结果

这里对表book进行测试，测试结果如下：
建表语句：
```sql
create table book
(
    book_id      int auto_increment
        primary key,
    category     varchar(63)                not null,
    title        varchar(63)                not null,
    press        varchar(63)                not null,
    publish_year int                        not null,
    author       varchar(63)                not null,
    price        decimal(7, 2) default 0.00 not null,
    stock        int           default 0    not null,
    constraint category
        unique (category, press, author, title, publish_year)
);
```
生成的java文件：[Book.java](src%2Fmain%2Fjava%2Fcn%2Fnoy%2Fjavahw%2Ftest%2FBook.java)
编写的测试文件：[BookTestMain.java](src%2Fmain%2Fjava%2Fcn%2Fnoy%2Fjavahw%2Ftest%2FBookTestMain.java)
测试输出：
```
original book: Book(publishYear=2003, author=author, price=1.0, bookId=102, category=category, title=title, press=press, stock=3)
select from database: Book(publishYear=2003, author=author, price=1.0, bookId=102, category=category, title=title, press=press, stock=3)
after update: Book(publishYear=2003, author=new author, price=1.0, bookId=102, category=category, title=title, press=press, stock=3)
after delete: null
```
测试成功