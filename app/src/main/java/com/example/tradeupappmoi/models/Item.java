package com.example.tradeupappmoi.models;

public class Item {
    private String title;
    private double price;
    private String category;
    private String location;
    private String image;
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }  // Getter cho location
    public void setLocation(String location) { this.location = location; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}

