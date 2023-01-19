package ru.bitServer.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.bitServer.dicom.DicomModaliti;
import ru.bitServer.util.LogTool;

import java.time.LocalTime;

public class TimetableTask {
    int id;
    LocalTime timeTask;
    DicomModaliti sourceAET;
    DicomModaliti destinationAET;
    String action;
    String source;
    String destination;
    String description;

    @Override
    public String toString() {
        return timeTask.toString();
    }

    public DicomModaliti getSourceAET() {
        return sourceAET;
    }

    public void setSourceAET(DicomModaliti sourceAET) {
        this.sourceAET = sourceAET;
    }

    public DicomModaliti getDestinationAET() {
        return destinationAET;
    }

    public void setDestinationAET(DicomModaliti destinationAET) {
        this.destinationAET = destinationAET;
    }

    public LocalTime getTimeTask() {
        return timeTask;
    }

    public void setTimeTask(LocalTime timeTask) {
        this.timeTask = timeTask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAltSource() {
        return source;
    }

    public String getSource() {
        String buf = null;
        if(source!=null) {
            if (source.equals("all")) {
                buf = "всех";
            } else {
                buf = source;
            }
        }
        return buf;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        String buf = "";
        switch (action){
            case "send":
                buf = "Полученные от " + getSource() + " переслать в " + getDestination();
                break;
            case "reset":
                buf = "Перезагрузка сервера ";
                break;
            case "deleteOld":
                buf = "Удаление снимков сделанных более 5 лет назад";
            default: break;
        }
        return buf;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimetableTask(){
        this.id = 0;
    }

    public TimetableTask(String id, String buf) {
        JsonParser parser = new JsonParser();
        JsonObject bufJson=new JsonObject();
        try {
            bufJson = parser.parse(buf).getAsJsonObject();
        }catch (Exception e){
            LogTool.getLogger().warn("Error parse json Task: "+buf+" "+e.getMessage());
        }
        this.id = Integer.parseInt(id);
        if (bufJson.has("time"))         this.timeTask = strSecToLocTime(bufJson.get("time").getAsString());
        if (bufJson.has("action"))       this.action  = bufJson.get("action").getAsString();
        if (bufJson.has("source"))       this.source = bufJson.get("source").getAsString();
        if (bufJson.has("destination"))  this.destination = bufJson.get("destination").getAsString();
    }

    public LocalTime strSecToLocTime(String buf){
        LocalTime bufTime;
        int hh = Integer.parseInt(buf) / 60;
        int mm = Integer.parseInt(buf) % 60;
        bufTime= LocalTime.of(hh,mm);
        return bufTime;
    }

    public String getStrTime(){
        return String.valueOf(this.timeTask.getHour()*60 + this.timeTask.getMinute());
    }
}
