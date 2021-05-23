package ru.bitServer.dicomviewer;


import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Study {
    private String studyInstanceUID;
    private String studyId;
    private Date studyDateTime;
    private String accessionNumber;
    private String studyDescription;
    private Patient patient;
    private LinkedList<Series> series = new LinkedList();

    public Study() {
    }

    public String getStudyInstanceUID() {
        return this.studyInstanceUID;
    }

    public void setStudyInstanceUID(String studyInstanceUID) {
        this.studyInstanceUID = studyInstanceUID;
    }

    public String getStudyId() {
        return this.studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public Date getStudyDateTime() {
        return this.studyDateTime;
    }

    public void setStudyDateTime(Date studyDateTime) {
        this.studyDateTime = studyDateTime;
    }

    public String getAccessionNumber() {
        return this.accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getStudyDescription() {
        return this.studyDescription;
    }

    public void setStudyDescription(String studyDescription) {
        this.studyDescription = studyDescription;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Series> getSeries() {
        return Collections.unmodifiableList(this.series);
    }

    public void addSeries(Series series) {
        this.series.addLast(series);
    }
}
