package ru.CustomOrthancWebMorda.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.primefaces.PrimeFaces;
import ru.CustomOrthancWebMorda.beans.dao.Users;
import ru.CustomOrthancWebMorda.beans.dicom.Patient;
import ru.CustomOrthancWebMorda.beans.dicom.Study;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static ru.CustomOrthancWebMorda.beans.MainBean.mainServer;

@ManagedBean(name = "queueBean", eager = false)
@SessionScoped
public class QueueBean implements UserDao {

    public String filtrDate = "today";
    public Date firstdate;
    public Date seconddate;
    public String typeSeach = "Не описан";
    private static List<String> selectedModaliti = new ArrayList<>();
    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public static String authentication;

    public ArrayList<Study> studiesFromRestApi = new ArrayList<>();
    public List<BitServerStudy> studiesFromTableBitServer = new ArrayList<>();


    private List<BitServerStudy> visibleStudiesList;
    private List<BitServerStudy> selectedVisibleStudies;
    private BitServerStudy selectedVisibleStudy;

    public static List<String> getSelectedModaliti() {
        return selectedModaliti;
    }

    public static void setSelectedModaliti(List<String> selectedModaliti) {
        QueueBean.selectedModaliti = selectedModaliti;
    }

    public List<BitServerStudy> getVisibleStudiesList() {
        return visibleStudiesList;
    }

    public void setVisibleStudiesList(List<BitServerStudy> visibleStudiesList) {
        this.visibleStudiesList = visibleStudiesList;
    }

    public List<BitServerStudy> getSelectedVisibleStudies() {
        return selectedVisibleStudies;
    }

    public void setSelectedVisibleStudies(List<BitServerStudy> selectedVisibleStudies) {
        this.selectedVisibleStudies = selectedVisibleStudies;
    }

    public BitServerStudy getSelectedVisibleStudy() {
        return selectedVisibleStudy;
    }

    public void setSelectedVisibleStudy(BitServerStudy selectedVisibleStudy) {
        this.selectedVisibleStudy = selectedVisibleStudy;
    }

    public String getFiltrDate() {
        return filtrDate;
    }

    public void setFiltrDate(String filtrDate) {
        this.filtrDate = filtrDate;
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

    public String getTypeSeach() {
        return typeSeach;
    }

    public void setTypeSeach(String typeSeach) {
        this.typeSeach = typeSeach;
    }

    ////Status
    /// Описан
    /// Отправлен на описание
    /// Не описан


    @PostConstruct
    public void init() {
        System.out.println("QueueBean");
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
        firstdate = new Date();
        seconddate = new Date();
        readStudyFromDB();
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public Boolean firstDateSelect() {
        filtrDate = "targetdate";
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
        return true;
    }

    public Boolean secondDateSelect() {
        filtrDate = "range";
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
        return true;
    }

    public void dataoutput() {
        visibleStudiesList = getBitServerStudy(typeSeach);
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public void readStudyFromDB() {
        JsonObject query = new JsonObject();
        query.addProperty("Level", "Studies");
        query.addProperty("CaseSensitive", false);
        query.addProperty("Expand", true);
        query.addProperty("Limit", 0);
        JsonObject queryDetails = new JsonObject();
        String dateStr;

        String dateStartFromBase = "20190101";
        seconddate = new Date();
        dateStr = dateStartFromBase + "-" + format.format(seconddate);
        queryDetails.addProperty("StudyDate", dateStr);
        queryDetails.addProperty("PatientID", "*");

        StringBuilder modalities = new StringBuilder();
        for (String buf : selectedModaliti) {
            modalities.append(buf).append("\\");
        }
        queryDetails.addProperty("Modality", modalities.toString());
        query.add("Query", queryDetails);
        System.out.println(query.toString());
   //     {"Level":"Studies","CaseSensitive":false,"Expand":true,"Limit":0,"Query":{"StudyDate":"20210101-20210516","PatientID":"*","Modality":"CR\\CT\\MR\\NM\\PT\\US\\XA\\CR\\MG\\DX\\"}}

        StringBuilder sb = makePostConnectionAndStringBuilder("/tools/find", query.toString());
        assert sb != null;
        System.out.println(sb.toString());
        Boolean existInTable = false;
        studiesFromRestApi = getStudiesFromJson(sb.toString());
        studiesFromTableBitServer = getAllBitServerStudy();
        for(Study bS:studiesFromRestApi){
            existInTable = false;
            for(BitServerStudy bBSS:studiesFromTableBitServer){
                if(bS.getOrthancId().equals(bBSS.getSid())){
                    existInTable = true;
                }
            }
            if(!existInTable) {
                BitServerStudy buf = new BitServerStudy(bS.getOrthancId(), bS.getShortId(), bS.getStudyDescription(), bS.getDate(), bS.getPatientName(), bS.getPatientBirthDate(), bS.getPatientSex(), "","","Не описан");//,"","",null,"",null,"");
                addStudyInBitServerStudyTable(buf);
            }
        }

        visibleStudiesList = getBitServerStudy(typeSeach);
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public static StringBuilder makePostConnectionAndStringBuilder(String apiUrl, String post) {
        StringBuilder sb = null;
        try {
            sb = new StringBuilder();
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
        String fulladdress = "http://" + mainServer.getIpaddress() + ":" + mainServer.getPort();
        HttpURLConnection conn = null;
        URL url = new URL(fulladdress + apiUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        authentication = Base64.getEncoder().encodeToString((mainServer.getLogin() + ":" + mainServer.getPassword()).getBytes());
        if (authentication != null) {
            conn.setRequestProperty("Authorization", "Basic " + authentication);
        }
        OutputStream os = conn.getOutputStream();
        os.write(post.getBytes());
        os.flush();
        conn.getResponseMessage();
        return conn;
    }

    private ArrayList<Study> getStudiesFromJson(String data) {
        JsonParser parserJson = new JsonParser();
        JsonArray studies = (JsonArray) parserJson.parse(data);
        Iterator<JsonElement> studiesIterator = studies.iterator();
        ArrayList<Study> studyList = new ArrayList<>();
        studyList.clear();

        while (studiesIterator.hasNext()) {
            JsonObject studyData = (JsonObject) studiesIterator.next();
            JsonObject parentPatientDetails = null;
            if (studyData.has("PatientMainDicomTags")) {
                parentPatientDetails = studyData.get("PatientMainDicomTags").getAsJsonObject();
            }
            String parentPatientID = studyData.get("ParentPatient").getAsString();
            String studyId = studyData.get("ID").getAsString();
            JsonObject studyDetails = studyData.get("MainDicomTags").getAsJsonObject();
            String patientBirthDate = "N/A";
            String patientSex = "N/A";
            String patientName = "N/A";
            String patientId = "N/A";
            String patientDobString = null;
            Date patientDob = null;

            if (parentPatientDetails.has("PatientBirthDate")) {
                patientDobString = parentPatientDetails.get("PatientBirthDate").getAsString();
            }

            try {
                patientDob = format.parse(patientDobString);
                //String dateString = new SimpleDateFormat("d MMM yyyy").format(patientDob);
                //patientBirthDate = dateString;
            } catch (Exception e) {
                System.out.println("Errot to transfer date");
            }

            if (parentPatientDetails.has("PatientSex")) {
                patientSex = parentPatientDetails.get("PatientSex").getAsString();
            }

            if (parentPatientDetails.has("PatientName")) {
                patientName = parentPatientDetails.get("PatientName").getAsString();
            }

            if (parentPatientDetails.has("PatientID")) {
                patientId = parentPatientDetails.get("PatientID").getAsString();
            }
            String accessionNumber = "N/A";
            if (studyDetails.has("AccessionNumber")) {
                accessionNumber = studyDetails.get("AccessionNumber").getAsString();
            }
            String studyInstanceUid = studyDetails.get("StudyInstanceUID").getAsString();
            String studyDate = null;
            Date studyDateObject = null;
            if (studyDetails.has("StudyDate")) {
                studyDate = studyDetails.get("StudyDate").getAsString();
            }

            try {
                studyDateObject = format.parse("19000101");
                assert studyDate != null;
                //SimpleDateFormat formatStudy = new SimpleDateFormat("yyyyMMdd");
                studyDateObject = format.parse(studyDate);
            } catch (Exception e) {
                System.out.println("Errot to transfer date");
            }

            try {
                studyDateObject = format.parse("19000101");
                assert studyDate != null;
                studyDateObject = format.parse(studyDate);
            } catch (Exception e) {
                System.out.println("Errot to transfer date");
            }

            String studyDescription = "N/A";
            if (studyDetails.has("StudyDescription")) {
                studyDescription = studyDetails.get("StudyDescription").getAsString();
            }

            Study studyObj = new Study(studyDescription, studyDateObject, accessionNumber, studyId, patientName, patientId, patientDob, patientSex, parentPatientID, studyInstanceUid);
            studyList.add(studyObj);
        }
        return studyList;
    }
}
