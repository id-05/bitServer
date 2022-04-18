package ru.bitServer.dicom;

import java.io.Serializable;

public class DicomModaliti implements Serializable {

    private String dicomtitle;
    private String dicomname;
    private String ip;
    private String dicomport;
    private String dicomproperty;

    public String getDicomtitle() {
        return dicomtitle;
    }

    public void setDicomtitle(String dicomtitle) {
        this.dicomtitle = dicomtitle;
    }

    public String getDicomname() {
        return dicomname;
    }

    public void setDicomname(String dicomname) {
        this.dicomname = dicomname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDicomport() {
        return dicomport;
    }

    public void setDicomport(String dicomport) {
        this.dicomport = dicomport;
    }

    public String getDicomproperty() {
        return dicomproperty;
    }

    public void setDicomproperty(String dicomproperty) {
        this.dicomproperty = dicomproperty;
    }

    public DicomModaliti (String title, String name, String ip, String port, String property) {
        this.dicomtitle = title;
        this.dicomname = name;
        this.ip = ip;
        this.dicomport = port;
        this.dicomproperty = property;
    }

}

