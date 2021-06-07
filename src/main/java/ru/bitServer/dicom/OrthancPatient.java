package ru.bitServer.dicom;

import java.util.HashMap;

public class OrthancPatient {

    public String name;
    private final String patientId;
    private final String orthancID;
    private final String birthDate;
    private final String sex;
    private HashMap<String, OrthancStudy> childStudies;
    private int studyCount;

    public int getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(int studyCount) {
        this.studyCount = studyCount;
    }

    public HashMap<String, OrthancStudy> getChildStudies() {
        return childStudies;
    }

    public void setChildStudies(HashMap<String, OrthancStudy> childStudies) {
        this.childStudies = childStudies;
    }

    public OrthancPatient(String name, String patientId, String birthDate, String sex, String orthancID, int studyCount) {
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

    public void addStudy(OrthancStudy study) {
        if(childStudies == null) {
            childStudies = new HashMap<>();
        }
        childStudies.put(study.getOrthancId(), study);
    }
}