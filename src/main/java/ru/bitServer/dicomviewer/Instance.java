package ru.bitServer.dicomviewer;

import javax.activation.DataSource;
import java.util.Date;

public abstract class Instance {
    private DataSource dataSource;
    private String sopClassUID;
    private String sopInstanceUID;
    private Date acquisitionDateTime;
    private String transferSyntaxUID;
    private String instanceNumber;
    private Series series;

    public Instance() {
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getSopClassUID() {
        return this.sopClassUID;
    }

    public void setSopClassUID(String sopClassUID) {
        this.sopClassUID = sopClassUID;
    }

    public String getSopInstanceUID() {
        return this.sopInstanceUID;
    }

    public void setSopInstanceUID(String sopInstanceUID) {
        this.sopInstanceUID = sopInstanceUID;
    }

    public Date getAcquisitionDateTime() {
        return this.acquisitionDateTime;
    }

    public void setAcquisitionDateTime(Date acquisitionDateTime) {
        this.acquisitionDateTime = acquisitionDateTime;
    }

    public String getTransferSyntaxUID() {
        return this.transferSyntaxUID;
    }

    public void setTransferSyntaxUID(String transferSyntaxUID) {
        this.transferSyntaxUID = transferSyntaxUID;
    }

    public String getInstanceNumber() {
        return this.instanceNumber;
    }

    public void setInstanceNumber(String instanceNumber) {
        this.instanceNumber = instanceNumber;
    }

    public Series getSeries() {
        return this.series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }
}
