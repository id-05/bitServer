package ru.CustomOrthancWebMorda.beans.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Usergroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gType;
    private String ruName;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getgType() {
        return gType;
    }

    public void setgType(String gType) {
        this.gType = gType;
    }

    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }


    public Usergroup() {

    }

    public Usergroup(String gName, String ruName, String status){
        this.gType = gName;
        this.ruName = ruName;
        this.status = status;
    }
}
