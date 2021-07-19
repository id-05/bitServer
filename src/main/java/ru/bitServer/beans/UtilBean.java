package ru.bitServer.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.io.File;

@ManagedBean(name = "utilBean", eager = true)
@RequestScoped
public class UtilBean {

    public String usedSpace;
    public String totalSpace;
    public String freeSpace;
    public String directory;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(String usedSpace) {
        this.usedSpace = usedSpace;
    }

    public String getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(String totalSpace) {
        this.totalSpace = totalSpace;
    }

    public String getFreespace() {
        return freeSpace;
    }

    public void setFreespace(String freespace) {
        this.freeSpace = freespace;
    }

    @PostConstruct
    public void init() {
        //directory =
        File bufFile = new File("C:/");
        totalSpace = (int) (bufFile.getTotalSpace()/ (double) (1024*1024*1024))+" Gb";
        freeSpace = (int) (bufFile.getFreeSpace()/ (double) (1024*1024*1024))+" Gb";
        double bufDouble = (bufFile.getFreeSpace() / (double )bufFile.getTotalSpace());
        usedSpace = (int) (100 - bufDouble * 100) + "%";
    }

}
