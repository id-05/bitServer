package ru.CustomOrthancWebMorda.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean(name = "autoriseBean", eager = false)
@ViewScoped
public class autoriseBean {

    String inputUserName;
    String inputPassword;

    public String getInputUserName() {
        return inputUserName;
    }

    public void setInputUserName(String inputUserName) {
        this.inputUserName = inputUserName;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    @PostConstruct
    public void init() {

    }

    public void enterPressed(AjaxBehaviorEvent event){
        System.out.println(inputPassword+"   "+inputUserName+"     "+event);
    }
}
