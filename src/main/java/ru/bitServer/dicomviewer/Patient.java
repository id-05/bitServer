package ru.bitServer.dicomviewer;


import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Patient {
    private String patientId;
    private String patientName;
    private Date patientBirthDate;
    private String patientSex;
    private LinkedList<Study> studies = new LinkedList();

    public Patient() {
    }

    public String getPatientId() {
        return this.patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return this.patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Date getPatientBirthDate() {
        return this.patientBirthDate;
    }

    public void setPatientBirthDate(Date patientBirthDate) {
        this.patientBirthDate = patientBirthDate;
    }

    public String getPatientSex() {
        return this.patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public List<Study> getStudies() {
        return Collections.unmodifiableList(this.studies);
    }

    public void addStudy(Study study) {
        this.studies.addLast(study);
    }
}
