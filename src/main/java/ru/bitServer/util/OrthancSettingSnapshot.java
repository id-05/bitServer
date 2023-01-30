package ru.bitServer.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OrthancSettingSnapshot {

    String id;
    String date;
    String description;
    JsonObject settingJson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonObject getSettingJson() {
        return settingJson;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrthancSettingSnapshot(String date, String description, JsonObject settingJson) {
        this.date = date;
        this.description = description;
        this.settingJson = settingJson;
    }

    public OrthancSettingSnapshot(String id, String buf) {
        JsonParser parser = new JsonParser();
        JsonObject bufJson = new JsonObject();
        this.id = id;
        try {
            bufJson = parser.parse(buf).getAsJsonObject();
        }catch (Exception e){
            LogTool.getLogger().warn("Error parse json snapshot: "+buf+" "+e.getMessage());
        }
        if (bufJson.has("date"))         this.date = bufJson.get("date").getAsString();
        if (bufJson.has("description"))       this.description  = bufJson.get("description").getAsString();
        if (bufJson.has("settings"))       this.settingJson = bufJson.get("settings").getAsJsonObject();
    }

    public OrthancSettingSnapshot(){

    }
}
