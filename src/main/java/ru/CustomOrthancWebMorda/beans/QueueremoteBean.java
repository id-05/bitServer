package ru.CustomOrthancWebMorda.beans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.xml.internal.ws.client.RequestContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;
import ru.CustomOrthancWebMorda.beans.dao.BitServerStudy;
import ru.CustomOrthancWebMorda.beans.dao.Usergroup;
import ru.CustomOrthancWebMorda.beans.dao.Users;
import ru.CustomOrthancWebMorda.beans.dicom.Study;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

import static ru.CustomOrthancWebMorda.beans.MainBean.info;
import static ru.CustomOrthancWebMorda.beans.MainBean.mainServer;

@ManagedBean(name = "queueremoteBean", eager = false)
@SessionScoped
public class QueueremoteBean implements UserDao {

    public String filtrDate = "today";
    public Date firstdate;
    public Date seconddate;
    public String typeSeach = "Не описан";
    private static List<String> selectedModaliti = new ArrayList<>();
    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public static String authentication;
    private List<BitServerStudy> visibleStudiesList;
    private List<BitServerStudy> selectedVisibleStudies;
    private BitServerStudy selectedVisibleStudy;
    public List<Usergroup> usergroupList;
    public String selectedUserGroup;
    public List<String> usergroupListRuName;
    public Users currentUser;
    public String currentUserId;
    private UploadedFile resultFile;
    private String bufResult;

    public String getBufResult() {
        return bufResult;
    }

    public void setBufResult(String bufResult) {
        this.bufResult = bufResult;
    }

    public UploadedFile getResultFile() {
        return resultFile;
    }

    public void setResultFile(UploadedFile resultFile) {
        this.resultFile = resultFile;
    }

    public List<String> getUsergroupListRuName() {
        usergroupListRuName = new ArrayList<String>();
        for(Usergroup bufgroup:usergroupList){
            usergroupListRuName.add(bufgroup.getRuName());
        }
        return usergroupListRuName;
    }

    public void setUsergroupListRuName(List<String> usergroupListRuName) {
        this.usergroupListRuName = usergroupListRuName;
    }

    public String getSelectedUserGroup() {
        return selectedUserGroup;
    }

    public void setSelectedUserGroup(String selectedUserGroup) {
        this.selectedUserGroup = selectedUserGroup;
    }

    public List<Usergroup> getUsergroupList() {
        return usergroupList;
    }

    public void setUsergroupList(List<Usergroup> usergroupList) {
        this.usergroupList = usergroupList;
    }

    public static List<String> getSelectedModaliti() {
        return selectedModaliti;
    }

    public static void setSelectedModaliti(List<String> selectedModaliti) {
        QueueremoteBean.selectedModaliti = selectedModaliti;
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
        selectedVisibleStudy = new BitServerStudy();
        HttpSession session = SessionUtils.getSession();
        currentUserId = session.getAttribute("username").toString();
        System.out.println("  currentUser   "+currentUser);

        System.out.println("QueueremoteBean");

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

        usergroupList = getActiveBitServerUsergroupList();
        selectedUserGroup = usergroupList.get(0).getRuName();
        dataoutput();
        PrimeFaces.current().ajax().update(":seachform:dt-studys");
    }

    public void dataoutput() {
        currentUser = getUserById(currentUserId);
        visibleStudiesList = getBitServerStudyOnAnalisis(currentUser.getGroupUser());
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

    public void addResult() throws IOException {

        selectedVisibleStudy.setStatus("Описан");
        selectedVisibleStudy.setUserwhodiagnost(currentUserId);
        selectedVisibleStudy.setDateresult(new Date());
        if(resultFile!=null){
            Path folder = Paths.get(MainBean.pathToSaveResult);
            String extension = FilenameUtils.getExtension(resultFile.getFileName());
            Path file2 = Files.createFile(Paths.get(folder.toString()+"/" + selectedVisibleStudy.getSid() + "." + extension));
            try (InputStream input = resultFile.getInputStream()) {
                Files.copy(input, file2, StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e){
                System.out.println("error"+e.getMessage().toString());
            }
            selectedVisibleStudy.setTyperesult(true);
            selectedVisibleStudy.setResult(file2.toString());
        }else{
            selectedVisibleStudy.setTyperesult(false);
            selectedVisibleStudy.setResult(bufResult);
        }
        selectedVisibleStudy.setLocked(false);
        updateStudyInBitServerStudyTable(selectedVisibleStudy);

        dataoutput();
    }

//    public void sendToAgent(){
//        JsonObject query = new JsonObject();
//        JsonObject queryDetails = new JsonObject();
//        queryDetails.addProperty("PatientName", "Hello");
//        queryDetails.addProperty("0010-1001", "World");
//        query.add("Replace", queryDetails);
//        JsonArray queryArray = new JsonArray();
//        queryArray.add("StudyDescription");
//        queryArray.add("SeriesDescription");
//        query.add("Keep",queryArray);
//        query.addProperty("KeepPrivateTags",true);
//        query.addProperty("DicomVersion","2017c");
//        int i = 0;
//        for(BitServerStudy bufStudy:selectedVisibleStudies){
//            if(!bufStudy.getStatus().equals("Отправлен на описание")) {
//                StringBuilder sb = makePostConnectionAndStringBuilder("/studies/" + bufStudy.getSid() + "/anonymize", query.toString());
//                JsonParser parserJson = new JsonParser();
//                JsonObject studies = (JsonObject) parserJson.parse(sb.toString());
//                bufStudy.setAnonimstudyid(studies.get("ID").getAsString());
//                bufStudy.setStatus("Отправлен на описание");
//                bufStudy.setDatesent(new Date());
//                bufStudy.setUsergroupwhosees(selectedUserGroup);
//                bufStudy.setUserwhosent(currentUser);
//                updateStudyInBitServerStudyTable(bufStudy);
//                i++;
//            }else{
//                showMessage("Внимание","Исследование "+bufStudy.getShortid()+" "+bufStudy.getPatientname()+" уже было отправлено ранее!",info);
//            }
//
//        }
//        showMessage("Внимание","Всего отправлено: "+i,info);
//        selectedVisibleStudies = null;
//        dataoutput();
//        PrimeFaces.current().executeScript("PF('visibleStudy').unselectAllRows()");
//        PrimeFaces.current().ajax().update(":seachform:dt-studys");
//        PrimeFaces.current().ajax().update(":seachform:send-button");
//    }

    public void addAnamnes(){
        updateStudyInBitServerStudyTable(selectedVisibleStudy);
    }

    public boolean hasSelectedStudy() {
        return this.selectedVisibleStudies != null && !this.selectedVisibleStudies.isEmpty();
    }

    public void showMessage(String title, String note, FacesMessage.Severity type) {
        FacesMessage message = new FacesMessage(title, note);
        message.setSeverity(type);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void redirectToOsimis(String sid) {
        System.out.println("osimis id for redirect "+sid);
        PrimeFaces.current().executeScript("window.open('http://192.168.1.58:8042/osimis-viewer/app/index.html?study="+sid+"','_blank')");
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        this.resultFile = null;
        UploadedFile file = event.getFile();
        if(file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            this.resultFile = file;
        }
    }

    public void lockedStudy(){
        System.out.println(selectedVisibleStudy.getId());
        resultFile = null;
        //selectedVisibleStudy.setLocked(true);
        //updateStudyInBitServerStudyTable(selectedVisibleStudy);
        PrimeFaces.current().executeScript("PF('uploadResultFile').reset()");
        PrimeFaces.current().ajax().update(":seachform:editorcomponent");
        PrimeFaces.current().ajax().update(":seachform:selectfilename");
        //заносим в бд запись о том что данное исследование в настоящий момент обрабатывается
        System.out.println("запись обрабатываемся!");
    }
}

