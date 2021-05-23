package ru.bitServer.dicom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Study {

    private String studyDescription;
    private Date   date;
    private String accession;
    private String modality;
    private String StudyOrthancId;
    private String ShortId;
    private String studyDateToStr;
    private String PatientName;
    private String PatientSex;
    private Date PatientBirthDate;
    private List<Serie> Series;

    public String getShortId() {
        return ShortId;
    }

    public void setShortId(String shortId) {
        ShortId = shortId;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getPatientSex() {
        return PatientSex;
    }

    public void setPatientSex(String patientSex) {
        PatientSex = patientSex;
    }

    public Date getPatientBirthDate() {
        return PatientBirthDate;
    }

    public void setPatientBirthDate(Date patientBirthDate) {
        PatientBirthDate = patientBirthDate;
    }

    public Study(String s, String s1, String s2) {
    }

    public List<Serie> getSeries() {
        return Series;
    }

    public void setSeries(List<Serie> series) {
        Series = series;
    }


    public String getStudyDateToStr() {
        return studyDateToStr;
    }

    public void setStudyDateToStr(String studyDateToStr) {
        this.studyDateToStr = studyDateToStr;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public Study(String studyDescription, String modality, Date date, String accession, String StudyOrthancId, String patientName, String patientID, Date birthDate, String sex, String patientOrthancId, String studyInstanceUID) {
        this.studyDescription = studyDescription;
        this.date = date;
        this.modality = modality;
        this.ShortId = patientID;
        this.PatientName = patientName;
        this.PatientBirthDate = birthDate;
        this.PatientSex = sex;
        this.accession = accession;
        this.StudyOrthancId = StudyOrthancId;
        SimpleDateFormat format =new SimpleDateFormat("dd/MM/yyyy");
        this.studyDateToStr = format.format(date);
        this.Series = new ArrayList<>();
    }
    

    public Study(String studyDescription, Date studyDateObject, String accessionNumber, String studyId) {
        this.studyDescription = studyDescription;
        this.date = studyDateObject;
        this.accession = accessionNumber;
        this.StudyOrthancId = studyId;
        SimpleDateFormat format =new SimpleDateFormat("dd/MM/yyyy");
        this.studyDateToStr = format.format(date);
        this.Series = new ArrayList<>();
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public String getOrthancId() {
        return StudyOrthancId;
    }

    public Date getDate() {
        return date;
    }

    public String getAccession() {
        return accession;
    }

}
