package com.example.kayll.myapplication;

public class Shop {
    private int id1;
    private String title;
    private String money;
    private int id2;

    public Shop( int id1,String title,String money,int id2){
        this.id1=id1;
        this.title=title;
        this.money=money;
        this.id2=id2;
    }
    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }
}
