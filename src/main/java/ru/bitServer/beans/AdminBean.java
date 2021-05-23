package ru.bitServer.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "adminBean", eager = true)
@SessionScoped
public class AdminBean {
    public String buf = "buffer";

    public String getBuf() {
        return buf;
    }

    public void setBuf(String buf) {
        this.buf = buf;
    }

    @PostConstruct
    public void init(){
        System.out.println("admin page");
    }
}
