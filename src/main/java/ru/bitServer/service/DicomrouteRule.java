package ru.bitServer.service;

public class DicomrouteRule {
    String Tag;
    String TagValue;
    String NameRemoteModality;
    boolean deleteAfteRoute;

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getTagValue() {
        return TagValue;
    }

    public void setTagValue(String tagValue) {
        TagValue = tagValue;
    }

    public String getNameRemoteModality() {
        return NameRemoteModality;
    }

    public void setNameRemoteModality(String nameRemoteModality) {
        NameRemoteModality = nameRemoteModality;
    }

    public boolean isDeleteAfteRoute() {
        return deleteAfteRoute;
    }

    public void setDeleteAfteRoute(boolean deleteAfteRoute) {
        this.deleteAfteRoute = deleteAfteRoute;
    }

    public DicomrouteRule(){

    }

    public DicomrouteRule(String Tag, String TagValue, String NameRemoteModality, boolean deleteAfteRoute){
        this.Tag = Tag;
        this.TagValue = TagValue;
        this.NameRemoteModality = NameRemoteModality;
        this.deleteAfteRoute = deleteAfteRoute;
    }
}
