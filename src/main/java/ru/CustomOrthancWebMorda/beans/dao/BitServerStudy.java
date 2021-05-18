package ru.CustomOrthancWebMorda.beans.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class BitServerStudy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sid;
    private String shortid;
    private String sdescription;
    private Date sdate;
    private String patientname;
    private Date patientbirthdate;
    private String patientsex;
    private String anamnes;
    private String result;
    private String status;
    private String anonimstudyid;
    private String userwhosent;
    private Date datesent;
    private String userwhodiagnost;
    private Date dateresult;
    private String usergroupwhosees;
    private boolean typeresult;
    private boolean locked;



    public BitServerStudy(String sid, String shortid, String sdescription, Date sdate, String patientname, Date patientbirthdate, String patientsex, String anamnes, String result, String status){//, String anonimstudyid, String userwhosent, Date datesent, String userwhodiagnost, Date dateresult, String usergroupwhosees) {
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
//        this.anonimstudyid = anonimstudyid;
//        this.userwhosent = userwhosent;
//        this.datesent = datesent;
//        this.userwhodiagnost = userwhodiagnost;
//        this.dateresult = dateresult;
//        this.usergroupwhosees = usergroupwhosees;
    }

    public boolean isTyperesult() {
        return typeresult;
    }

    public void setTyperesult(boolean typeresult) {
        this.typeresult = typeresult;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
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

    public Date getSdate() {
        return sdate;
    }

    public void setSdate(Date sdate) {
        this.sdate = sdate;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public Date getPatientbirthdate() {
        return patientbirthdate;
    }

    public void setPatientbirthdate(Date patientbirthdate) {
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

    public String getUserwhodiagnost() {
        return userwhodiagnost;
    }

    public void setUserwhodiagnost(String userwhodiagnost) {
        this.userwhodiagnost = userwhodiagnost;
    }

    public Date getDatesent() {
        return datesent;
    }

    public void setDatesent(Date datesent) {
        this.datesent = datesent;
    }

    public Date getDateresult() {
        return dateresult;
    }

    public void setDateresult(Date dateresult) {
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
