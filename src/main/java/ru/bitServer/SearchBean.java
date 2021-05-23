package ru.bitServer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import ru.bitServer.dicom.Patient;
import ru.bitServer.dicom.Study;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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

import static ru.bitServer.beans.MainBean.mainServer;

@ManagedBean(name = "searchBean", eager = true)
@SessionScoped
public class SearchBean {

    public static String authentication;
    //public static String fulladdress = "http://127.0.0.1:8042";////"http://185.59.139.156:8142";//"http://"+mainServer.getIpaddress()+":"+ mainServer.getPort();//"
    //public static String fulladdress = "http://"+MainBean.mainServer.getIpaddress()+":"+MainBean.mainServer.getPort();
    public String searchId;
    public String searchName;
    private static String searchDate;
    public static int searchType = 1;//имя пациента
    public static ArrayList<Patient> patients = new ArrayList<>();
    public static int seachCount;
    //private final JsonParser parserJson = new JsonParser();
    private final SimpleDateFormat format =new SimpleDateFormat("yyyyMMdd");
    public static Date firstdate;
    public static Date seconddate;
    private static List<String> selectedModaliti = new ArrayList<>();
    public String bufferRequest;

    public String getBufferRequest() {
        return bufferRequest;
    }

    public void setBufferRequest(String bufferRequest) {
        this.bufferRequest = bufferRequest;
    }


    public List<String> getSelectedModaliti() {
        return selectedModaliti;
    }

    public void setSelectedModaliti(List<String> selectedModaliti) {
        SearchBean.selectedModaliti = selectedModaliti;
    }

    public Date getFirstdate() {
        return firstdate;
    }

    public void setFirstdate(Date firstdate) {
        SearchBean.firstdate = firstdate;
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
        //FacesContext facesContext = FacesContext.getCurrentInstance();
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
        //PrimeFaces.current().executeScript("alert('"+query.toString()+"');");
        System.out.println(query.toString());
        StringBuilder sb = makePostConnectionAndStringBuilder("/tools/find",query.toString());
        assert sb != null;
        //PrimeFaces.current().executeScript("alert('"+sb.toString()+"');");
        System.out.println(sb.toString());
        //System.out.println(sb);
        String buf = sb.toString();
        getPatientsFromJson(buf);
        patients.sort(Comparator.comparing(Patient::getName));
        seachCount = patients.size();
        PrimeFaces.current().ajax().update(":seachform:dt-patients");
    }

    public void redirectPatientPanel(String patientID) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("patientID", patientID);
        FacesContext.getCurrentInstance().getExternalContext().redirect("patientinfo.xhtml");
    }

    public Boolean firstDateSelect(){
        searchDate = "targetdate";
        PrimeFaces.current().ajax().update(":seachform");
        return true;
    }

    public void secondDateSelect(){
        searchDate = "range";
        PrimeFaces.current().ajax().update(":seachform");
    }

    public void onTypeSearchSelect(){
        searchId = null;
        PrimeFaces.current().ajax().update(":seachform");
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
        String fulladdress = "http://"+ mainServer.getIpaddress()+":"+ mainServer.getPort();
        HttpURLConnection conn = null ;
        URL url = new URL(fulladdress+apiUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        authentication = Base64.getEncoder().encodeToString((mainServer.getLogin()+":"+mainServer.getPassword()).getBytes());
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
               System.out.println("Errot to transfer date");
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
                SimpleDateFormat formatStudy =new SimpleDateFormat("yyyyMMdd");
                studyDateObject = format.parse(studyDate);
            } catch (Exception e) {
              System.out.println("Errot to transfer date");
            }

            String studyDescription = "N/A";
            if(studyDetails.has("StudyDescription")){
                studyDescription=studyDetails.get("StudyDescription").getAsString();
            }
            Study studyObj = new Study(studyDescription, "", studyDateObject, accessionNumber, studyId, patientName, patientId, patientDob, patientSex, parentPatientID, studyInstanceUid);

            if(!patientMap.containsKey(parentPatientID)) {
                Patient patient = new Patient(patientName,patientId,patientBirthDate,patientSex,parentPatientID,1);
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
                int buf = patient.getStudyCount();
                patient.setStudyCount(buf+1);
            }
       }
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
