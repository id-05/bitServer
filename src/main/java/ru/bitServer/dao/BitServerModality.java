package ru.bitServer.dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitServerModality {
    private String name;

    public BitServerModality() {

    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    public String getName() {
        return name;
    }

}
