package model;

public class PR {
    private String id;
    private String productID;
    private String date;
    private String customerID;
    private String prStatus;
    private String quantity;
    private String pricePerEach;

    public PR(String id, String productID, String date, String customerID, String prStatus, int quantity, int pricePerEach) {
        this.id = String.format("%05d", Integer.parseInt(id));
        this.productID = String.format("%05d", Integer.parseInt(productID));
        this.date = date;
        this.customerID = String.format("%05d", Integer.parseInt(customerID));
        this.prStatus = prStatus;
        this.quantity = String.format("%,d", quantity);
        this.pricePerEach = String.format("%,d", quantity);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getPrStatus() {
        return prStatus;
    }

    public void setPrStatus(String prStatus) {
        this.prStatus = prStatus;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s", id, productID, customerID, date, prStatus);
    }
}
