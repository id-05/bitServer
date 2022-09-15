package ru.bitServer.dicom;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrthancStudy {

    String studyDescription;
    Date   date;
    String accession;
    String modality;
    String StudyOrthancId;
    String ShortId;
    String studyDateToStr;
    String PatientName;
    String PatientSex;
    String InstitutionName;
    Date   PatientBirthDate;
    SimpleDateFormat format =new SimpleDateFormat("dd/MM/yyyy");

    public String getInstitutionName() {
        return InstitutionName;
    }

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

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
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


    public OrthancStudy(String InstitutionName, String studyDescription, String modality, Date date, String accession, String StudyOrthancId, String patientName, String patientID, Date birthDate, String sex, String patientOrthancId, String studyInstanceUID) {
        this.InstitutionName = InstitutionName;
        this.studyDescription = studyDescription;
        this.date = date;
        this.modality = modality;
        this.ShortId = patientID;
        this.PatientName = patientName;
        this.PatientBirthDate = birthDate;
        if(birthDate!=null) {
            this.PatientBirthDate.setHours(23);
        }
        this.PatientSex = sex;
        this.accession = accession;
        this.StudyOrthancId = StudyOrthancId;
        this.studyDateToStr = format.format(date);
    }

    public OrthancStudy(String PatientName){
        this.PatientName = PatientName;
    }

}
