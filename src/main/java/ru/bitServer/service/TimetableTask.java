package ru.bitServer.service;

import java.util.Date;

public class TimetableTask {
    int id;
    Date timeTask;
    String action;
    String description;

    public Date getTimeTask() {
        return timeTask;
    }

    public void setTimeTask(Date timeTask) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimetableTask(){

    }

    public TimetableTask(Date timeTask, String action, String description) {
        this.timeTask = timeTask;
        this.action = action;
        this.description = description;
    }
}
