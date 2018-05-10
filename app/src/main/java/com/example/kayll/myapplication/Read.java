package com.example.kayll.myapplication;

public class Read {
    private int id ;
    private String name;
    private String time;
    private String title;
    private int photo;
    public Read(int id,String name,String time,String title,int photo){
        this.id=id;
        this.name=name;
        this.time=time;
        this.title=title;
        this.photo=photo;
    }
    public Read(int id,String name,String time,String title){
        this.id=id;
        this.name=name;
        this.time=time;
        this.title=title;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
