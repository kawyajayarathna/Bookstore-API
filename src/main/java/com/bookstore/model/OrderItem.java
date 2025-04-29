package com.bookstore.model;

public class OrderItem {
    private String isbn;
    private int quantity;
    private double price;

    public OrderItem() {}
    public OrderItem(String isbn, int quantity, double price) {
        this.isbn = isbn;
        this.quantity = quantity;
        this.price = price;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

}
