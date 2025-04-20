package com.bookstore.model;

public class Book {
    private String title;
    private int authorId;
    private String ISBN;
    private int publicationYear;
    private double price;
    private int stock;

    public Book(){}

    public Book(String title, int authorId, String ISBN, int publicationYear, double price, int stock) {
        this.title = title;
        this.authorId = authorId;
        this.ISBN = ISBN;
        this.publicationYear = publicationYear;
        this.price = price;
        this.stock = stock;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getAuthorId() {
        return authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    public String getISBN() {
        return ISBN;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    public int getPublicationYear() {
        return publicationYear;
    }
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
}
