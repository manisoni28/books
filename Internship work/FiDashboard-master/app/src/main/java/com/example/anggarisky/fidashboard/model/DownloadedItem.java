package com.example.anggarisky.fidashboard.model;

public class Item {
    String id;
    String name;
    String url;
    double price;
    int thumbnail;

 
    public Item() {
    }

    public Item(String key,String url) {
        name = key;
        this.url = url;
    }

    public Item(String id, String key, String url) {
        name =key;
        this.url = url;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getUrl() {
        return url;
    }
 
    public void setUrl(String url) {
        this.url = url;
    }
 
    public double getPrice() {
        return price;
    }
 
    public void setPrice(double price) {
        this.price = price;
    }

}