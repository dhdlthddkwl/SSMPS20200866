package com.example.ssmps_paymentscreen;

public class ListViewItem {
    private String text; // 상품 이름
    private int price; // 상품 가격
    private int cnt; // 상품 갯수

    public ListViewItem(String text, int price, int cnt){
        this.text = text;
        this.price = price;
        this.cnt = cnt;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getText() {
        return this.text;
    }

    public String getPrice() {
        return String.valueOf(this.price);
    }

    public String getCnt() {
        return String.valueOf(this.cnt);
    }

    public String getAll_price() { return String.valueOf(this.price *  this.cnt); }
}
