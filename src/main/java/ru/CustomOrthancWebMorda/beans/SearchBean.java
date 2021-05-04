package ru.CustomOrthancWebMorda.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@ManagedBean(name = "searchBean", eager = true)
@ViewScoped
public class SearchBean {

    public static String authentication;
    public static String fulladdress = "http://185.59.139.156:8142";
    public String searchId;
    public String searchName;
    private static String searchDate;
    public int searchType = 1;//имя пациента
    public ArrayList<Patient> patients = new ArrayList<>();
    public int seachCount;
    private final JsonParser parserJson = new JsonParser();
    private SimpleDateFormat format =new SimpleDateFormat("yyyyMMdd");
    public Date firstdate;
    public Date seconddate;
    private List<String> selectedModaliti = new ArrayList<>();

    public List<String> getSelectedModaliti() {
        return selectedModaliti;
    }

    public void setSelectedModaliti(List<String> selectedModaliti) {
        this.selectedModaliti = selectedModaliti;
    }

    public Date getFirstdate() {
        return firstdate;
    }

    public void setFirstdate(Date firstdate) {
        this.firstdate = firstdate;
    }

    public Date getSeconddate() {
        return seconddate;
    }

    public void setSeconddate(Date seconddate) {
        this.seconddate = seconddate;
    }

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
        searchDate = "today";
        System.out.println("seach");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        firstdate = new Date();
        seconddate = new Date();
        selectedModaliti.clear();
        selectedModaliti.add("CR");
        selectedModaliti.add("CT");
        selectedModaliti.add("MR");
        selectedModaliti.add("NM");
        selectedModaliti.add("PT");
        selectedModaliti.add("US");
        selectedModaliti.add("XA");
        selectedModaliti.add("CR");
        selectedModaliti.add("MG");
        selectedModaliti.add("DX");
    }

    public void seach() throws IOException {
        System.out.println("seach start");
       // String param = "{\"Level\":\"Study\",\"CaseSensitive\":false,\"Expand\":true,\"Limit\":0,\"Query\":{\"StudyDate\":\"20210101-20210429\",\"PatientID\":\"*\",\"Modality\":\"MR\\\\\"}}";
       // String param = "{\"Level\":\"Study\",\"CaseSensitive\":false,\"Expand\":true,\"Limit\":0,\"Query\":{\"StudyDate\":\"*\",\"PatientID\":\"*\",\"Modality\":\"MR\\\\\"}}";
        JsonObject query=new JsonObject();
        query.addProperty("Level", "Studies");
        query.addProperty("CaseSensitive", false);
        query.addProperty("Expand", true);
        query.addProperty("Limit", 0);
        JsonObject queryDetails=new JsonObject();
        String dateStr;
        switch (searchDate){
            case "today":
                Date bufDate = new Date();
                dateStr = format.format(bufDate)+"-"+format.format(bufDate);
                queryDetails.addProperty("StudyDate", dateStr);
                break;
            case "yesterday":
                Instant now = Instant.now();
                Instant yesterday = now.minus(1, ChronoUnit.DAYS);
                Date myDate = Date.from(yesterday);
                dateStr = format.format(myDate )+"-"+format.format(myDate);
                queryDetails.addProperty("StudyDate", dateStr);
                break;
            case "targetdate":
                dateStr = format.format(firstdate)+"-"+format.format(firstdate);
                queryDetails.addProperty("StudyDate", dateStr);
                break;
            case "range":
                dateStr = format.format(firstdate)+"-"+format.format(seconddate);
                queryDetails.addProperty("StudyDate", dateStr);
                break;
            default:
                dateStr = "*";
                queryDetails.addProperty("StudyDate", dateStr);
                break;
        }

        switch (searchType){
            case 0:
                if(searchId!=null){
                    queryDetails.addProperty("PatientID", searchId);
                }else{
                    queryDetails.addProperty("PatientID", "*");
                }
                break;
            case 1: queryDetails.addProperty("PatientID", "*");
                break;
            default:break;
        }

        StringBuilder modalities=new StringBuilder();
        for(String buf:selectedModaliti){
            modalities.append(buf).append("\\");
        }
        queryDetails.addProperty("Modality", modalities.toString());

        query.add("Query", queryDetails);
        System.out.println(query.toString());
        StringBuilder sb = makePostConnectionAndStringBuilder("/tools/find",query.toString());
        System.out.println(sb);
        assert sb != null;
        String buf = sb.toString();
        getPatientsFromJson(buf);
        patients.sort(Comparator.comparing(Patient::getName));
        seachCount = patients.size();
        PrimeFaces.current().ajax().update(":seachform:dt-patients");

        for(Patient bufP:patients){
            System.out.println(bufP.getName());
            HashMap<String,Study> bufhashmap = bufP.getChildStudies();
            for(String key : bufP.getChildStudies().keySet()){
                System.out.println("study Key: " + key);
            }
        }
    }

    public void onTypeSearchSelect(){
        searchId = null;
        PrimeFaces.current().ajax().update(":seachform");
    }

    public void redirect(String str) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(str);
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
               //print("Errot to transfer date");
            }

            if(parentPatientDetails.has("PatientSex")) {
                patientSex = parentPatientDetails.get("PatientSex").getAsString();
            }

            if(parentPatientDetails.has("PatientName")) {
                patientName=parentPatientDetails.get("PatientName").getAsString();
            }

            if(parentPatientDetails.has("PatientID")) {
                patientId = parentPatientDetails.get("PatientID").getAsString();
            }
            String accessionNumber = "N/A";
            if(studyDetails.has("AccessionNumber")) {
                accessionNumber = studyDetails.get("AccessionNumber").getAsString();
            }
            String studyInstanceUid = studyDetails.get("StudyInstanceUID").getAsString();
            String studyDate = null;
            Date studyDateObject = null;
            if(studyDetails.has("StudyDate")) {
                studyDate = studyDetails.get("StudyDate").getAsString();
            }

            try {
                studyDateObject = format.parse("19000101");
                assert studyDate != null;
                studyDateObject=format.parse(studyDate);
            } catch (Exception e) {
              //  MainActivity.print("Errot to transfer date");
            }

            String studyDescription = "N/A";
            if(studyDetails.has("StudyDescription")){
                studyDescription=studyDetails.get("StudyDescription").getAsString();
            }
            Study studyObj = new Study(studyDescription, studyDateObject, accessionNumber, studyId, patientName, patientId, patientDob, patientSex, parentPatientID, studyInstanceUid);

            if(!patientMap.containsKey(parentPatientID)) {
                Patient patient = new Patient(patientName,patientId,patientBirthDate,patientSex,parentPatientID);
                patient.addStudy(studyObj);
                patientMap.put(parentPatientID, patient);
                switch (searchType){
                    case 0: //поиск по id
                        patients.add(patient);
                        break;
                    case 1: // поиск по фамилии
                        if(searchId!=null){
                            if(patient.name.toUpperCase().contains(searchId.toUpperCase())){
                                patients.add(patient);
                            }
                        }else
                        {
                            patients.add(patient);
                        }
                        break;
                    default:break;
                }
            }else {
                Patient patient = patientMap.get(parentPatientID);
                patient.addStudy(studyObj);
            }
       }
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
