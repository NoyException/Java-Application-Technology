package cn.noy.javahw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

public class DBConnector {
    private Connection connection;
    private String host;
    private String port;
    private String databaseName;
    private String username;
    private String password;

    public DBConnector(String host, String port, String databaseName, String username, String password) {
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public void connect(){
        String url = String.format("jdbc:mysql://%s:%s/%s", host, port, databaseName);
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
