package com.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int customerId;
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}
    public Cart(int customerId) {
        this.customerId = customerId;
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public boolean removeItem(String isbn) {
        return items.removeIf(i -> i.getIsbn().equals(isbn));
    }

    public void updateItem(String isbn, int quantity) {
        items.stream()
                .filter(i -> i.getIsbn().equals(isbn))
                .findFirst()
                .ifPresent(i -> i.setQuantity(quantity));
    }
    public void updateItem(CartItem item) {
        updateItem(item.getIsbn(), item.getQuantity());
    }


    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

}
