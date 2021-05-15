package ru.CustomOrthancWebMorda.beans;

import java.io.Serializable;

public class VisibleStudy implements Serializable {

    private String orthancId;
    private String patientName;
    private String description;
    private String dateToStr;

    public String getOrthancId() {
        return orthancId;
    }

    public void setOrthancId(String orthancId) {
        this.orthancId = orthancId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateToStr() {
        return dateToStr;
    }

    public void setDateToStr(String dateToStr) {
        this.dateToStr = dateToStr;
    }

    public VisibleStudy(){

    }

    public VisibleStudy(String OrthancId, String PatientName, String Description, String DateToStr){
        this.orthancId = OrthancId;
        this.patientName = PatientName;
        this.description = Description;
        this.dateToStr = DateToStr;
    }

}
