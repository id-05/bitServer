package ru.bitServer.service;

public class WorkListItem {

    private String DicomTag;
    private String VR;
    private String Value;

    public String getDicomTag() {
        return DicomTag;
    }

    public void setDicomTag(String dicomTag) {
        DicomTag = dicomTag;
    }

    public String getVR() {
        return VR;
    }

    public void setVR(String VR) {
        this.VR = VR;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public WorkListItem(){

    }

    public WorkListItem(String dicomTag, String VR, String value) {
        this.DicomTag = dicomTag;
        this.VR = VR;
        this.Value = value;
    }

    @Override
    public String toString() {
        return "DicomTag= "+DicomTag+" VR= "+VR+" Value= " +Value;
    }
}
