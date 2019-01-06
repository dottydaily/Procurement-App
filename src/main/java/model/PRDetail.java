package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PRDetail {

    private String purchaseRequestId;
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private String date;
    private String customerID;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String status;
    private String phoneNumber;
    private int limit;

    public PRDetail(PR pr, Customer customer) {
        DBConnecter database = DBConnecter.getInstance();

        purchaseRequestId = pr.getId();
        date = pr.getDate();
        customerID = customer.getId();
        firstName = customer.getFirstName();
        lastName = customer.getLastName();
        email = customer.getEmail();
        address = customer.getAddress();
        status = customer.getStatus();
        phoneNumber = customer.getPhoneNumber();
        limit = customer.getLimit();

        // get all product of this pr
        try {
            ResultSet pRResultSet = database.getResultSet(
                    "SELECT\n" +
                            "   pr.pr_id,\n" +
                            "   pr.product_id,\n" +
                            "   product_list.product_name,\n" +
                            "   product_list.price_per_each,\n" +
                            "   product_list.product_amount\n" +
                            "FROM pr\n" +
                            "INNER JOIN product_list\n" +
                            "ON pr.product_id = product_list.product_id AND pr.pr_id = "+ purchaseRequestId);

            while (pRResultSet.next()) {
                Product product = new Product(pRResultSet.getString(3)
                                            , pRResultSet.getString(4)
                                            , pRResultSet.getString(5));
                product.setId(pRResultSet.getString(2));

                products.add(product);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public PRDetail(String purchaseRequestId, String date, String customerID, String firstName, String lastName
            , String email, String address, String status, String phoneNumber, int limit) {
        this(new PR(purchaseRequestId, "", date, customerID),
                new Customer(firstName, lastName, email, address, status, phoneNumber, limit));
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return String.format("prID: %s, customerID: %s, %s, %s, %s, %s, %s, %s", purchaseRequestId, customerID, firstName, lastName, email, address, phoneNumber, status);
    }

    public ObservableList<Product> getProducts() {
        return products;
    }
}
