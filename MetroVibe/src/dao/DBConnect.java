package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

private static final String URL= "jdbc:mysql://www.papademas.net:3307/510labs?autoReconnect=true&useSSL=false"; 
private static final String USERNAME= "db510";
private static final String PASSWORD= "510";

public static Connection getconnection() throws SQLException{
return DriverManager.getConnection(URL, USERNAME, PASSWORD);
}
}

