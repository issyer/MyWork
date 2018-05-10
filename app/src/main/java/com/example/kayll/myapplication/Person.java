package com.example.kayll.myapplication;

/**
 * Created by Kayll on 2018/2/21.
 */

public class Person {
    private String biaoti;
    private String nr;
    public void setBiaoti(String biaoti){this.biaoti=biaoti;}
    public String getBiaoti(){return biaoti;}
    public void setNr(String nr){this.nr=nr;}
    public String getNr(){return nr;}
    public Person(String biaoti,String nr){this.biaoti=biaoti;this.nr=nr;}

}
