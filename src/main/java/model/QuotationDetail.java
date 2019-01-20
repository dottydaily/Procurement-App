package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuotationDetail {

    private String quotationId;
    private String purchaseRequestId;
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private String date;
    private String customerID;
    private String firstName;
    private String lastName;
    private int totalCost;
    private Customer customer;

    public QuotationDetail(int quotationId, int purchaseRequestId, String date
            , int customerID, String firstName, String lastName, int totalCost) {
        this.quotationId = String.format("%05d", quotationId);
        this.purchaseRequestId = String.format("%05d", purchaseRequestId);
        this.date = date;
        this.customerID = String.format("%05d", customerID);
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalCost = totalCost;

        DBConnecter database = DBConnecter.getInstance();

        try {
            ResultSet productResultSet = database.getResultSet(
                    "SELECT\n" +
                            "   product_list.product_id,\n" +
                            "   product_list.product_name,\n" +
                            "   product_list.price_per_each,\n" +
                            "   product_list.product_quantity\n" +
                            "FROM\n" +
                            "   quotation_list\n" +
                            "INNER JOIN product_list\n" +
                            "ON quotation_list.product_id = product_list.product_id\n" +
                            "AND quotation_list.quotation_id = " + quotationId);

            while (productResultSet.next()) {
                Product product = new Product(productResultSet.getString(2)
                        , productResultSet.getString(3)
                        , productResultSet.getString(4));
                product.setId(productResultSet.getString(1));

                products.add(product);
            }

            ResultSet customerResultSet = database.getResultSet(
                    "SELECT * From customer_list WHERE customer_list.customer_id = " + customerID);

            if (customerResultSet.next()) {
                String customerStatus = "Bad";
                if (customerResultSet.getBoolean(6)) {
                    customerStatus = "Good";
                }
                customer = new Customer(customerResultSet.getString(2)
                        , customerResultSet.getString(3), customerResultSet.getString(4)
                        , customerResultSet.getString(5), customerStatus
                        , customerResultSet.getString(7), customerResultSet.getInt(8));
                customer.setId(customerResultSet.getString(1));
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public String getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(String quotationId) {
        this.quotationId = quotationId;
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public String getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(String purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
