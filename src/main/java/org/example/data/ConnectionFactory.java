package org.example.data;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties properties = new Properties();
        FileReader reader = null;
        try{
            reader = new FileReader("src/main/resources/credentials.properties");
            properties.load(reader);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        URL = properties.getProperty("url");
        USERNAME = properties.getProperty("user");
        PASSWORD = properties.getProperty("password");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME,PASSWORD);
    }

}
