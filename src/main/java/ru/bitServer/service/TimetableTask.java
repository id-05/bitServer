package ru.bitServer.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.bitServer.util.LogTool;

import java.time.LocalTime;

public class TimetableTask {
    int id;
    LocalTime timeTask;
    String action;
    String source;
    String destination;
    String description;

    @Override
    public String toString() {
        return timeTask.toString();
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
        String buf;
        if ("send".equals(action)) {
            buf = "Полученные от " + getSource() + " переслать в " + getDestination();
        } else {
            buf = "---";
        }
        return buf;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimetableTask(){

    }

    public TimetableTask(LocalTime timeTask, String action, String source, String destination, String description) {
        this.timeTask = timeTask;
        this.action = action;
        this.source = source;
        this.destination = destination;
        this.description = description;
    }

    public TimetableTask(String buf) {
        JsonParser parser = new JsonParser();
        JsonObject bufJson=new JsonObject();
        try {
            bufJson = parser.parse(buf).getAsJsonObject();
        }catch (Exception e){
            LogTool.getLogger().warn("Error parse json Task: "+buf+" "+e.getMessage());
        }
        if (bufJson.has("time"))         this.timeTask = strSecToLocTime(bufJson.get("time").getAsString());
        if (bufJson.has("action"))       this.action  = bufJson.get("action").getAsString();
        if (bufJson.has("source"))       this.source = bufJson.get("source").getAsString();
        if (bufJson.has("destination"))  this.destination = bufJson.get("destination").getAsString();

    }

    public int timeToSeconds(LocalTime localTime){
        return localTime.getHour()*60*60 + localTime.getMinute()*60 + localTime.getSecond();
    }

    public LocalTime strSecToLocTime(String buf){
        LocalTime bufTime = null;
        int hh = Integer.parseInt(buf) / 60;
        int mm = Integer.parseInt(buf) % 60;
        bufTime= LocalTime.of(hh,mm);
        return bufTime;
    }

    public String getStrTime(){
        return String.valueOf(this.timeTask.getHour()*60*60 + this.timeTask.getMinute()*60);
    }

//    public int timeToSeconds(String buf){
//        String[] bufArray = userPrefs.get("startTime","00:00").split(":");
//        int hh = Integer.parseInt(bufArray[0]);
//        int mm = Integer.parseInt(bufArray[1]);
//        return hh*60*60 + mm*60;
//    }
}
