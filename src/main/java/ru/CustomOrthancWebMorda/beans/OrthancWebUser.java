package ru.CustomOrthancWebMorda.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean(name = "orthancWebUser")
@ViewScoped
public class OrthancWebUser implements Serializable {

    public String login;

    private String password;

    public OrthancWebUser() {}

    public OrthancWebUser(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public OrthancWebUser clone() {
        return new OrthancWebUser(getlogin(), getpassword());
    }

    public String getlogin() {
        return login;
    }

    public void setlogin(String login) {
        this.login = login;
    }

    public String getpassword() {
        return password;
    }

    public void setName(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrthancWebUser other = (OrthancWebUser) obj;
        if (login == null) {
            return other.login == null;
        } else return login.equals(other.login);
    }
}
