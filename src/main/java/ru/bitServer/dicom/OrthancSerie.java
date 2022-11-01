package ru.bitServer.dicom;

import com.google.gson.JsonArray;

import java.util.ArrayList;

public class OrthancSerie {

    String serieDescription;
    int nbInstances;
    String id;
    String seriesNumber;
    ArrayList<byte[]> instancesOfByte;
    //private JsonArray instances;
    int instancesCount;
    ArrayList<String> instances = new ArrayList<>();

    public OrthancSerie() {

    }

    public int getInstancesCount() {
        return instancesCount;
    }
//
//    //public JsonArray getInstances() {
//        return instances;
//    }


    public ArrayList<byte[]> getInstancesOfByte() {
        return instancesOfByte;
    }

    public void setInstancesOfByte(ArrayList<byte[]> instancesOfByte) {
        this.instancesOfByte = instancesOfByte;
    }

    public String getSerieDescription(){
        return serieDescription;
    }

    public String getId() {
        return id;
    }

    public int getNbInstances() {
        return nbInstances;
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

    public OrthancSerie(String id, String serieDescription, ArrayList<byte[]> instancesOfByte, int instancesCount) {
        this.serieDescription = serieDescription;
        this.id = id;
        this.instancesOfByte = instancesOfByte;
        this.instancesCount = instancesOfByte.size();
    }

    public OrthancSerie(String id, String serieDescription, ArrayList<String> instances) {
        this.serieDescription = serieDescription;
        this.id = id;
        this.instances = instances;
    }

    public OrthancSerie(String seriesDescription, String seriesNumber, String serieId) {
        this.serieDescription = seriesDescription;
        this.id = serieId;
        this.seriesNumber=seriesNumber;
    }

    public OrthancSerie(String seriesDescription, String seriesNumber, JsonArray instances, int size, String serieId) {
        this.serieDescription = seriesDescription;
        //this.instances = instances;
        this.nbInstances = size;
        this.id = serieId;
        this.seriesNumber = seriesNumber;
        this.instancesCount = instances.size();

    }
}
