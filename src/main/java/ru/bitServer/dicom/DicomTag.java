package ru.bitServer.dicom;

public class DicomTag {

    String TagName;
    String TagValue;
    boolean hasChange;

    public boolean isHasChange() {
        return hasChange;
    }

    public void setHasChange(boolean hasChange) {
        this.hasChange = hasChange;
    }

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
        this.hasChange = false;
        this.TagName = tagName;
        this.TagValue = tagValue;
    }

    public DicomTag(){

    }
}
