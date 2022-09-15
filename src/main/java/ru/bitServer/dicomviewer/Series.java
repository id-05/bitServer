package ru.bitServer.dicomviewer;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Series {
    private String seriesInstanceUID;
    private String seriesNumber;
    private String modality;
    private String institutionName;
    private String seriesDescription;
    private LinkedList<Instance> instances = new LinkedList();
    private Study study;

    public Series() {
    }

    public String getSeriesInstanceUID() {
        return this.seriesInstanceUID;
    }

    public void setSeriesInstanceUID(String seriesInstanceUID) {
        this.seriesInstanceUID = seriesInstanceUID;
    }

    public String getSeriesNumber() {
        return this.seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getModality() {
        return this.modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getInstitutionName() {
        return this.institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getSeriesDescription() {
        return this.seriesDescription;
    }

    public void setSeriesDescription(String seriesDescription) {
        this.seriesDescription = seriesDescription;
    }

    public List<Instance> getInstances() {
        return Collections.unmodifiableList(this.instances);
    }

    public void addInstance(Instance instance) {
        this.instances.addLast(instance);
    }

    public Study getStudy() {
        return this.study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}
