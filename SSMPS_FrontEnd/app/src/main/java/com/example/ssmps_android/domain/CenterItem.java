package com.example.ssmps_android.domain;

import java.io.Serializable;

public class CenterItem implements Serializable {
    private Long id;
    private String name;
    private String type;
    private int price;
    private String image;
    private String barcode;

    public CenterItem(Long id, String name, String type, int price, String image, String barcode) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.image = image;
        this.barcode = barcode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
