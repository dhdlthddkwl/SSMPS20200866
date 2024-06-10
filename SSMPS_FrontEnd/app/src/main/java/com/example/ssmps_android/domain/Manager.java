package com.example.ssmps_android.domain;

import java.util.List;

public class Manager {
    private Long id;
    private String accountId;
    private String password;
    private List<Store> storeList;

    public Manager(Long id, String accountId, String password, List<Store> storeList) {
        this.id = id;
        this.accountId = accountId;
        this.password = password;
        this.storeList = storeList;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }
}