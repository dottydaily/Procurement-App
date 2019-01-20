package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PODetail {
    private String poId;
    private String quotationId;
    private String prId;
    private String firstName;
    private String lastName;
    private String sendDate;
    private double totalCost;
    private String status;
    private Customer customer;
    private DBConnecter database = DBConnecter.getInstance();
    private ResultSet customerResultSet;

    public PODetail(String poId, String prId, String quotationId, String firstName, String lastName
            , String sendDate, double totalCost, String status) {
        this.poId = String.format("%05d", Integer.parseInt(poId));
        this.prId = String.format("%05d", Integer.parseInt(prId));
        this.quotationId = String.format("%05d", Integer.parseInt(quotationId));
        this.firstName = firstName;
        this.lastName = lastName;
        this.sendDate = sendDate;
        this.totalCost = totalCost;
        this.status = status;

        queryCustomer();
    }

    public void queryCustomer() {
        try {
            customerResultSet = database.getResultSet(
                    "SELECT * FROM customer_list\n" +
                            "INNER JOIN pr ON pr.customer_id = customer_list.customer_id\n" +
                            "AND pr.pr_id = " + this.prId + "\n" +
                            "GROUP BY customer_list.customer_id");

            if (customerResultSet.next()) {
                String customerStatus = "Bad";
                if (customerResultSet.getBoolean(6)) {
                    customerStatus = "Good";
                }
                System.out.println(customerResultSet.getString(7));
                System.out.println(customerResultSet.getString(6));
                this.customer = new Customer(customerResultSet.getString(2), customerResultSet.getString(3)
                        , customerResultSet.getString(4), customerResultSet.getString(5)
                        , customerStatus, customerResultSet.getString(7)
                        , customerResultSet.getInt(8));
                this.customer.setId(String.format("%05d", Integer.parseInt(customerResultSet.getString(1))));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(String quotationId) {
        this.quotationId = quotationId;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
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

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
