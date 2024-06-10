package com.example.ssmps_android.dto;

import com.example.ssmps_android.domain.Location;

import java.util.List;

public class LocationRequest {
    List<Location> locationList;

    public LocationRequest(List<Location> locationList) {
        this.locationList = locationList;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }
}
