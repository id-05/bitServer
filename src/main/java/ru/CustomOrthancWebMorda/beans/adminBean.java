package ru.CustomOrthancWebMorda.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "adminBean", eager = true)
@SessionScoped
public class adminBean {

    @PostConstruct
    public void init(){
        System.out.println("admin page");
    }
}
