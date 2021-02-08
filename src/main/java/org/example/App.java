package org.example;


import org.example.data.ConnectionFactory;

import java.sql.SQLException;

public class App
{
    public static void main( String[] args ) throws SQLException {
        ConnectionFactory.getConnection();
    }
}
