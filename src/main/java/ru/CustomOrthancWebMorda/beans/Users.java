package ru.CustomOrthancWebMorda.beans;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String uname;
    private String password;
    private String ruName;
    private String ruMiddleName;
    private String ruFamily;
    private String role;
    private String groupUser;


    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    public String getRuMiddleName() {
        return ruMiddleName;
    }

    public void setRuMiddleName(String ruMiddleName) {
        this.ruMiddleName = ruMiddleName;
    }

    public String getRuFamily() {
        return ruFamily;
    }

    public void setRuFamily(String ruFamily) {
        this.ruFamily = ruFamily;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGroupUser() {
        return groupUser;
    }

    public void setGroupUser(String groupUser) {
        this.groupUser = groupUser;
    }

    public Users(String uname, String password) {
        this.uname = uname;
        this.password = password;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users(){

    }

    public Users(String login, String Password, String Name, String Middlename, String Family, String Role, String Group){
        this.uname = login;
        this.password = Password;
        this.ruName = Name;
        this.ruMiddleName = Middlename;
        this.ruFamily = Family;
        this.role = Role;
        this.groupUser = Group;
    }
}
