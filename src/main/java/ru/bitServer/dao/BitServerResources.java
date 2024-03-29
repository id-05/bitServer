package ru.bitServer.dao;

public class BitServerResources {

    private Long id;
    private String rname;
    private String rvalue;
    private boolean isBoolType;

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRvalue() {
        return rvalue;
    }

    public void setRvalue(String rvalue) {
        this.rvalue = rvalue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public BitServerResources(){

    }

    public BitServerResources(String rname, String rvalue){
        this.rname = rname;
        this.rvalue = rvalue;
    }

    public BitServerResources(String rname, String rvalue, Long id){
        this.rname = rname;
        this.rvalue = rvalue;
        this.id = id;
    }
}
