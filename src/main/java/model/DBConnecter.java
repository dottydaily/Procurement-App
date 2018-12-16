package model;

import java.sql.*;

public class DBConnecter {

    private Connection connect;
    private Statement statement;
    private String databaseName;

    public DBConnecter() {
        databaseName = "procurement_app";
        try {
            connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/"+databaseName,"root","1234");
            statement = connect.createStatement();
            System.out.println("DATABASE CONNECTED!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet(String query) throws SQLException{
        return statement.executeQuery(query);
    }

    public void insertUserData(String firstName, String lastName, String username, String password,
                               String email, String phoneNumber) {

        String query = "INSERT INTO user_list VALUES (NULL, ";
        query = query + String.format("'%s', '%s', '%s', '%s', '%s', '%s')",
                username, password, firstName, lastName, email, phoneNumber);

        System.out.println(query);
        try {
            statement.executeUpdate(query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
}