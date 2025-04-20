package com.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int customerid;
    private List<CartItem> items = new ArrayList<>();

    public Cart(){
        this.items = new ArrayList<>();
    }
    public Cart(int customerId){
        this.customerid = customerId;
        this.items = new ArrayList<>();
    }
    public int getCustomerid() {
        return customerid;
    }
    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }
    public List<CartItem> getItems() {
        return items;
    }
    public void setItems(List<CartItem> items) {
        this.items = items;
    }
    public void addItem(CartItem item){
        this.items.add(item);
    }
    public void removeItem(String ISBN){
        this.items.removeIf(i -> i.getISBN().equals(ISBN));
    }
    public void updateItem(String ISBN, int quantity){
        for(CartItem item : items){
            if(item.getISBN().equals(ISBN)){
                item.setQuantity(quantity);
                return;
            }
        }
    }
}
