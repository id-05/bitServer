package ru.bitServer.dao;

import java.io.Serializable;
import java.text.ParseException;
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
    private String patientBirthDateStr;
    private String PatientSex;
    private String anamnes;
    private String result;
    private int status;
    private String rustatus;
    private String anonimstudyid;
    //private String userwhosent;
    private Date datesent;
    //private String userwhodiagnost;
    private Date dateresult;
    private String usergroupwhosees;
    private boolean typeResult;
    //private Date datablock;
    private int userwhoblock;
    private String statusStyle;
    private String Manufacturer;
    private String InstitutionName;
    private String StationName;
    private String AETSource;

    SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

    public BitServerStudy(String sid, String shortid, String sdescription, String source, Date sdate, String modality, Date DateAddInbase, String patientname, Date patientbirthdate, String patientsex, String anamnes, String result, int status){//, String anonimstudyid, String userwhosent, Date datesent, String userwhodiagnost, Date dateresult, String usergroupwhosees) {

//        if(sid.length()>59){
//            this.sid = sid.substring(0,59);
//        }else{
            this.sid = sid;
        //}

//        if(shortid.length()>63){
//            this.shortid = shortid.substring(0,63);
//        }else{
            this.shortid = shortid;
        //}

//        if(sdescription.length()>59){
//            this.sdescription = sdescription.substring(0,59);
//        }else{
            this.sdescription = sdescription;
//        }

//        if(source.length()>59){
//            this.source = source.substring(0,59);
//        }else{
            this.source = source;
       // }

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
                          String Manufacturer, String InstitutionName, String StationName, String AetSource){
        this.sid = sid;
        this.shortid = shortid;
        this.sdescription = sdescription;
        this.sdate = sdate;
        this.modality = modality;
        this.PatientName = patientname;
        this.PatientBirthDate = patientbirthdate;
        this.patientBirthDateStr = new SimpleDateFormat("dd.MM.yyyy").format(patientbirthdate);
        this.PatientSex = patientsex;
        this.status = status;
        this.Manufacturer = Manufacturer;
        this.InstitutionName = InstitutionName;
        this.StationName = StationName;
        this.AETSource = AetSource;
    }

    public BitServerStudy(String sid, Date sdate){
        this.sid = sid;
        this.sdate = sdate;
    }

    public BitServerStudy(String sid, String shortid, Date sdate, String patientname){
        this.sid = sid;
        this.shortid = shortid;
        this.sdate = sdate;
        this.PatientName = patientname;
    }

    public BitServerStudy(String studyDate, String studyDescription, String patientName, String patientBirthDate, String patientID, String patientSex, String modality) throws ParseException {
        this.sdate = FORMAT.parse(studyDate);
        this.sdescription = studyDescription;
        this.PatientName = patientName;
        this.PatientBirthDate = FORMAT.parse(patientBirthDate);
        this.patientBirthDateStr = patientBirthDate;
        this.PatientSex = patientSex;
        this.sid = patientID;
        this.shortid = patientID;
        this.modality = modality;
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

    public boolean isTypeResult() {
        return typeResult;
    }

    public void setTypeResult(boolean typeResult) {
        this.typeResult = typeResult;
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

    public void setStatusStyle(String statusStyle) {
        this.statusStyle = statusStyle;
    }

    public String getStatusStyle() {
        switch (status){
            case 0: this.statusStyle = "noresult";
                break;
            case 1:
            case 3:
                this.statusStyle = "inprocess";
                break;
            case 2: this.statusStyle = "yesresult";
                break;
            default: break;
        }
        return statusStyle;
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

    public String getAETSource() {
        return AETSource;
    }

    public void setAETSource(String AETSource) {
        this.AETSource = AETSource;
    }

    public String getPatientBirthDateStr() {
        return patientBirthDateStr;
    }

    public void setPatientBirthDateStr(String patientBirthDateStr) {
        this.patientBirthDateStr = patientBirthDateStr;
    }

    public BitServerStudy(){

    }

}
