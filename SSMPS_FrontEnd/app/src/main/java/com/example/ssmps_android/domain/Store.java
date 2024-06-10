package com.example.ssmps_android.domain;

import java.io.Serializable;
import java.util.List;

public class Store implements Serializable {
    private Long id;
    private String name;
    private String address;
    private List<Location> locationList;

    public Store(Long id, String name, String address, List<Location> locationList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.locationList = locationList;
    }

    public Store(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setLocation(List<Location> items) {
        this.locationList = items;
    }

    public List<Location> getLocationList() {
        return locationList;
    }
}