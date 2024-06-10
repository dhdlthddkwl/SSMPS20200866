package com.example.ssmps_android.dto;

import com.example.ssmps_android.domain.CenterItem;
import com.example.ssmps_android.domain.Store;

public class RegistItemRequest {
    CenterItem centerItem;
    Store store;

    public RegistItemRequest(CenterItem centerItem, Store store) {
        this.centerItem = centerItem;
        this.store = store;
    }

    public CenterItem getCenterItem() {
        return centerItem;
    }

    public void setCenterItem(CenterItem centerItem) {
        this.centerItem = centerItem;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
