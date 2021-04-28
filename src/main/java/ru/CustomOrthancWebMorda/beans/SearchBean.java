package ru.CustomOrthancWebMorda.beans;

import jdk.nashorn.internal.objects.annotations.Constructor;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "searchBean", eager = true)
@ViewScoped
public class SearchBean {

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String value1;

    FacesMessage.Severity info = FacesMessage.SEVERITY_INFO;
    FacesMessage.Severity error = FacesMessage.SEVERITY_ERROR;
    FacesMessage.Severity warning = FacesMessage.SEVERITY_WARN;


    @PostConstruct
    public void init() {
        System.out.println("seach");
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
