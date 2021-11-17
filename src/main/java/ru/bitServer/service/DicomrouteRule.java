package ru.bitServer.service;

public class DicomrouteRule {
    String NameRemoteModality;
    boolean deleteAfteRoute;

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

    public DicomrouteRule(String NameRemoteModality, boolean deleteAfteRoute){
        this.NameRemoteModality = NameRemoteModality;
        this.deleteAfteRoute = deleteAfteRoute;
    }
}
