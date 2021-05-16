package ru.CustomOrthancWebMorda.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BitServerStudy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sid;
    private String shortid;
    private String sdescription;
    private String sdate;
    private String patientname;
    private String patientbirthdate;
    private String patientsex;
    private String anamnes;
    private String result;
    private String status;
    private String anonimstudyid;
    private String userwhosent;
    private String datesent;
    private String userwhodiagnost;
    private String dateresult;
    private String usergroupwhosees;

    public BitServerStudy(String sid, String shortid, String sdescription, String sdate, String patientname, String patientbirthdate, String patientsex, String anamnes, String result, String status, String anonimstudyid, String userwhosent, String datesent, String userwhodiagnost, String dateresult, String usergroupwhosees) {
        this.sid = sid;
        this.shortid = shortid;
        this.sdescription = sdescription;
        this.sdate = sdate;
        this.patientname = patientname;
        this.patientbirthdate = patientbirthdate;
        this.patientsex = patientsex;
        this.anamnes = anamnes;
        this.result = result;
        this.status = status;
        this.anonimstudyid = anonimstudyid;
        this.userwhosent = userwhosent;
        this.datesent = datesent;
        this.userwhodiagnost = userwhodiagnost;
        this.dateresult = dateresult;
        this.usergroupwhosees = usergroupwhosees;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getShortid() {
        return shortid;
    }

    public void setShortid(String shortid) {
        this.shortid = shortid;
    }

    public String getSdescription() {
        return sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getPatientbirthdate() {
        return patientbirthdate;
    }

    public void setPatientbirthdate(String patientbirthdate) {
        this.patientbirthdate = patientbirthdate;
    }

    public String getPatientsex() {
        return patientsex;
    }

    public void setPatientsex(String patientsex) {
        this.patientsex = patientsex;
    }

    public String getAnamnes() {
        return anamnes;
    }

    public void setAnamnes(String anamnes) {
        this.anamnes = anamnes;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnonimstudyid() {
        return anonimstudyid;
    }

    public void setAnonimstudyid(String anonimstudyid) {
        this.anonimstudyid = anonimstudyid;
    }

    public String getUserwhosent() {
        return userwhosent;
    }

    public void setUserwhosent(String userwhosent) {
        this.userwhosent = userwhosent;
    }

    public String getDatesent() {
        return datesent;
    }

    public void setDatesent(String datesent) {
        this.datesent = datesent;
    }

    public String getUserwhodiagnost() {
        return userwhodiagnost;
    }

    public void setUserwhodiagnost(String userwhodiagnost) {
        this.userwhodiagnost = userwhodiagnost;
    }

    public String getDateresult() {
        return dateresult;
    }

    public void setDateresult(String dateresult) {
        this.dateresult = dateresult;
    }

    public String getUsergroupwhosees() {
        return usergroupwhosees;
    }

    public void setUsergroupwhosees(String usergroupwhosees) {
        this.usergroupwhosees = usergroupwhosees;
    }

    public BitServerStudy(){

    }
}
