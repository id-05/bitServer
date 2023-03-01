package ru.bitServer.dicom;

import java.util.ArrayList;

public class OrthancSerie {

    String serieDescription;
    String id;
    String seriesNumber;
    ArrayList<byte[]> instancesOfByte;
    int instancesCount;
    ArrayList<String> instances = new ArrayList<>();

    public OrthancSerie() {

    }
    public int getInstancesCount() {
        return instancesCount;
    }

    public ArrayList<byte[]> getInstancesOfByte() {
        return instancesOfByte;
    }

    public String getSerieDescription(){
        return serieDescription;
    }

    public String getId() {
        return id;
    }

    public String getSeriesNumber(){
        return this.seriesNumber;
    }


    public ArrayList<String> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<String> instances) {
        this.instances = instances;
    }

    public OrthancSerie(String id, String serieDescription, ArrayList<byte[]> instancesOfByte) {
        this.serieDescription = serieDescription;
        this.id = id;
        this.instancesOfByte = instancesOfByte;
        this.instancesCount = instancesOfByte.size();
    }
}
