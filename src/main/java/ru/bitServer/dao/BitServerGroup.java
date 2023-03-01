package ru.bitServer.dao;

import java.util.ArrayList;

public class BitServerGroup {

    private Long id;
    private String ruName;
    ArrayList<BitServerUser> userList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    public ArrayList<BitServerUser> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<BitServerUser> userList) {
        this.userList = userList;
    }

    public BitServerGroup(Long id, String ruName, ArrayList<BitServerUser> userList){
        this.id = id;
        this.ruName = ruName;
        this.userList = userList;
    }

    public BitServerGroup(){
    }

}
