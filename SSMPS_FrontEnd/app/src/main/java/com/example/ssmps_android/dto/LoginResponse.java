package com.example.ssmps_android.dto;

import java.util.List;

public class LoginResponse {
    private Long id;
    private String accountId;
    private List<StoreResponse> storeResponseList;

    private String token;

    public LoginResponse(Long id, String accountId, List<StoreResponse> storeResponseList, String token) {
        this.id = id;
        this.accountId = accountId;
        this.storeResponseList = storeResponseList;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<StoreResponse> getStoreResponseList() {
        return storeResponseList;
    }

    public void setStoreResponseList(List<StoreResponse> storeResponseList) {
        this.storeResponseList = storeResponseList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
