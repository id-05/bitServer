package ru.bitServer.service;

public class DicomRouteRule {
    String Tag;
    String TagValue;
    String NameRemoteModality;
    boolean deleteAfterRoute;

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

    public boolean isDeleteAfterRoute() {
        return deleteAfterRoute;
    }

    public void setDeleteAfterRoute(boolean deleteAfterRoute) {
        this.deleteAfterRoute = deleteAfterRoute;
    }

    public DicomRouteRule(){

    }

    public DicomRouteRule(String Tag, String TagValue, String NameRemoteModality, boolean deleteAfterRoute){
        this.Tag = Tag;
        this.TagValue = TagValue;
        this.NameRemoteModality = NameRemoteModality;
        this.deleteAfterRoute = deleteAfterRoute;
    }
}
