package cn.noy.javahw.test;

import lombok.Data;
import java.sql.*;

/**
 * Auto generated from table `book`
 * @author Noy(3210105713)
 */
@Data
public class Book {
	private Integer publishYear;
	private String author;
	private Double price;
	private Integer bookId;
	private String category;
	private String title;
	private String press;
	private Integer stock;

	public void insert(Connection connection){
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO `book`(`publish_year`, `author`, `price`, `book_id`, `category`, `title`, `press`, `stock`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			statement.setInt(1, publishYear);
			statement.setString(2, author);
			statement.setDouble(3, price);
			statement.setInt(4, bookId);
			statement.setString(5, category);
			statement.setString(6, title);
			statement.setString(7, press);
			statement.setInt(8, stock);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(Connection connection){
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE `book` SET `publish_year` = ?, `author` = ?, `price` = ?, `book_id` = ?, `category` = ?, `title` = ?, `press` = ?, `stock` = ? WHERE `book_id` = ?");
			statement.setInt(1, publishYear);
			statement.setString(2, author);
			statement.setDouble(3, price);
			statement.setInt(4, bookId);
			statement.setString(5, category);
			statement.setString(6, title);
			statement.setString(7, press);
			statement.setInt(8, stock);
			statement.setInt(9, bookId);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void delete(Connection connection){
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM `book` WHERE `book_id` = ?");
			statement.setInt(1, bookId);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static Book select(Connection connection, Integer bookId){
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM `book` WHERE `book_id` = ?");
			statement.setInt(1, bookId);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				Book book = new Book();
				book.publishYear = resultSet.getInt("publish_year");
				book.author = resultSet.getString("author");
				book.price = resultSet.getDouble("price");
				book.bookId = resultSet.getInt("book_id");
				book.category = resultSet.getString("category");
				book.title = resultSet.getString("title");
				book.press = resultSet.getString("press");
				book.stock = resultSet.getInt("stock");
				return book;
			}
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}