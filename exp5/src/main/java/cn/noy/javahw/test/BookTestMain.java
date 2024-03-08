package cn.noy.javahw.test;

import cn.noy.javahw.db.DBConnector;

public class BookTestMain {
    public static void main(String[] args){
        DBConnector connector = new DBConnector("localhost","3306","library","root","123456");
        connector.connect();
        Book book = new Book();
        book.setBookId(102);
        book.setPublishYear(2003);
        book.setPrice(1D);
        book.setStock(3);
        book.setTitle("title");
        book.setAuthor("author");
        book.setCategory("category");
        book.setPress("press");
        book.insert(connector.getConnection());

        System.out.println("original book: "+book);

        book = Book.select(connector.getConnection(), 102);
        if (book != null) {
            System.out.println("select from database: "+book);
            book.setAuthor("new author");
            book.update(connector.getConnection());
            book = Book.select(connector.getConnection(), 102);
            System.out.println("after update: "+book);
            if (book != null) {
                book.delete(connector.getConnection());
                book = Book.select(connector.getConnection(), 102);
                System.out.println("after delete: "+book);
            }
        }

    }
}
