package com.bookstore.model;

public class Customer {
    private int CustomerId;
    private String CustomerName;
    private String email;
    private String password;

    public Customer() {}

    public Customer(int CustomerId, String CustomerName, String email, String password) {
        this.CustomerId = CustomerId;
        this.CustomerName = CustomerName;
        this.email = email;
        this.password = password;
    }
    public int getCustomerId() {
        return CustomerId;
    }
    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }
    public String getCustomerName() {
        return CustomerName;
    }
    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
}
