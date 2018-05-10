package com.example.kayll.myapplication;

public class Message {


    private int Id;
    private String Name;
    private String title;
    private String time;

    public Message(){

    }
    public Message(int id,String name,String title,String time){
        this.Id=id;
        this.Name=name;
        this.time=time;
        this.title=title;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
