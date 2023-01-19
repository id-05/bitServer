package ru.bitServer.dicom;

import java.io.Serializable;

public class DicomModaliti implements Serializable {

    @Override
    public String toString() {
        return dicomName;
    }


    private String dicomTitle;
    private String dicomName;
    private String ip;
    private String dicomPort;
    private String dicomProperty;

    public String getDicomTitle() {
        return dicomTitle;
    }

    public void setDicomTitle(String dicomTitle) {
        this.dicomTitle = dicomTitle;
    }

    public String getDicomName() {
        return dicomName;
    }

    public void setDicomName(String dicomName) {
        this.dicomName = dicomName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDicomPort() {
        return dicomPort;
    }

    public void setDicomPort(String dicomPort) {
        this.dicomPort = dicomPort;
    }

    public String getDicomProperty() {
        return dicomProperty;
    }

    public void setDicomProperty(String dicomProperty) {
        this.dicomProperty = dicomProperty;
    }

    public DicomModaliti (String title, String name, String ip, String port, String property) {
        this.dicomTitle = title;
        this.dicomName = name;
        this.ip = ip;
        this.dicomPort = port;
        this.dicomProperty = property;
    }

}

