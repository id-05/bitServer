package ru.bitServer.dicom;

public class DicomTag {

    String TagName;
    String TagValue;

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String tagName) {
        TagName = tagName;
    }

    public String getTagValue() {
        return TagValue;
    }

    public void setTagValue(String tagValue) {
        TagValue = tagValue;
    }

    public DicomTag(String tagName, String tagValue) {
        TagName = tagName;
        TagValue = tagValue;
    }

    public DicomTag(){

    }
}
