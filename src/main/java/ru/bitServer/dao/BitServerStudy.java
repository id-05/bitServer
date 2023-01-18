package ru.bitServer.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitServerStudy implements Serializable {

    private Long id;
    private String sid;
    private String shortid;
    private String modality;
    private String sdescription;
    private String source;
    private Date sdate;
    private Date DateAddInbase;
    private String PatientName;
    private Date PatientBirthDate;
    private String PatientSex;
    private String anamnes;
    private String result;
    private int status;
    private String rustatus;
    private String anonimstudyid;
    private String userwhosent;
    private Date datesent;
    private String userwhodiagnost;
    private Date dateresult;
    private String usergroupwhosees;
    private boolean typeresult;
    private Date datablock;
    private int userwhoblock;
    private String statusstyle;
    private String Manufacturer;
    private String InstitutionName;
    private String StationName;
    private String AetSource;

    public BitServerStudy(String sid, String shortid, String sdescription, String source, Date sdate, String modality, Date DateAddInbase, String patientname, Date patientbirthdate, String patientsex, String anamnes, String result, int status){//, String anonimstudyid, String userwhosent, Date datesent, String userwhodiagnost, Date dateresult, String usergroupwhosees) {

        if(sid.length()>59){
            this.sid = sid.substring(0,59);
        }else{
            this.sid = sid;
        }

        if(shortid.length()>63){
            this.shortid = shortid.substring(0,63);
        }else{
            this.shortid = shortid;
        }

        if(sdescription.length()>59){
            this.sdescription = sdescription.substring(0,59);
        }else{
            this.sdescription = sdescription;
        }

        if(source.length()>59){
            this.source = source.substring(0,59);
        }else{
            this.source = source;
        }

        this.sdate = sdate;
        this.modality = modality;
        this.DateAddInbase = DateAddInbase;
        this.PatientName = patientname;
        this.PatientBirthDate = patientbirthdate;
        this.PatientSex = patientsex;
        this.anamnes = anamnes;
        this.result = result;
        this.status = status;
    }

    public BitServerStudy(String sid, String shortid, String sdescription, Date sdate, String modality, String patientname, Date patientbirthdate, String patientsex, int status,
                          String Manufacturer, String InstitutionName, String StationName, String AetSource){//, String anonimstudyid, String userwhosent, Date datesent, String userwhodiagnost, Date dateresult, String usergroupwhosees) {
        if(sid.length()>59){
            this.sid = sid.substring(0,59);
        }else{
            this.sid = sid;
        }
        if(shortid.length()>63){
            this.shortid = shortid.substring(0,63);
        }else{
            this.shortid = shortid;
        }
        this.sdescription = sdescription;
        this.sdate = sdate;
        this.modality = modality;
        this.PatientName = patientname;
        this.PatientBirthDate = patientbirthdate;
        this.PatientSex = patientsex;
        this.status = status;
        this.Manufacturer = Manufacturer;
        this.InstitutionName = InstitutionName;
        this.StationName = StationName;
        this.AetSource = AetSource;
    }

    public BitServerStudy(String sid, Date sdate){
        if(sid.length()>59){
            this.sid = sid.substring(0,59);
        }else{
            this.sid = sid;
        }
        this.sdate = sdate;
    }

    public BitServerStudy(String sid, String shortid, Date sdate, String patientname){//, String anonimstudyid, String userwhosent, Date datesent, String userwhodiagnost, Date dateresult, String usergroupwhosees) {
        if(sid.length()>59){
            this.sid = sid.substring(0,59);
        }else{
            this.sid = sid;
        }
        if(shortid.length()>63){
            this.shortid = shortid.substring(0,63);
        }else{
            this.shortid = shortid;
        }
        this.sdate = sdate;
        this.PatientName = patientname;
    }

    public String getPreview() {
//        OrthancRestApi connection = new OrthancRestApi(mainServer.getIpaddress(),mainServer.getPort(),mainServer.getLogin(),mainServer.getPassword());
//        StringBuilder sb = connection.makeGetConnectionAndStringBuilder("/studies/"+sid);
//        JsonParser parserJsonSerie = new JsonParser();
//        JsonObject bufObj = (JsonObject) parserJsonSerie.parse(sb.toString());
//        JsonArray series= bufObj.get("Series").getAsJsonArray();
//        sb = connection.makeGetConnectionAndStringBuilder("/series/"+series.get(0).getAsString());
//        bufObj = (JsonObject) parserJsonSerie.parse(sb.toString());
//        JsonArray instances= bufObj.get("Instances").getAsJsonArray();
        return "";//"http://"+mainServer.getIpaddress()+":"+mainServer.getPort()+"/instances/"+instances.get(0).getAsString()+"/preview";
    }

    public int getUserwhoblock() {
        return userwhoblock;
    }

    public void setUserwhoblock(int userwhoblock) {
        this.userwhoblock = userwhoblock;
    }

    public void setDatablock(Date datablock) {
        this.datablock = datablock;
    }

    public boolean isTyperesult() {
        return typeresult;
    }

    public void setTyperesult(boolean typeresult) {
        this.typeresult = typeresult;
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

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getSdescription() {
        return sdescription;
    }

    public void setSdescription(String sdescription) {
        this.sdescription = sdescription;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getSdate() {
        return sdate;
    }

    public String getStringStudyDate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(sdate);
    }

    public void setSdate(Date sdate) {
        this.sdate = sdate;
    }

    public Date getDateAddInbase() {
        return DateAddInbase;
    }

    public void setDateAddInbase(Date dateAddInbase) {
        this.DateAddInbase = dateAddInbase;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        this.PatientName = patientName;
    }

    public Date getPatientBirthDate() {
        return PatientBirthDate;
    }

    public String getStringPatientBirthdate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(PatientBirthDate);
    }

    public void setPatientBirthDate(Date patientBirthDate) {
        this.PatientBirthDate = patientBirthDate;
    }

    public String getPatientSex() {
        return PatientSex;
    }

    public void setPatientSex(String patientSex) {
        this.PatientSex = patientSex;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRustatus() {
        switch (status){
            case 0: this.rustatus = "Не описан";
                break;
            case 1: this.rustatus = "Отправлен на описание";
                break;
            case 2: this.rustatus = "Описан";
                break;
            case 3: this.rustatus = "Заблокирован для описания";
                break;
            default: break;
        }
        return rustatus;
    }

    public void setStatusstyle(String statusstyle) {
        this.statusstyle = statusstyle;
    }

    public String getStatusstyle() {
        switch (status){
            case 0: this.statusstyle = "noresult";
                break;
            case 1:
            case 3:
                this.statusstyle = "inprocess";
                break;
            case 2: this.statusstyle = "yesresult";
                break;
            default: break;
        }
        return statusstyle;
    }

    public void setRustatus(String rustatus) {

        this.rustatus = rustatus;
    }

    public String getAnonimstudyid() {
        return anonimstudyid;
    }

    public void setAnonimstudyid(String anonimstudyid) {
        this.anonimstudyid = anonimstudyid;
    }

    public void setUserwhosent(String userwhosent) {
        this.userwhosent = userwhosent;
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

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getInstitutionName() {
        return InstitutionName;
    }

    public void setInstitutionName(String institutionName) {
        InstitutionName = institutionName;
    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public String getAetSource() {
        return AetSource;
    }

    public void setAetSource(String aetSource) {
        AetSource = aetSource;
    }

    public BitServerStudy(){

    }

}
