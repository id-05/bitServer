package ru.bitServer.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrthancSettingSnapshot {

    Date date;
    String description;
    JsonObject settingJson;
    DateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm");

    public JsonObject getSettingJson() {
        return settingJson;
    }

    public void setSettingJson(JsonObject settingJson) {
        this.settingJson = settingJson;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrthancSettingSnapshot(Date date, String description, JsonObject settingJson) {
        this.date = date;
        this.description = description;
        this.settingJson = settingJson;
    }

    public OrthancSettingSnapshot(String buf) throws ParseException {
        JsonParser parser = new JsonParser();
        JsonObject bufJson=new JsonObject();
        try {
            bufJson = parser.parse(buf).getAsJsonObject();
        }catch (Exception e){
            LogTool.getLogger().warn("Error parse json snapshot: "+buf+" "+e.getMessage());
        }

        if (bufJson.has("date"))         this.date = formatter.parse(bufJson.get("date").getAsString());
        if (bufJson.has("description"))       this.description  = bufJson.get("description").getAsString();
        if (bufJson.has("settings"))       this.settingJson = bufJson.get("settings").getAsJsonObject();
    }
}
