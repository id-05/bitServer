package ru.bitServer.dao;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
public class BitServerScheduler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedulerid;
    private int usercreateid;
    private String destinationgroup;
    private String timecondition;
    private String source;
    private int clock;
    private int minute;
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

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public BitServerScheduler(){

    }

    public BitServerScheduler(int uservreatedid, String destinationgroup, String timecondition, String source, int clock, int minute, String modality){
        this.usercreateid = uservreatedid;
        this.destinationgroup = destinationgroup;
        this.timecondition = timecondition;
        this.source = source;
        this.clock = clock;
        this.minute = minute;
        this.modality = modality;
    }


}
