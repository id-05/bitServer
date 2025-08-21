package ru.bitServer.service;

public class SourceDevice {

    String stationName;
    String visibleName;
    String modality;
    String lastActive;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getVisibleName() {
        return visibleName;
    }

    public void setVisibleName(String visibleName) {
        this.visibleName = visibleName;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public SourceDevice(){

    }

    public SourceDevice(String stationName, String visibleName, String modality) {
        this.stationName = stationName;
        this.visibleName = visibleName;
        this.modality = modality;
    }

    public SourceDevice(String stationName, String visibleName, String modality, String lastActive) {
        this.stationName = stationName;
        this.visibleName = visibleName;
        this.modality = modality;
        this.lastActive = lastActive;
    }
}
