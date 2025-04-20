package com.bookstore.model;

public class CartItem {
    private String ISBN;
    private int quantity;

    public CartItem(String ISBN, int quantity) {
        this.ISBN = ISBN;
        this.quantity = quantity;
    }
    public String getISBN() {
        return ISBN;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
