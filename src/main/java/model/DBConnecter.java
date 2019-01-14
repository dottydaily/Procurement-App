package model;

import java.sql.*;

public class DBConnecter {

    static DBConnecter instance;
    private Connection connect;
    private PreparedStatement statement;
    private String databaseName;

    private DBConnecter() {
        databaseName = "procurement_app";
        try {
            connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/"+databaseName,"root","1234");
            System.out.println("DATABASE CONNECTED!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DBConnecter getInstance() {
        if (instance == null) {
            instance = new DBConnecter();
        }

        return instance;
    }

    public Boolean hasValueInTable(String tableName, String rowName, String checkString) {
        String query = String.format("SELECT %s FROM %s WHERE %s.%s = %s"
                , rowName, tableName, tableName, rowName, checkString);

        try {
            ResultSet resultSet = getResultSet(query);

            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return true;
    }

    public ResultSet getResultSet(String query) throws SQLException{
        statement = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        return statement.executeQuery();
    }

    public void insertUserData(String firstName, String lastName, String username, String password,
                               String email, String phoneNumber) {

        String query = "INSERT INTO user_list VALUES (NULL, ";
        query = query + String.format("'%s', '%s', '%s', '%s', '%s', '%s')",
                username, password, firstName, lastName, email, phoneNumber);

        doQuery(query);
    }

    public Customer insertCustomerData(String firstName, String lastName, String email, String address,
                               String status, String phoneNumber, String limit) {

        Customer customer = new Customer(firstName, lastName, email, address
                , status, phoneNumber, Integer.parseInt(limit));

        String query = "INSERT INTO customer_list VALUES (NULL, ";
        query = query + String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                firstName, lastName, email, address, status, phoneNumber, limit);

        System.out.println(query);
        try {
            statement = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                System.out.println("customer id = " + generatedKeys.getString(1));
                customer.setId(String.format("%05d", Integer.parseInt(generatedKeys.getString(1))));

                return customer;
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return null;
    }

    public Product insertProductData(String productName, String pricePerEach, String amount) {

        Product product = new Product(productName, pricePerEach, amount);

        String query = "INSERT INTO product_list VALUES (NULL, ";
        query = query + String.format("'%s', '%s', '%s')",
                productName, pricePerEach, amount);

        System.out.println(query);
        try {
            statement = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                System.out.println("product id = " + generatedKeys.getString(1));
                product.setId(String.format("%05d", Integer.parseInt(generatedKeys.getString(1))));

                return product;
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return null;
    }

    public void updateProductData(String product_id, int pricePerEach) {
        String query = String.format("UPDATE product_list SET price_per_each = %d WHERE product_id = %s"
                , pricePerEach, product_id);

        try {
            statement = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            System.out.println("Update failed: product_id = " + product_id);
        }
    }

    public void insertPR(String prID, String productID, String date, String customerID, String status) {
        String query = "INSERT INTO pr VALUES (NULL, ";
        query = query + String.format("'%s', '%s', '%s', '%s', '%s')",
                prID, productID, date, customerID, status);

        doQuery(query);
    }

    public void insertQuotation(String quotationID, String prID, String productID, String date
            , String customerID, String totalCost) {
        String query = "INSERT INTO quotation_list VALUES (NULL, ";
        query = query + String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                quotationID, prID, productID, date, customerID, totalCost, "Incomplete");

        doQuery(query);
    }

    public void updateQuotation(String prID, String productID, String totalCost) {
        String query = String.format("Update quotation_list SET total_cost = %s\n" +
                "WHERE quotation_list.pr_id = %s AND product_id = %s"
                , totalCost, prID, productID);

        doQuery(query);
    }

    public void insertPO(String prId, String quotationId, String sendDate) {
        String query = "INSERT INTO po VALUES (NULL, ";
        query = query + String.format("'%s', '%s', '%s', '%s')"
                , prId, quotationId, sendDate, "Incomplete");

        doQuery(query);
    }

    public void updatePoStatus(String poId, String prId, String quotationId, String status) {
        System.out.println(poId + status);
        String query = String.format("UPDATE po SET po_status = '%s' WHERE po_id = %s", status, poId);
        doQuery(query);
        query = String.format("UPDATE pr SET pr_status = '%s' WHERE pr_id = %s", status, prId);
        doQuery(query);
        query = String.format("UPDATE quotation_list SET quotation_status = '%s' WHERE quotation_id = %s"
                , status, quotationId);
        doQuery(query);
    }

    private void doQuery(String query) {
        System.out.println(query);
        try {
            statement = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            System.out.println("ERROR BY SQL COMMAND : " + query);
        }
    }
}