package ru.bitServer.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Time;
import java.util.Date;

@Entity
public class BitServerScheduler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedulerid;
    private int usercreateid;
    private int destinationgroup;
    private String timecondition;
    private String source;
    private Date time;
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

    public int getDestinationgroup() {
        return destinationgroup;
    }

    public void setDestinationgroup(int destinationgroup) {
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BitServerScheduler(){

    }

    public BitServerScheduler(int uservreatedid, int destinationgroup, String timecondition, String source, Date time, String modality){
        this.usercreateid = uservreatedid;
        this.destinationgroup = destinationgroup;
        this.timecondition = timecondition;
        this.source = source;
        this.time = time;
        this.modality = modality;
    }


}
