package model;

public class Product {
    private String id;
    private String name;
    private String pricePerEach;
    private String quantity;
    private String amount;

    public Product(String name, String pricePerEach, String quantity) {
        this.id = null;
        this.name = name;
        this.pricePerEach = String.format("%,d", Integer.parseInt(pricePerEach));
        this.quantity = String.format("%,d", Integer.parseInt(quantity));
        this.amount = String.format("%,d", Integer.parseInt(pricePerEach)*Integer.parseInt(quantity));
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

    public String getPricePerEach() {
        return pricePerEach;
    }

    public int getPricePerEachAsInt() {
        return getNumberFromNumberString(pricePerEach);
    }

    public void setPricePerEach(int pricePerEach) {
        this.pricePerEach = String.format("%,d", pricePerEach);
        this.amount = String.format("%,d", pricePerEach * getNumberFromNumberString(quantity));
    }

    public String getQuantity() {
        return quantity;
    }

    public int getQuantityAsInt() {
        return getNumberFromNumberString(quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = String.format("%,d", quantity);
        this.amount = String.format("%,d", getNumberFromNumberString(pricePerEach) * quantity);
    }

    public String getAmount() {
        return amount;
    }

    public int getAmountAsInt() {
        return getNumberFromNumberString(amount);
    }

    private int getNumberFromNumberString(String text) {
        String[] temp = text.split(",");
        String number = "";
        for (int i = 0 ; i < temp.length ; i++) {
            number += temp[i];
        }

        return Integer.parseInt(number);
    }

    @Override
    public String toString() {
        return String.format("%s %50s %30s %5s", id, name, pricePerEach, quantity);
    }
}
