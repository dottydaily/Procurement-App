package controller;

import java.sql.*;

public class DBConnecter {

    private Connection connect;
    private Statement statement;

    public DBConnecter() {
        try {
            connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mysql","root","1234");
            statement = connect.createStatement();
            System.out.println("DATABASE CONNECTED!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet(String query) throws SQLException{
        return statement.executeQuery(query);
    }
}