package ru.CustomOrthancWebMorda.beans.dicom;

import ru.CustomOrthancWebMorda.beans.dicom.Serie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Study {

    private String studyDescription;
    private Date   date;
    private String accession;
    private String StudyOrthancId;
    private String studyDateToStr;
    private List<Serie> Series;

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

    public Study(String studyDescription, Date date, String accession,
                 String StudyOrthancId, String patientName, String patientID, Date birthDate, String sex, String patientOrthancId, String studyInstanceUID) {
        this.studyDescription = studyDescription;
        this.date = date;
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
