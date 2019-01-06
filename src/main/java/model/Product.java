package model;

public class Product {
    private String id;
    private String name;
    private int pricePerEach;
    private int amount;

    public Product(String name, String pricePerEach, String amount) {
        this.id = null;
        this.name = name;
        this.pricePerEach = Integer.parseInt(pricePerEach);
        this.amount = Integer.parseInt(amount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = String.format("%05d", Integer.parseInt(id));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPricePerEach() {
        return pricePerEach;
    }

    public void setPricePerEach(int pricePerEach) {
        this.pricePerEach = pricePerEach;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", id, name, amount);
    }
}
