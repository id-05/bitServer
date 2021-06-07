package ru.bitServer.dicom;

import com.google.gson.JsonArray;

public class OrthancSerie {

    private String serieDescription;
    private int nbInstances;
    private String id;
    private String seriesNumber;
    private JsonArray instances;
    private int instancesCount;

    public int getInstancesCount() {
        return instancesCount;
    }

    public JsonArray getInstances() {
        return instances;
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

    public OrthancSerie(){

    }

    public OrthancSerie(String seriesDescription, String seriesNumber, String serieId) {
        this.serieDescription = seriesDescription;
        this.id = serieId;
        this.seriesNumber=seriesNumber;
    }

    public OrthancSerie(String seriesDescription, String seriesNumber, JsonArray instances, int size, String serieId) {
        this.serieDescription = seriesDescription;
        this.instances = instances;
        this.nbInstances = size;
        this.id = serieId;
        this.seriesNumber = seriesNumber;
        this.instancesCount = instances.size();

    }
}
