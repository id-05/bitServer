package ru.bitServer.service;

public class WorkListRecord {
    String PatientId;
    String PatientName;
    String DateBirth;
    String AcceccionNumber;
    String Sex;
    String DateCreate;
    String Description;
    String Modality;

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }

    public String getDateBirth() {
        return DateBirth;
    }

    public String getAcceccionNumber() {
        return AcceccionNumber;
    }

    public String getSex() {
        return Sex;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getModality() {
        return Modality;
    }

    public void setModality(String modality) {
        Modality = modality;
    }

    public String getDateCreate() {
        return DateCreate;
    }

    public void setDateCreate(String dateCreate) {
        DateCreate = dateCreate;
    }

    public WorkListRecord(String patientId, String patientName, String dateBirth, String acceccionNumber, String sex, String dateCreate, String description, String modality) {
        PatientId = getClearString(patientId);
        PatientName = patientName;
        DateBirth = dateBirth;
        AcceccionNumber = acceccionNumber;
        Sex = sex;
        DateCreate = dateCreate.substring(0,8);
        Description = description;
        Modality = modality;
    }

    public void print(){
        System.out.print(PatientId+"\n"+
                PatientName+"\n"+
                DateBirth+"\n"+
                AcceccionNumber+"\n"+
                Sex+"\n"+
                DateCreate+"\n"+
                Description+"\n"+
                Modality+"\n"
        );
    }

    private String getClearString(String buf){
        int i = buf.indexOf("^");
        if(i!=-1){
            return buf.substring(0,i);
        }
        else return buf;
    }
}
