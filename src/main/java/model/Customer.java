package model;

public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String status;
    private String phoneNumber;
    private String limit;

    public Customer(String firstName, String lastName, String email, String address, String status, String phoneNumber, int limit) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.status = status;
        this.phoneNumber = String.format("%010d", Integer.parseInt(phoneNumber));
        this.limit = String.format("%,d", limit);
    }

    public void setId(String id) {
        this.id = String.format("%05d", Integer.parseInt(id));;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = String.format("%010d", Integer.parseInt(phoneNumber));;
    }

    public void setLimit(int limit) {
        this.limit = String.format("%,d", limit);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLimit() {
        return limit;
    }

    public int getLimitAsInt() {
        return getNumberFromNumberString(limit);
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
        return id+" "+firstName+" "+lastName;
    }
}
