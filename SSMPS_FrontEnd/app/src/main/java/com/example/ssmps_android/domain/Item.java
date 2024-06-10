package com.example.ssmps_android.domain;

import java.io.Serializable;

public class Item implements Serializable {
    private Long id;
    private Store store;
    private String name;
    private String type;
    private int price;
    private int quantity;
    private String image;
    private Location location;
    private String barcode;

    public Item(Long id, Store store, String name, String type, int price, int quantity, String image, Location location, String barcode) {
        this.id = id;
        this.store = store;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
//        this.location = location;
        this.barcode = barcode;
    }

    public Item(Long id, String name, String type, int price, int quantity, String image, Location location, String barcode) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
//        this.location = location;
        this.barcode = barcode;
    }

    public Item(Long id, Store store, String name, String type, int price, int quantity, String image, String barcode) {
        this.id = id;
        this.store = store;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.barcode = barcode;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    public Location getLocation() {
//        return location;
//    }

//    public void setLocation(Location location) {
//        this.location = location;
//    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
