package ru.bitServer.service;

public class BitServerApp {
    String name;
    String date;

    public BitServerApp() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BitServerApp(String name, String date){
        this.name = name;
        this.date = date;
    }
}