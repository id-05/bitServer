package ru.bitServer.dao;

import javax.persistence.*;

@Entity
public class Usergroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ruName;
    private String ruContragent;
    private boolean downloadTrue;
    private String status;
    private boolean forlocal;

    public boolean isForlocal() {
        return forlocal;
    }

    public void setForlocal(boolean forlocal) {
        this.forlocal = forlocal;
    }

    public String getRuContragent() {
        return ruContragent;
    }

    public void setRuContragent(String ruContragent) {
        this.ruContragent = ruContragent;
    }

    public boolean isDownloadTrue() {
        return downloadTrue;
    }

    public void setDownloadTrue(boolean downloadTrue) {
        this.downloadTrue = downloadTrue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }


    public Usergroup() {

    }

    public Usergroup(String ruContragent, String ruName, String status, boolean downloadTrue, boolean forlocal){
        this.ruName = ruName;
        this.ruContragent = ruContragent;
        this.downloadTrue = downloadTrue;
        this.forlocal = forlocal;
        this.status = status;
    }
}
