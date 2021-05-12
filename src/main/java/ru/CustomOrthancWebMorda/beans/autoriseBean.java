package ru.CustomOrthancWebMorda.beans;


import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


@ManagedBean(name = "autoriseBean", eager = false)
@SessionScoped
public class autoriseBean implements UserDao {

    String inputUserName;
    String inputPassword;
    public Users User;

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
       System.out.println("autorise");
       initialHibernate();
    }

    public void loginValidate(){
        System.out.println("loginValidate");
        User = validateUserAndGetIfExist(inputUserName,inputPassword);
        if (User.getUname()!=null){
            PrimeFaces.current().executeScript("window.open('http://192.168.1.58:8084/index.xhtml')");
        }
        System.out.println(User.getUname());
    }


    public String validateUsernamePassword() {
        User = validateUserAndGetIfExist(inputUserName,inputPassword);

        if (User.getUname()!=null) {
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", User.getUid());
            return "homePage";
        } else {

            return "autorisePage";
        }
    }

    //logout event, invalidate session
    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        return "autorisePage";
    }
}
