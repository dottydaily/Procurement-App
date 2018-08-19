package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

public class test {
    public static void main(String[] args) {
        DBConnecter connecter = new DBConnecter();
        try {
            ResultSet resultSet = connecter.getResultSet("select * from user_list");

            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString("username"));
            }
            resultSet.beforeFirst();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
