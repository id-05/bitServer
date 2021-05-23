package ru.bitServer.dicom;

import java.util.HashMap;

public class Patient {

    public String name;
    private String patientId;
    private String orthancID;
    private String birthDate;
    private String sex;
    private HashMap<String, Study> childStudies;
    private int studyCount;

    public int getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(int studyCount) {
        this.studyCount = studyCount;
    }

    public HashMap<String, Study> getChildStudies() {
        return childStudies;
    }

    public void setChildStudies(HashMap<String, Study> childStudies) {
        this.childStudies = childStudies;
    }

    public Patient(String name, String patientId, String birthDate, String sex, String orthancID, int studyCount) {
        this.name = name;
        this.patientId = patientId;
        this.orthancID = orthancID;
        this.birthDate = birthDate;
        this.sex = sex;
        this.studyCount = studyCount;
    }

    public String getName() {
        return this.name;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public String getPatientOrthancId() {
        return this.orthancID;
    }

    public String getPatientBirthDate() {
        return this.birthDate;
    }

    public String getPatientSex() {
        return this.sex;
    }

    public void addStudy(Study study) {
        if(childStudies==null) {
            childStudies= new HashMap<>();
        }
        childStudies.put(study.getOrthancId(), study);
    }
}