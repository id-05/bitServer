package ru.bitServer.dao;

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
    private String ugroup;
    private String uTheme;
    private String blockStudy;
    private boolean hasBlockStudy;
    private int successStudyCount;
    private int returnStudyCount;
    private int rating;

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

    public String getUgroup() {
        return ugroup;
    }

    public void setUgroup(String ugroup) {
        this.ugroup = ugroup;
    }

    public Users(String uname, String password, boolean bool) {
        this.uname = uname;
        this.password = password;
        this.hasBlockStudy = bool;
    }

    public boolean isHasBlockStudy() {
        return hasBlockStudy;
    }

    public void setHasBlockStudy(boolean hasBlockStudy) {
        this.hasBlockStudy = hasBlockStudy;
    }

    public int getSuccessStudyCount() {
        return successStudyCount;
    }

    public void setSuccessStudyCount(int successStudyCount) {
        this.successStudyCount = successStudyCount;
    }

    public int getReturnStudyCount() {
        return returnStudyCount;
    }

    public void setReturnStudyCount(int returnStudyCount) {
        this.returnStudyCount = returnStudyCount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public String getuTheme() {
        return uTheme;
    }

    public void setuTheme(String uTheme) {
        this.uTheme = uTheme;
    }

    public String getBlockStudy() {
        return blockStudy;
    }

    public void setBlockStudy(String blockStudy) {
        this.blockStudy = blockStudy;
    }

    public Users(){

    }

    public Users(String login, String Password, String Name, String Middlename, String Family, String Role, String Group, boolean bool){
        this.uname = login;
        this.password = Password;
        this.ruName = Name;
        this.ruMiddleName = Middlename;
        this.ruFamily = Family;
        this.role = Role;
        this.ugroup = Group;
        this.hasBlockStudy = bool;
    }

    public String getSignature(){
        return this.uname+"/"+ruName+"/"+ruMiddleName+"/"+ruFamily;
    }
}
