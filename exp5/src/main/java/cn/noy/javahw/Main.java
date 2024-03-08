package cn.noy.javahw;

import cn.noy.javahw.db.DBConnector;
import cn.noy.javahw.db.Schema;
import cn.noy.javahw.db.SchemaExtractor;
import cn.noy.javahw.util.CaseConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please input the host of MySQL database:");
        String host = scanner.nextLine();
        if(host.isEmpty()) host = "localhost";
        System.out.println("please input the port of MySQL database:");
        String port = scanner.nextLine();
        if(port.isEmpty()) port = "3306";
        System.out.println("please input the database name:");
        String databaseName = scanner.nextLine();
        if(databaseName.isEmpty()) databaseName = "library";
        System.out.println("please input the username:");
        String username = scanner.nextLine();
        if (username.isEmpty()) username = "root";
        System.out.println("please input the password:");
        String password = scanner.nextLine();
        if(password.isEmpty()) password = "123456";

        DBConnector connector = new DBConnector(host, port, databaseName, username, password);
        connector.connect();
        SchemaExtractor extractor = new SchemaExtractor(connector);
        try {
            System.out.println("please input the table name:");
            String tableName = scanner.nextLine();
            if(tableName.isEmpty()) tableName = "book";
            Schema schema = extractor.extractSchema(tableName);
            File file = new File("exp5/src/main/java/cn/noy/javahw/test/"+ CaseConverter.toCamelCase(tableName, true)+".java");
            File parentDir = file.getParentFile();
            if(!parentDir.exists()){
                if(!parentDir.mkdirs()){
                    throw new RuntimeException("failed to create directory");
                }
            }
            if(file.exists()){
                if(!file.delete()){
                    throw new RuntimeException("failed to delete old file");
                }
            }
            if(file.createNewFile()){
                try (FileOutputStream fos = new FileOutputStream(file)){
                    fos.write(schema.generateJavaClass("cn.noy.javahw.test").getBytes(StandardCharsets.UTF_8));
                    fos.flush();
                    System.out.println("file generated: " + file.getAbsolutePath());
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}