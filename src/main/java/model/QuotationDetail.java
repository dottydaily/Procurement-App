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

    public QuotationDetail(Quotation quotation, Customer customer) {
        DBConnecter database = DBConnecter.getInstance();

        quotationId = quotation.getQuotationId();
        purchaseRequestId = quotation.getPr_id();
        date = quotation.getDate();
        customerID = quotation.getCustomer_id();
        firstName = customer.getFirstName();
        lastName = customer.getLastName();
        totalCost = quotation.getTotal_cost();
        this.customer = customer;

        try {
            ResultSet quotationResultSet = database.getResultSet(
                    "SELECT\n" +
                            "   quotation_list.quotation_id,\n" +
                            "   quotation_list.product_id,\n" +
                            "   product_list.product_name,\n" +
                            "   product_list.price_per_each,\n" +
                            "   product_list.product_amount\n" +
                            "FROM\n" +
                            "   quotation_list\n" +
                            "INNER JOIN product_list\n" +
                            "ON quotation_list.product_id = product_list.product_id\n" +
                            "AND quotation_list.quotation_id = " + quotationId);

            while (quotationResultSet.next()) {
                System.out.println("-----------"+quotationResultSet.getString(2)+"----------");

                Product product = new Product(quotationResultSet.getString(3)
                        , quotationResultSet.getString(4)
                        , quotationResultSet.getString(5));
                product.setId(quotationResultSet.getString(2));

                products.add(product);
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

    @Override
    public String toString() {
        return "Quotation ID in Class QuotationDetail: " + quotationId;
    }
}
