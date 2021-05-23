package ru.bitServer.dicom;

import java.io.Serializable;

public class OrthancWebUser implements Serializable {

    private String login;
    private String pass;

    public OrthancWebUser(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
