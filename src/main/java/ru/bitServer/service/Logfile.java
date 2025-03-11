package ru.bitServer.service;


import java.io.File;

public class Logfile {

    String rname;
    File file;

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Logfile() {
    }

    public Logfile(String name, File file) {
        this.rname = name;
        this.file = file;
    }
}
