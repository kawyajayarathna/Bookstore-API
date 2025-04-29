package com.bookstore.model;

public class CartItem {
    private String isbn;
    private int quantity;

    public CartItem() {}
    public CartItem(String isbn, int quantity) {
        this.isbn = isbn;
        this.quantity = quantity;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

}