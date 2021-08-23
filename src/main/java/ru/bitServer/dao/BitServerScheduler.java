package ru.bitServer.dao;

import javax.persistence.*;

@Entity
public class BitServerScheduler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedulerid;
    private int usercreateid;
    private String destinationgroup;
    private String timecondition;
    private String source;
    private String clock;
    private String minute;
    private String modality;

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public Long getSchedulerid() {
        return schedulerid;
    }

    public void setSchedulerid(Long schedulerid) {
        this.schedulerid = schedulerid;
    }

    public int getUsercreateid() {
        return usercreateid;
    }

    public void setUsercreateid(int usercreateid) {
        this.usercreateid = usercreateid;
    }

    public String getDestinationgroup() {
        return destinationgroup;
    }

    public void setDestinationgroup(String destinationgroup) {
        this.destinationgroup = destinationgroup;
    }

    public String getTimecondition() {
        return timecondition;
    }

    public void setTimecondition(String timecondition) {
        this.timecondition = timecondition;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public BitServerScheduler(){

    }

    public BitServerScheduler(int uservreatedid, String destinationgroup, String timecondition, String source, String clock, String minute, String modality){
        this.usercreateid = uservreatedid;
        this.destinationgroup = destinationgroup;
        this.timecondition = timecondition;
        this.source = source;
        this.clock = clock;
        this.minute = minute;
        this.modality = modality;
    }
}
