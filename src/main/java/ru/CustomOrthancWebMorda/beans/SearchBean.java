package ru.CustomOrthancWebMorda.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean(name = "searchBean", eager = true)
@ViewScoped
public class SearchBean {

    public static String authentication;
    public static String fulladdress ="http://185.59.139.156:8142";
    public String searchId;
    public String searchName;
    public String searchDate;
    public int searchType;
    public ArrayList<Patient> patients = new ArrayList<>();
    public int seachCount;
    private JsonParser parserJson = new JsonParser();
    private SimpleDateFormat format =new SimpleDateFormat("yyyyMMdd");

    public int getSeachCount() {
        return seachCount;
    }

    public void setSeachCount(int seachCount) {
        this.seachCount = seachCount;
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    FacesMessage.Severity info = FacesMessage.SEVERITY_INFO;
    FacesMessage.Severity error = FacesMessage.SEVERITY_ERROR;
    FacesMessage.Severity warning = FacesMessage.SEVERITY_WARN;

    public void SeachBean(String myArg){}

    @PostConstruct
    public void init() {
        System.out.println("seach");
    }

    public void seach() throws IOException {
        System.out.println("seach start");
        String param = "{\"Level\":\"Studies\",\"CaseSensitive\":false,\"Expand\":true,\"Limit\":0,\"Query\":{\"StudyDate\":\"20210101-20210429\",\"PatientID\":\"*\",\"Modality\":\"MR\\\\\"}}";
        StringBuilder sb = makePostConnectionAndStringBuilder("/tools/find",param );
        System.out.println(sb);
        assert sb != null;
        String buf = sb.toString();
        getPatientsFromJson(buf);
        seachCount = patients.size();
        PrimeFaces.current().ajax().update(":seachform:dt-patients");
    }

    public void clickOpenPatient(){
       // String param1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentPatientID");
      //  System.out.println("clickopenpatient");
      //  System.out.println(param1);
        //PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
        //return ("index.xhtml");
    }

    public static StringBuilder makePostConnectionAndStringBuilder(String apiUrl, String post) {
        StringBuilder sb =null;
        try {
            sb=new StringBuilder();
            HttpURLConnection conn = makePostConnection(apiUrl, post);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                int i = output.indexOf("}");
                    sb.append(output);
            }
            conn.disconnect();
            conn.getResponseMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb;
    }

    public static HttpURLConnection makePostConnection(String apiUrl, String post) throws Exception {
        HttpURLConnection conn = null ;
        URL url = new URL(fulladdress+apiUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        authentication = Base64.getEncoder().encodeToString(("doctor:doctor").getBytes());
        if(authentication != null){
            conn.setRequestProperty("Authorization", "Basic " + authentication);
        }
        OutputStream os = conn.getOutputStream();
        os.write(post.getBytes());
        os.flush();
        conn.getResponseMessage();
        return conn;
    }

    private void getPatientsFromJson(String data){
        HashMap<String, Patient> patientMap = new HashMap<>();
        JsonParser parserJson = new JsonParser();
        JsonArray studies = (JsonArray) parserJson.parse(data);
        Iterator<JsonElement> studiesIterator = studies.iterator();
        patients.clear();
        int i = 0;

        while (studiesIterator.hasNext()) {
            JsonObject studyData = (JsonObject) studiesIterator.next();
            JsonObject  parentPatientDetails = null;
            if(studyData.has("PatientMainDicomTags")) {parentPatientDetails = studyData.get("PatientMainDicomTags").getAsJsonObject(); }
            String parentPatientID = studyData.get("ParentPatient").getAsString();
            String studyId=studyData.get("ID").getAsString();
            JsonObject studyDetails = studyData.get("MainDicomTags").getAsJsonObject();
            String patientBirthDate = "N/A";
            String patientSex = "N/A";
            String patientName = "N/A";
            String patientId = "N/A";
            String patientDobString = null;
            Date patientDob = null;

            if(parentPatientDetails.has("PatientBirthDate"))
                { patientDobString = parentPatientDetails.get("PatientBirthDate").getAsString(); }

            try {
                patientDob = format.parse(patientDobString);
                String dateString = new SimpleDateFormat("d MMM yyyy").format(patientDob);
                patientBirthDate = dateString;
            } catch (Exception e) {

               // MainActivity.print("Errot to transfer date");
            }

            if(parentPatientDetails.has("PatientSex")) { patientSex=parentPatientDetails.get("PatientSex").getAsString(); }
            if(parentPatientDetails.has("PatientName"))
                { patientName=parentPatientDetails.get("PatientName").getAsString();
                    System.out.println(patientName);
                }
            if(parentPatientDetails.has("PatientID")) { patientId=parentPatientDetails.get("PatientID").getAsString(); }
            String accessionNumber="N/A";
            if(studyDetails.has("AccessionNumber")) {accessionNumber=studyDetails.get("AccessionNumber").getAsString();}
            String studyInstanceUid=studyDetails.get("StudyInstanceUID").getAsString();
            String studyDate=null;
            Date studyDateObject=null;
            if(studyDetails.has("StudyDate")) { studyDate=studyDetails.get("StudyDate").getAsString(); }

            try {
                studyDateObject=format.parse("19000101");
                assert studyDate != null;
                studyDateObject=format.parse(studyDate);
            } catch (Exception e) {
              //  MainActivity.print("Errot to transfer date");
            }

            String studyDescription="N/A";
            if(studyDetails.has("StudyDescription")){ studyDescription=studyDetails.get("StudyDescription").getAsString(); }
            Study studyObj=new Study(studyDescription, studyDateObject, accessionNumber, studyId, patientName, patientId, patientDob, patientSex, parentPatientID, studyInstanceUid);
            if(!patientMap.containsKey(parentPatientID)) {
                Patient patient = new Patient(patientName,patientId,patientBirthDate,patientSex,parentPatientID);
                patient.addStudy(studyObj);
                patientMap.put(parentPatientID, patient);
                patients.add(patient);
            }

       }

    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
