package ru.bitServer.service;

public class Maindicomtags {
    String id;
    String taggroup;
    String tagelement;
    String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaggroup() {
        return taggroup;
    }

    public void setTaggroup(String taggroup) {
        this.taggroup = taggroup;
    }

    public String getTagelement() {
        return tagelement;
    }

    public void setTagelement(String tagelement) {
        this.tagelement = tagelement;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Maindicomtags(String id, String taggroup, String tagelement, String value) {
        this.id = id;
        this.taggroup = taggroup;
        this.tagelement = tagelement;
        this.value = value;
    }
}
