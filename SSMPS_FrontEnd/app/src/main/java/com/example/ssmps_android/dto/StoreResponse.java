package com.example.ssmps_android.dto;

import android.location.LocationListener;

import com.example.ssmps_android.domain.Location;

import java.util.List;

public class StoreResponse {
    private Long id;
    private String name;
    private String address;

    private List<Location> locationList;

    public StoreResponse(Long id, String name, String address) {
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

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }
}
