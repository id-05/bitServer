package ru.bitServer.dicom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrthancStudy {

    private String studyDescription;
    private Date   date;
    private String accession;
    private String modality;
    private String StudyOrthancId;
    private String ShortId;
    private String studyDateToStr;
    private String PatientName;
    private String PatientSex;
    private String InstitutionName;
    private Date   PatientBirthDate;
    private List<OrthancSerie> series;

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
        //System.out.println("OrthancStudy");
        this.InstitutionName = InstitutionName;
        this.studyDescription = studyDescription;
        this.date = date;
        //System.out.println(" date = "+date);
        //System.out.println(" birthDate = "+birthDate);
        this.modality = modality;
        this.ShortId = patientID;
        this.PatientName = patientName;
        this.PatientBirthDate = birthDate;
        if(birthDate!=null) {
            this.PatientBirthDate.setHours(23);
        }
        //System.out.println(" PatientBirthDate = "+birthDate);
        this.PatientSex = sex;
        this.accession = accession;
        this.StudyOrthancId = StudyOrthancId;
        SimpleDateFormat format =new SimpleDateFormat("dd/MM/yyyy");
        this.studyDateToStr = format.format(date);
        this.series = new ArrayList<>();
    }

}
