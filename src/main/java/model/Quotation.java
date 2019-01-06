package model;

public class Quotation {
    private String quotationId;
    private String pr_id;
    private String product_id;
    private String date;
    private String customer_id;
    private int total_cost;

    public Quotation(String quotationId, String pr_id, String product_id, String date, String customer_id, int total_cost) {
        this.quotationId = String.format("%05d", Integer.parseInt(quotationId));
        this.pr_id = String.format("%05d", Integer.parseInt(pr_id));
        this.product_id = String.format("%05d", Integer.parseInt(product_id));
        this.date = date;
        this.customer_id = String.format("%05d", Integer.parseInt(customer_id));
        this.total_cost = total_cost;
    }

    public String getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(String quotationId) {
        this.quotationId = quotationId;
    }

    public String getPr_id() {
        return pr_id;
    }

    public void setPr_id(String pr_id) {
        this.pr_id = pr_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public int getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(int total_cost) {
        this.total_cost = total_cost;
    }

    @Override
    public String toString() {
        return "Quotation ID: " + quotationId;
    }
}
